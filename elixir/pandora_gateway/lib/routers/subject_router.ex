defmodule SubjectRouter do
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

  get "/:id" do
    {status, body} =
      handle_response(
        GenServer.call(
          :subject,
          {:get_request, "/subject/#{id}"}
        )
      )

    respond(conn, status, body)
  end

  post "/topic" do
    {status, body} =
      handle_response(
        GenServer.call(
          :subject,
          {:post_request, "/topic", Poison.encode!(conn.body_params)}
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
        Logger.error(%{response: response, reason: reason})
        {500, "Something went wrong"}
    end
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
