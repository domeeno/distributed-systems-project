defmodule LoadBalancer.Agent do
  use GenServer
  require Logger

  def start_link(args) do
    {:ok, pid} = GenServer.start_link(__MODULE__, args, name: String.to_atom(args.service))
    Registry.register(Registry.ViaTest, args.service, pid)
    Logger.info("Load Balancer for #{args.service} started")
    {:ok, pid}
  end

  def init(args) do
    {:ok,
     %{
       :addrs => [],
       :addr_index => 0,
       :alive_addrs => []
     }}
  end

  def handle_call({:register, service, address}, _from, state) do
    Logger.info("[DISCOVERY]: Registering address for #{service} service: #{address}")
    {:reply, :ok, Map.put(state, :alive_addrs, [address | state.addrs])}
  end

  def handle_call({:get_request, url}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url
    IO.inspect(address)
    response = HTTPoison.get(address)
    IO.inspect(response)
    {:reply, response, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:post_request, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url
    IO.inspect(address)

    response =
      HTTPoison.post(address, params, [
        {"Content-Type", "application/json"}
      ])

    {:reply, response, Map.put(state, :addr_index, switch_port(state))}
  end

  defp switch_port(state) do
    if state.addr_index + 1 > length(state.alive_addrs) - 1 do
      0
    else
      state.addr_index + 1
    end
  end
end
