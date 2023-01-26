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
    {:ok, socket} = :gen_tcp.connect('#{args.address}', args.port, opts)
    Logger.info("Established connection with Cache server at #{args.address}:#{args.port}")
    _ = send_and_recv(socket, "0|0|create|subject|\r\n")
    _ = send_and_recv(socket, "0|0|create|file|\r\n")
    :gen_tcp.close(socket)

    {:ok,
     %{
       address: '#{args.address}',
       port: args.port,
       opts: opts
     }}
  end

  def handle_call({:update, bucket, key, data}, _from, state) do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = :gen_tcp.connect('#{state.address}', state.port, opts)

    # chunk
    [chunks, number_of_chunks] = data_to_chunks(data)

    IO.inspect("here yes")
    # send_chunkz
    if number_of_chunks == 1 do
      response = send_recv_chunk(socket, bucket, key, hd(chunks))
      log_response(String.split(response, "|"), bucket, key)
      {:reply, response, state}
    else
      response = send_chunks(socket, bucket, key, chunks, number_of_chunks)
      IO.inspect("HEERERERRE???? <> " <> response)
      log_response(String.split(response, "|"), bucket, key)
      {:reply, response, state}
    end
  end

  def handle_call({:query, bucket, key}, _from, state) do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = :gen_tcp.connect('#{state.address}', state.port, opts)

    operation = "0|0|get|#{bucket}|#{key}|\r\n"
    response = send_and_recv(socket, operation)
    _ = send_and_recv(socket, "")

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

  defp send_recv_chunk(socket, bucket, key, data) do
    operation = "0|0|put|#{bucket}|#{key}|#{data}|\r\n"
    response = send_and_recv(socket, operation)
    :gen_tcp.close(socket)
    IO.inspect("HEREEE?????")
    response
  end

  defp send_chunks(socket, bucket, key, [head | tail], number_of_chunks) do
    operation = "1|#{number_of_chunks}|put|#{bucket}|#{key}|#{head}\r\n"
    :ok = :gen_tcp.send(socket, operation)
    result = send_chunk(socket, tail, number_of_chunks, 2)
    IO.inspect("Hdsoajdsad?" <> result)
    result
  end

  defp send_chunk(socket, [head | tail], number_of_chunks, chunk_number) do
    operation = "#{chunk_number}|#{number_of_chunks}|#{head}"
    IO.inspect(operation)

    if number_of_chunks != chunk_number do
      send_to(socket, operation <> "\r\n")
      send_chunk(socket, tail, number_of_chunks, chunk_number + 1)
    else
      IO.inspect("not cool bro " <> operation)
      response = send_and_recv(socket, operation <> "|\r\n")
      :gen_tcp.close(socket)
      response
    end
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
    IO.inspect("nice <>" <> command)
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
