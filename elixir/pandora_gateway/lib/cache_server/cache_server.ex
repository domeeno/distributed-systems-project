defmodule Cache.CacheServer do
  use GenServer
  require Logger

  def start_link(args) do
    {:ok, pid} = GenServer.start_link(__MODULE__, args, name: :cache_server)
    Registry.register(Registry.ViaTest, "cache_server", pid)
    {:ok, pid}
  end

  def init(args) do
    opts = [:binary, packet: :line, active: false]

    case connect_socket(args.address, args.port, opts) do
      {:ok, socket} ->
        IO.inspect(socket)
        :gen_tcp.close(socket)

      _ ->
        {}
    end

    {:ok,
     %{
       address: '#{args.address}',
       port: args.port,
       opts: opts,
       buckets: []
     }}
  end

  def handle_call({:buckets}, _from, state) do
    {:reply, state.buckets, state}
  end

  def handle_call({:update, bucket, key, data}, _from, state) do
    opts = [:binary, packet: :line, active: false]

    case connect_socket(state.address, state.port, opts) do
      {:ok, socket} ->
        bucket_list =
          if !Enum.member?(state.buckets, bucket) do
            result = create_bucket(bucket, socket)

            case result do
              {:ok, bucket} ->
                [bucket | state.buckets]

              {:error} ->
                state.buckets
            end
          else
            state.buckets
          end

        if Enum.member?(bucket_list, bucket) do
          # [chunks, number_of_chunks]
          chunk_data = data_to_chunks(data)

          result = send_chunks(socket, bucket, key, chunk_data)

          case result do
            {:ok, response} ->
              Logger.info("Cached: " <> key)
              {:reply, response, Map.put(state, :buckets, bucket_list)}

            # error handling
            _ ->
              Logger.info("Couldn't update cache for: " <> key)
              {:reply, "error", Map.put(state, :buckets, bucket_list)}
          end
        else
          :gen_tcp.close(socket)
          {:reply, "error", state}
        end

      _ ->
        Logger.info("Couldn't update cache for: " <> key)
        {:reply, "error", state}
    end
  end

  def handle_call({:query, bucket, key}, _from, state) do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = :gen_tcp.connect('#{state.address}', state.port, opts)

    operation = "0|0|get|#{bucket}|#{key}|\r\n"
    :ok = :gen_tcp.send(socket, operation)

    response = receive_data(socket, "")

    case String.split(response, "|") do
      ["OK", "NOT FOUND", _] ->
        {:reply, {:not_found}, state}

      ["OK", data, _] ->
        {:reply, {:found, data}, state}

      ["ERROR", "BAD REQUEST", _] ->
        Logger.error("Error getting in #{bucket}: #{key}")
        {:reply, {:not_found}, state}
    end
  end

  defp create_bucket(bucket, socket) do
    response = send_and_recv(socket, "0|0|create|#{bucket}|\r\n")
    IO.inspect(response)

    case response do
      "OK|CREATE\r\n" ->
        Logger.info("#{response} - Created bucket: #{bucket}")
        {:ok, bucket}

      ["ERROR", "BAD REQUEST", _] ->
        Logger.error("Couldn't create bucket: #{bucket}")
        {:error}
    end
  end

  defp connect_socket(address, port, opts) do
    connection = :gen_tcp.connect('#{address}', port, opts)

    case connection do
      {:ok, socket} ->
        Logger.info("Established connection with Cache server at #{address}:#{port}")
        {:ok, socket}

      {:error, reason} ->
        Logger.error("Couldn't connect to Cache Server at #{address}:#{port}, reason: #{reason}")
        {:error}
    end
  end

  defp receive_data(socket, data) do
    {:ok, response} = :gen_tcp.recv(socket, 0)

    case response do
      "OK\r\n" -> data
      "OK|NOT FOUND|\r\n" -> response
      _ -> receive_data(socket, data <> response)
    end
  end

  defp send_recv_chunk(socket, bucket, key, data) do
    operation = "0|0|put|#{bucket}|#{key}|#{data}|\r\n"
    response = send_and_recv(socket, operation)
    :gen_tcp.close(socket)
    response
  end

  defp send_chunks(socket, bucket, key, [chunks, number_of_chunks]) do
    if number_of_chunks == 1 do
      result = send_recv_chunk(socket, bucket, key, hd(chunks))
      {:ok, result}
      # handle multiple chunks
    else
      operation = "1|#{number_of_chunks}|put|#{bucket}|#{key}|#{hd(chunks)}\r\n"
      Logger.info("Sending chunk - " <> "1" <> "/" <> Integer.to_string(number_of_chunks))
      :ok = :gen_tcp.send(socket, operation)
      result = send_chunk(socket, tl(chunks), number_of_chunks, 2)
      {:ok, result}
    end
  end

  defp send_chunk(socket, [head | tail], number_of_chunks, chunk_number) do
    operation = "#{chunk_number}|#{number_of_chunks}|#{head}"

    Logger.info(
      "Sending chunk - " <>
        Integer.to_string(chunk_number) <> "/" <> Integer.to_string(number_of_chunks)
    )

    IO.inspect("...")

    if length(tail) != 1 do
      send_to(socket, operation <> "|\r\n")
    else
      send_to(socket, operation <> "|\r\n")
    end

    IO.inspect("chunk " <> Integer.to_string(chunk_number) <> " sent")
    send_chunk(socket, tail, number_of_chunks, chunk_number + 1)
  end

  defp send_chunk(socket, [], _number_of_chunks, _chunk_number) do
    IO.inspect("waiting response.... ")
    response = :gen_tcp.recv(socket, 0, 1000)
    IO.inspect(response)
    :gen_tcp.close(socket)
    response
  end

  defp data_to_chunks(data) do
    data =
      String.replace(data, ~r/\s\s+/, " ")
      |> String.replace("\n", " ")

    chunks = Regex.scan(~r/.{1,1000}/, data)
    [chunks, length(chunks)]
  end

  defp send_to(socket, command) do
    :gen_tcp.send(socket, command)
  end

  defp send_and_recv(socket, command) do
    :ok = :gen_tcp.send(socket, command)
    {:ok, data} = :gen_tcp.recv(socket, 0, 1000)
    IO.inspect("this is the ok message" <> data)
    data
  end

  defp log_response(response, bucket, key) do
    case response do
      ["OK", _, _] ->
        Logger.info("Cached in #{bucket}: #{key}")

      ["ERROR", "BAD REQUEST", _] ->
        Logger.error("Error caching in #{bucket}: #{key}")
    end
  end
end
