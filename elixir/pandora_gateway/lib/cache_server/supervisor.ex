defmodule Cache.ServerSupervisor do
  use Supervisor

  @app Application.compile_env!(:pandora_gateway, :app)

  def start_link() do
    Supervisor.start_link(__MODULE__, :ok, name: __MODULE__)
  end

  def init(:ok) do
    children = [
      Supervisor.child_spec({Cache.CacheServer, @app.cache},
        id: :cache_client
      )
    ]

    Supervisor.init(children, strategy: :one_for_one)
  end
end
