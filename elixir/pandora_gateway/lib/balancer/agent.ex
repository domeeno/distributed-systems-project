defmodule LoadBalancer.Agent do
  use GenServer
  require Logger

  def start_link(args) do
    {:ok, pid} = GenServer.start_link(__MODULE__, args, name: String.to_atom(args.service))
    Registry.register(Registry.ViaTest, args.service, pid)
    Logger.info("Load Balancer for #{args.service} at address #{args.address} started")
    {:ok, pid}
  end

  def init(args) do
    {:ok,
     %{
       :ports => args.ports,
       :address => args.address,
       :port_index => 0,
       :alive_services => args.ports
     }}
  end

  def handle_call({:get_request, url}, _from, state) do
    address = state.address <> ":" <> Enum.at(state.alive_services, state.port_index) <> url
    response = HTTPoison.get(address)
    {:reply, response, Map.put(state, :port_index, switch_port(state))}
  end

  def handle_call({:post_request, url, params}, _from, state) do
    address = state.address <> ":" <> Enum.at(state.alive_services, state.port_index) <> url
    IO.inspect(address)

    response =
      HTTPoison.post(address, params, [
        {"Content-Type", "application/json"}
      ])

    {:reply, response, Map.put(state, :port_index, switch_port(state))}
  end

  defp switch_port(state) do
    if state.port_index + 1 > length(state.alive_services) - 1 do
      0
    else
      state.port_index + 1
    end
  end
end
