defmodule Gateway do
  use Application
  require Logger

  @app Application.compile_env!(:pandora_gateway, :app)

  def start(_type, _args) do
    children = [
      # Layers:
      #
      # 1. Router 
      Plug.Cowboy.child_spec(
        scheme: :http,
        plug: Gateway.Router,
        options: [
          port: @app.port
        ]
      )

    ]

    opts = [strategy: :one_for_one, name: Gateway.Application]

    Logger.info("Server has started at port: #{@app.port}")

    Supervisor.start_link(children, opts)
  end

end
