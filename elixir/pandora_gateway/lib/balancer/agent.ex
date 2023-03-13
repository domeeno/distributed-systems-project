defmodule LoadBalancer.Agent do
  use GenServer
  require Logger

  @reroutes Application.compile_env!(:pandora_gateway, :reroutes)

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
    {:reply, :ok, Map.put(state, :alive_addrs, [address | state.alive_addrs])}
  end

  def handle_call({:request, request_type, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    request_id = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{request_id} routing GET to #{address}")

    {status, body} = make_request(request_type, address, params, request_id)

    {status, body} = if (status != 200) do
        reroute_request(
          request_type,
          url,
          params,
          Map.put(state, :addr_index, switch_port(state)),
          request_id,
          @reroutes
        )
    else 
      {status, body}
    end

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  defp reroute_request(method, url, params, state, request_id, 1) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    Logger.info(
      "REROUTING REQUEST-ID: #{request_id}, ATTEMPT #{@reroutes}/#{@reroutes} rereouting GET to #{address}"
    )

    make_request(method, address, params, request_id)
  end

  defp reroute_request(method, url, params, state, request_id, reroutes) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    Logger.info(
      "REROUTING REQUEST-ID: #{request_id}, ATTEMPT #{@reroutes - reroutes + 1}/#{@reroutes} rereouting GET to #{address}"
    )

    {status, body} = make_request(method, address, params, request_id)

    {status, body} = if (status != 200) do
        reroute_request(
          method,
          url,
          params,
          Map.put(state, :addr_index, switch_port(state)),
          request_id,
          reroutes - 1
        )
    else
      {status, body}
    end

    {status, body}
  end

  defp make_request(method, address, params, request_id) do
    case method do
      :get_request ->
        handle_response(
          HTTPoison.request(:get, address, "", [], []),
          request_id
        )

      :post_request ->
        handle_response(
          HTTPoison.request(
            :post,
            address,
            params,
            [{"Content-Type", "application/json"}],
            []
          ),
          request_id
        )

      :put_request ->
        handle_response(
          HTTPoison.request(
            :put,
            address,
            params,
            [{"Content-Type", "application/json"}],
            []
          ),
          request_id
        )

      :delete_request ->
        handle_response(
          HTTPoison.request(:delete, address, "", [], []),
          request_id
        )
    end
  end

  def handle_call({:get_request, url}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    request_id = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{request_id} routing GET to #{address}")

    {status, body} = handle_response(HTTPoison.get(address), request_id)

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:post_request, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    request_id = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{request_id} routing POST to #{address}")

    {status, body} =
      handle_response(
        HTTPoison.post(address, params, [
          {"Content-Type", "application/json"}
        ]),
        request_id
      )

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:put_request, url, params}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    request_id = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{request_id} routing PUT to #{address}")

    {status, body} =
      handle_response(
        HTTPoison.put(address, params, [
          {"Content-Type", "application/json"}
        ]),
        request_id
      )

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  def handle_call({:delete_request, url}, _from, state) do
    address = Enum.at(state.alive_addrs, state.addr_index) <> url

    request_id = UUID.uuid4()
    Logger.info("FORWARD REQUEST-ID: #{request_id} routing DELETE to #{address}")

    {status, body} = handle_response(HTTPoison.delete(address), request_id)

    {:reply, {status, body}, Map.put(state, :addr_index, switch_port(state))}
  end

  defp handle_response(response, request_id) do
    case response do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        Logger.info("FORWARD REQUEST-ID: #{request_id} #{200} response: #{body}")
        {200, body}

      {:ok, %HTTPoison.Response{status_code: 201, body: body}} ->
        Logger.info("FORWARD REQUEST-ID: #{request_id} #{200} response: #{body}")
        {201, body}

      {:ok, %HTTPoison.Response{status_code: 404}} ->
        Logger.error("FORWARD REQUEST-ID: #{request_id} #{404} NOT FOUND")
        {404, "Not found :("}

      {:ok, %HTTPoison.Response{status_code: 418}} ->
        Logger.error("FORWARD REQUEST-ID: #{request_id} #{418} I'm a teapot")
        {418, "I'm a teapot"}

      {:ok, error_response} ->
        IO.inspect(error_response)
        Logger.error("FORWARD REQUEST-ID: #{request_id} #{error_response.status_code} reason: #{error_response.body}")
        {error_response.status_code, error_response.body}

      {:error, %HTTPoison.Error{reason: reason}} ->
        Logger.error("FORWARD REQUEST-ID: #{request_id} #{500} reason: #{reason}")
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
