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

    requestId = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{requestId} routing GET to #{address}")

    {status, body} = handle_response(HTTPoison.get(address), requestId)

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:post_request, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    requestId = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{requestId} routing POST to #{address}")

    {status, body} =
      handle_response(
        HTTPoison.post(address, params, [
          {"Content-Type", "application/json"}
        ]),
        requestId
      )

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:put_request, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    requestId = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{requestId} routing PUT to #{address}")

    {status, body} =
      handle_response(
        HTTPoison.put(address, params, [
          {"Content-Type", "application/json"}
        ]),
        requestId
      )

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:delete_request, url}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    requestId = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{requestId} routing DELETE to #{address}")

    {status, body} = handle_response(HTTPoison.delete(address), requestId)

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  defp handle_response(response, requestId) do
    case response do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        Logger.info("FORWARD REQUEST-ID: #{requestId} #{200} response: #{body}")
        {200, body}

      {:ok, %HTTPoison.Response{status_code: 404}} ->
        Logger.info("FORWARD REQUEST-ID: #{requestId} #{404} NOT FOUND")
        {404, "Not found :("}

      {:error, %HTTPoison.Error{reason: reason}} ->
        Logger.error("FORWARD REQUEST-ID: #{requestId} #{500} reason: #{reason}")
        {500, "Something went wrong"}
    end
  end

  defp switch_port(state) do
    if state.addr_index + 1 > length(state.alive_addrs) - 1 do
      0
    else
      state.addr_index + 1
    end
  end
end
