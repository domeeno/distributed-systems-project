defmodule LoadBalancer.Supervisor do
  use Supervisor

  @app_services Application.compile_env!(:pandora_gateway, :services)

  def start_link() do
    Supervisor.start_link(__MODULE__, :ok, name: __MODULE__)
  end

  def init(:ok) do
    children = [
      Supervisor.child_spec({LoadBalancer.Agent, Enum.at(@app_services, 0)}, id: :user_service_worker),
      Supervisor.child_spec({LoadBalancer.Agent, Enum.at(@app_services, 1)}, id: :subject_service_worker),
      Supervisor.child_spec({LoadBalancer.Agent, Enum.at(@app_services, 2)}, id: :file_service_worker)
    ]
    Supervisor.init(children, strategy: :one_for_one)
  end
end
