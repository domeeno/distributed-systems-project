defmodule Router.Subject do
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
    Get Subject tree, 

    returns a nested JSON file with all the topics

    Cached
  """
  get "/:id" do
    case GenServer.call(:cache_server, {:query, "subject", id}) do
      {:found, data} ->
        respond(conn, 200, data)

      _ ->
        {status, body} =
          GenServer.call(
            :subject,
            {:get_request, "/subject/#{id}"}
          )

        Logger.info("Caching subject: #{id}")
        GenServer.call(:cache_server, {:update, "subject", id, body})
        respond(conn, status, body)
    end
  end

  @doc ~S"""
    Get All topics from all subjects, test rquest 
  """
  get "/topic" do
    {status, body} =
      GenServer.call(
        :subject,
        {:get_request, "subject/topic"}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    Create a subject and store it's id in a user's subject list 
  """
  post "/:userSubjectsId/user/:userId" do
    {status, body} =
      GenServer.call(
        :subject,
        {:post_request, "/#{userSubjectsId}/user/#{userId}", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    Create a topic, specifying the subject and parent topic it belongs to.

    Removes cached subject tree
  """
  post "/:subjectId/parent/:topicId" do
    # Subject tree gets updated so we delete the entry in cache if exists
    GenServer.call(:cache_server, {:delete, "subject", subjectId})

    {status, body} =
      GenServer.call(
        :subject,
        {:post_request, "/#{subjectId}/parent/#{topicId}"}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    add a subject to user's liked list
  """
  put "user/:likedId/like/:subjectId" do
    {status, body} =
      GenServer.call(
        :subject,
        {:put_request, "user/#{likedId}/like/#{subjectId}", Poison.encode!(conn.body_params)}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    get user created subjects list 
  """
  get "user/subjects/:id" do
    {status, body} =
      GenServer.call(
        :subject,
        {:get_request, "/user/subjects/#{id}"}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    get user saved subjects list 
  """
  get "user/saves/:id" do
    {status, body} =
      GenServer.call(
        :subject,
        {:get_request, "/user/saves/#{id}"}
      )

    respond(conn, status, body)
  end

  @doc ~S"""
    get user saved subjects list 
  """
  get "user/likes/:id" do
    {status, body} =
      GenServer.call(
        :subject,
        {:get_request, "/user/likes/#{id}"}
      )

    respond(conn, status, body)
  end

  defp respond(conn, code, body) do
    conn
    |> put_resp_content_type("application/json")
    |> send_resp(code, body)
  end
end
