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

  get "/users" do
    {code, body} = call("http://localhost:8080/user/all")
    send_resp(conn, code, body)
  end

  match _ do
    send_resp(conn, 404, "404")
  end

  defp call(url) do
    case HTTPoison.get(url) do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {200, body}
      {:ok, %HTTPoison.Response{status_code: 404}} ->
        {404, "Not found :("}
      {:error, %HTTPoison.Error{reason: reason}} ->
        IO.inspect reason
        {500, "Something went wrong"}
    end
  end
end
