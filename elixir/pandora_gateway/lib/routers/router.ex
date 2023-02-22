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
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(200, "all good")
  end

  # userservice
  forward("/user", to: UserRouter)

  # subjectservice
  forward("/subject", to: SubjectRouter)

  # fileservice
  # forward("/file", to: Router.FileRouter)

  # service discovery
  forward("/service", to: ServiceRouter)

  match _ do
    send_resp(conn, 404, "404")
  end
end
