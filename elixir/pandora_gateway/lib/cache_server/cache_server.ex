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
        disconnect_socket(socket)

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
          disconnect_socket(socket)
          {:reply, "error", state}
        end

      _ ->
        Logger.info("Couldn't update cache for: " <> key)
        {:reply, "error", state}
    end
  end

  def handle_call({:query, bucket, key}, _from, state) do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = connect_socket('#{state.address}', state.port, opts)

    operation = "0|0|get|#{bucket}|#{key}|\r\n"
    :ok = :gen_tcp.send(socket, operation)

    response = receive_data(socket, "")
    IO.inspect(response)

    case String.split(response, "|") do
      ["OK", "NOT FOUND", _] ->
        disconnect_socket(socket)
        {:reply, {:not_found}, state}

      ["OK", data, _] ->
        Logger.info("Found #{key} cache in bucket: \"#{bucket}\"")
        disconnect_socket(socket)
        {:reply, {:found, data}, state}

      ["ERROR", "BAD REQUEST", _] ->
        Logger.error("Error getting in #{bucket}: #{key}")
        disconnect_socket(socket)
        {:reply, {:not_found}, state}
    end
  end

  def handle_call({:delete, bucket, key}, _from, state) do
    Logger.info("Deleting key #{key}: #{bucket}")
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = connect_socket('#{state.address}', state.port, opts)

    operation = "0|0|del|#{bucket}|#{key}|\r\n"
    IO.inspect(operation)
    :ok = :gen_tcp.send(socket, operation)
    response = receive_data(socket, "")
    disconnect_socket(socket)

    case String.split(response, "|") do
      ["OK", data, _] ->
        Logger.info("Deleted #{key} cache in bucket: \"#{bucket}\"")
        {:reply, {:ok, data}, state}

      _ ->
        Logger.error("Error deleting in #{bucket}: #{key}")
        {:reply, {:error}, state}
    end
  end

  defp create_bucket(bucket, socket) do
    Logger.info("bucket: \"#{bucket}\", not found. Creating \"#{bucket}\"")
    response = send_and_recv(socket, "0|0|create|#{bucket}|\r\n")

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

  defp disconnect_socket(socket) do
    Logger.info("Closing Connection")
    :gen_tcp.close(socket)
  end

  defp receive_data(socket, data) do
    {:ok, response} = :gen_tcp.recv(socket, 0)

    case response do
      "OK\r\n" -> data
      "OK|NOT FOUND|\r\n" -> response
      "OK|DEL|\r\n" -> data
      _ -> receive_data(socket, data <> response)
    end
  end

  defp send_recv_chunk(socket, bucket, key, data) do
    operation = "0|0|put|#{bucket}|#{key}|#{data}|\r\n"
    response = send_and_recv(socket, operation)
    disconnect_socket(socket)
    response
  end

  defp send_chunks(socket, bucket, key, [chunks, number_of_chunks]) do
    if number_of_chunks == 1 do
      result = send_recv_chunk(socket, bucket, key, hd(chunks))
      {:ok, result}
      # handle multiple chunks
    else
      operation = "1|#{number_of_chunks}|put|#{bucket}|#{key}|#{hd(chunks)}\r\n"
      Logger.info("#{key}: Sending chunk - " <> "1" <> "/" <> Integer.to_string(number_of_chunks))
      # Sending first chunk 
      :ok = :gen_tcp.send(socket, operation)
      Logger.info("#{key}: chunk " <> "1" <> " sent")
      result = send_chunk(socket, tl(chunks), number_of_chunks, 2, key)
      {:ok, result}
    end
  end

  defp send_chunk(socket, [head | tail], number_of_chunks, chunk_number, key) do
    operation = "#{chunk_number}|#{number_of_chunks}|#{head}"

    Logger.info(
      "#{key}: Sending chunk - " <>
        Integer.to_string(chunk_number) <> "/" <> Integer.to_string(number_of_chunks)
    )

    if length(tail) != 1 do
      send_to(socket, operation <> "|\r\n")
    else
      send_to(socket, operation <> "|\r\n")
    end

    Logger.info("#{key}: chunk " <> Integer.to_string(chunk_number) <> " sent")
    send_chunk(socket, tail, number_of_chunks, chunk_number + 1, key)
  end

  defp send_chunk(socket, [], _number_of_chunks, _chunk_number, _key) do
    response = :gen_tcp.recv(socket, 0, 1000)
    disconnect_socket(socket)
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
