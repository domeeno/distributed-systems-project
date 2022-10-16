defmodule Gateway.Router do
  use Plug.Router

  plug(Plug.Static,
    at: "/",
    from: :pandora_gateway
  )

  plug(:match)

  plug(Plug.Parsers,
    parsers: [:json],
    pass: ["application/json"],
    json_decoder: Jason
  )

  plug(:dispatch)

  get "/" do
    send_resp(conn, 200, "Hello World!")
  end
end
