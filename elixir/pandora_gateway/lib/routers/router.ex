defmodule Gateway.Router do
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

  get "/status" do
    Logger.info("status good")

    conn
    |> put_resp_content_type("application/json")
    |> send_resp(200, "all good")
  end

  # userservice
  forward("/user", to: Router.User)

  # subjectservice
  forward("/subject", to: Router.Subject)
  forward("/topic", to: Router.Topic)

  # service discovery
  forward("/service", to: Router.Discovery)

  match _ do
    Logger.error("Undefined request path")
    send_resp(conn, 404, "404")
  end
end
