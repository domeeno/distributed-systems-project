defmodule Router.User do
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

  get ":user_id/info" do
    {status, body} =
      GenServer.call(
        :userservice,
        {:request, :get_request, "/user/#{user_id}/info", ""}
      )

    respond(conn, status, body)
  end

  post "/register" do
    {status, body} =
      GenServer.call(
        :userservice,
        {:request, :post_request, "/register", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  post "/login" do
    {status, body} =
      GenServer.call(
        :userservice,
        {:request, :post_request, "/user/login", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  put ":user_id" do
    {status, body} =
      GenServer.call(
        :userservice,
        {:request, :put_request, "/user/#{user_id}", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
