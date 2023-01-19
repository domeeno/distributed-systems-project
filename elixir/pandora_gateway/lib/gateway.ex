defmodule Gateway do
  use Application
  require Logger

  @app Application.compile_env!(:pandora_gateway, :app)

  def start(_type, _args) do
    Logger.info("Cache has port #{@app.cache.port}")

    children = [
      %{id: Registry, start: {Registry, :start_link, [:duplicate, Registry.ViaTest]}},
      Plug.Cowboy.child_spec(
        scheme: :http,
        plug: Gateway.Router,
        options: [
          dispatch: dispatch(),
          port: @app.port
        ]
      ),
      Registry.child_spec(
        keys: :duplicate,
        name: Registry.Gateway
      ),
      %{id: ServiceSupervisor, start: {LoadBalancer.Supervisor, :start_link, []}}
    ]

    opts = [strategy: :one_for_one, name: Gateway.Application]

    Logger.info("Server has started at port: #{@app.port}")

    Supervisor.start_link(children, opts)
  end

  defp dispatch do
    [
      {:_,
       [
         {:_, Plug.Cowboy.Handler, {Gateway.Router, []}}
       ]}
    ]
  end
end
