defmodule Router.File do
  use Plug.Router

  plug :match

  plug :dispatch


  get "/status" do
    send_resp(conn, 200, "Good")
  end
end
