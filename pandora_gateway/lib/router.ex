defmodule Gateway.Router do
  use Plug.Router
  require Logger

  # TODO move to application start
  @user_env Application.compile_env!(:pandora_gateway, :services)

  # TODO service urls to be moved to application start and distributed to the load balancers
  @user_url "http://#{@user_env.user_service.address}:#{@user_env.user_service.ports}"

  plug(Plug.Static,
    at: "/",
    from: :pandora_gateway
  )

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

  get "/users" do
    {status, body} = handle_response(HTTPoison.get("#{@user_url}/user/all"))

    respond(conn, status, body)
  end

  post "/user" do
    {status, body} =
      handle_response(
        HTTPoison.post("#{@user_url}/register", Poison.encode!(conn.body_params), [
          {"Content-Type", "application/json"}
        ])
      )

    respond(conn, status, body)
  end

  post "/login" do 
    {status, body} =
    handle_response(
      HTTPoison.post("#{@user_url}/user/login", Poison.encode!(conn.body_params), [
        {"Content-Type", "application/json"}
      ])
    )

  respond(conn, status, body)
  end

  match _ do
    send_resp(conn, 404, "404")
  end

  # Helper functions

  defp handle_response(response) do
    case response do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {200, body}

      {:ok, %HTTPoison.Response{status_code: 404}} ->
        {404, "Not found :("}

      {:error, %HTTPoison.Error{reason: reason}} ->
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
