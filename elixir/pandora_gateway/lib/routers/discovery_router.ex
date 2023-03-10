defmodule Discovery.Router do
  use Plug.Router
  require Logger

  plug(Plug.Static,
    at: "/",
    from: :pandora_gateway
  )

  plug(CORSPlug)

  plug(:match)

  plug(
    Plug.Parsers,
    parsers: [:json],
    pass: ["application/json"],
    json_decoder: Poison
  )

  plug(:dispatch)

  get "/:service" do
    Logger.info("[DISCOVERY]: registering #{service} service")
    address =
      "http://" <>
        to_string(:inet_parse.ntoa(conn.remote_ip)) <> ":" <> conn.params["port"]

    case service do
      "subject" ->
        GenServer.call(:subject, {:register, service, address})

      "user" ->
        GenServer.call(:userservice, {:register, service, address})

      "file" ->
        GenServer.call(:file, {:register, service, address})

      "cache" ->
        GenServer.call(:cache, {:register, service, address})

      _ ->
        Logger.info("[DISCOVERY]: Unregistered service discovery call: #{service}")
    end

    respond(conn, 200, "Good")
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
