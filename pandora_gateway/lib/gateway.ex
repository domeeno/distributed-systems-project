defmodule Gateway do
  use Application
  require Logger

  @app_envs Application.compile_env!(:pandora_gateway, :services)

  def start(_type, _args) do

    IO.inspect(@app_envs.user_service)

    children = [
      Plug.Cowboy.child_spec(
        scheme: :http,
        plug: Gateway.Router,
        options: [
          dispatch: dispatch(),
          port: 4000
        ]
      ),
      Registry.child_spec(
        keys: :duplicate,
        name: Registry.Gateway
      ),
      # For each service a balancer
      %{id: Balancer.UserService, start: {Balancer.Agent, :start_link, [@app_envs.user_service]}},
      # %{id: Balancer.SubjectService, start: {Balancer.Agent, :start_link, [%{:address => "127.0.0.1", :ports => ["8085", "8086", "8087"]}]}},
      # %{id: Balancer.BucketService, start: {Balancer.Agent, :start_link, [%{:address => "127.0.0.1", :ports => ["8085", "8086", "8087"]}]}}
    ]

    opts = [strategy: :one_for_one, name: Gateway.Application]

    Logger.info("Server has started at port: 4000")

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
