defmodule Router.Topic do
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

  @doc ~S"""
    Get All topics from all subjects, test rquest 
  """
  get "/" do
    {status, body} =
      GenServer.call(
        :subject,
        {:request, :get_request, "/topic", ""}
      )

    respond(conn, status, body)
  end

  get "/:id" do
    {status, body} =
      GenServer.call(
        :subject,
        {:request, :get_request, "/topic/#{id}", ""}
      )

    respond(conn, status, body)
  end

  post "/:topic_id/subject/:subject_id" do
    # Subject tree gets updated so we delete the entry in cache if exists
    GenServer.call(:cache_server, {:delete, "subject", subject_id})

    {status, body} =
      GenServer.call(
        :subject,
        {:request, :post_request, "/topic/parent/#{topic_id}", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
