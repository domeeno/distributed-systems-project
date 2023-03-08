defmodule Gateway.Router do
  use Plug.Router

  plug :match

  plug :dispatch

  plug CORSPlug


  get "/status" do
    send_resp(conn, 200, "Good")
  end

  forward("/user", to: Router.User)

  forward("/subject", to: Router.Subject)

  forward("/file", to: Router.File)

  forward("/discovery", to: Router.Discovery)

  match _ do 
    send_resp(conn, 404, "Oops!")
  end

end

