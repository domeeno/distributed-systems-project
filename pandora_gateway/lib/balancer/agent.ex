defmodule LoadBalancer.Agent do
  use GenServer

  def start_link(args) do
    {:ok, pid} = GenServer.start_link(__MODULE__, args, name: String.to_atom(args.service))
    Registry.register(Registry.ViaTest, args.service, pid)
    {:ok, pid}
  end

  def init(arg) do
    IO.inspect(arg)
    {:ok,
     %{
       :ports => arg.ports,
       :address => arg.address,
       :current_port => 0,
       :alive_services => arg.ports
    }}
  end

  def status() do
    "Good"
  end
end
