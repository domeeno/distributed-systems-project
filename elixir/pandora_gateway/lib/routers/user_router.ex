defmodule UserRouter do
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
      handle_response(
        GenServer.call(
          :userservice,
          {:get_request, "user/#{user_id}/info"}
        )
      )

    respond(conn, status, body)
  end

  post "/register" do
    {status, body} =
      handle_response(
        GenServer.call(
          :userservice,
          {:post_request, "/register", Poison.encode!(conn.body_params)}
        )
      )

    respond(conn, status, body)
  end

  post "/login" do
    {status, body} =
      handle_response(
        GenServer.call(
          :userservice,
          {:post_request, "/user/login", Poison.encode!(conn.body_params)}
        )
      )

    respond(conn, status, body)
  end

  # TODO handle this duplication later
  defp handle_response(response) do
    case response do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {200, body}

      {:ok, %HTTPoison.Response{status_code: 404}} ->
        {404, "Not found :("}

      {:error, %HTTPoison.Error{reason: reason}} ->
        Logger.error(reason)
        {500, "Something went wrong"}
    end
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
