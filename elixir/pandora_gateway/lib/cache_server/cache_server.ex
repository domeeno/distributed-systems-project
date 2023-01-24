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
    {:ok, %{socket: socket}}
  end

  def handle_call({:call, command}, _from, state) do
    data = send_and_recv(state.socket, command)
    {:reply, data, state}
  end

  defp send_and_recv(socket, command) do
    :ok = :gen_tcp.send(socket, command)
    {:ok, data} = :gen_tcp.recv(socket, 0, 1000)
    data
  end
end
