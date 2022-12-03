defmodule LoadBalancer.Supervisor do
  use DynamicSupervisor

  def start_link(services) do
    supervisor = DynamicSupervisor.start_link(__MODULE__, :ok, name: __MODULE__)
    :ok = start_balancer(services)
    supervisor
  end

  def get_count() do
    DynamicSupervisor.count_children(__MODULE__).active
  end

  def start_balancer(services) do
    start_b(services)
  end

  defp start_b([head | tail]) do
    DynamicSupervisor.start_child(__MODULE__, {LoadBalancer.Agent, head})
    start_b(tail)
  end

  defp start_b([]) do
    :ok
  end

  def init(:ok) do
    DynamicSupervisor.init(
      strategy: :one_for_one,
      max_restarts: 1_000
    )
  end
end
