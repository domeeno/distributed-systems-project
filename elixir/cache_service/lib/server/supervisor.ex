defmodule Cache.ServerSupervisor do
  use Supervisor

  @app Application.compile_env!(:cache_service, :app)

  def start_link(opts) do
    Supervisor.start_link(__MODULE__, :ok, opts)
  end

  @impl true
  def init(:ok) do
    children = [
      {Task.Supervisor, name: Cache.Server.TaskSupervisor},
      Supervisor.child_spec({Task, fn -> Cache.Server.accept(@app.port) end},
        restart: :permanent
      )
    ]

    Supervisor.init(children, strategy: :one_for_all)
  end
end
