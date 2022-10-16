defmodule Gateway do
  use Application

  def start(_type, _args) do
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
      )
    ]

    opts = [strategy: :one_for_one, name: Gateway.Application]
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
