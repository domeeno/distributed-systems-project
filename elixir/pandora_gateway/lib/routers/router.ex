defmodule Gateway.Router do
  use Plug.Router
  require Logger

  plug(Plug.Static,
    at: "/",
    from: :pandora_gateway
  )

  plug(CORSPlug)

  plug(:dispatch)
  plug(:match)

  plug(
    Plug.Parsers,
    parsers: [:json],
    pass: ["application/json"],
    json_decoder: Poison
  )

  get "/status" do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(200, "all good")
  end

  # userservice
  forward("/user", to: Router.UserRouter)

  # subjectservice
  # forward("/subject", to: Router.SubjectRouter)

  # userservice
  # forward("/file", to: Router.FileRouter)

  match _ do
    send_resp(conn, 404, "404")
  end

  # Helper functions

  # TODO handle this duplication later
  defp handle_response(response) do
    case response do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {200, body}

      {:ok, %HTTPoison.Response{status_code: 404}} ->
        {404, "Not found :("}

      {:error, %HTTPoison.Error{reason: reason}} ->
        # TODO replace with logger
        IO.inspect(reason)
        {500, "Something went wrong"}
    end
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
