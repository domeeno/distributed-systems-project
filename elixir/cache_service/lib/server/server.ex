defmodule Cache.Server do
  require Logger

  def accept(port) do
    {:ok, socket} =
      :gen_tcp.listen(port, [:binary, active: false, reuseaddr: true])

    Logger.info("Accepting connections on port #{port}")
    loop_acceptor(socket)
  end

  defp loop_acceptor(socket) do
    {:ok, client} = :gen_tcp.accept(socket)
    {:ok, pid} = Task.Supervisor.start_child(Cache.Server.TaskSupervisor, fn -> serve(client) end)
    :ok = :gen_tcp.controlling_process(client, pid)
    loop_acceptor(socket)
  end

  defp serve(socket) do
    msg =
      with {:ok, data} <- read_line(socket),
           {:ok, operation} <- Cache.Parser.parse(data),
           do: Cache.Parser.run(operation)

    write_line(socket, msg)
    serve(socket)
  end

  defp read_line(socket) do
    response = :gen_tcp.recv(socket, 0)
    case response do
      {:ok, data} ->
        IO.inspect(response)
        [chunk, size, command] = String.split(data, "|", parts: 3)
        if (chunk==size) do
          {:ok, command}
        else
          {:ok, read_line_chunks(command, socket)}
        end
      # MATCH EVERYTHING ELSE WITH !:ok
      _ -> response
    end
  end

  defp read_line_chunks(command, socket) do
    {:ok, data} = :gen_tcp.recv(socket, 0)

    [chunk, size, next_chunk] = String.split(data, "|", parts: 3)
    command = String.replace(command, "\r\n", "") <> next_chunk
    if (chunk==size) do
      command
    else
      read_line_chunks(command, socket)
    end
  end

  defp write_line(socket, {:ok, text}) do
    :gen_tcp.send(socket, text)
  end

  defp write_line(socket, {:error, :bad_operation}) do
    :gen_tcp.send(socket, "ERROR|BAD REQUEST|\r\n")
  end

  defp write_line(_socket, {:error, :closed}) do
    exit(:shutdown)
  end

  defp write_line(socket, {:error, :not_found}) do
    :gen_tcp.send(socket, "OK|NOT FOUND|\r\n")
  end

  defp write_line(socket, {:error, error}) do
    :gen_tcp.send(socket, "error\r\n")
    exit(error)
  end
end