defmodule LoadBalancer.Agent do
  use GenServer

  def start_link(args) do
    {:ok, pid} = GenServer.start_link(__MODULE__, args, name: String.to_atom(args.service))
    Registry.register(Registry.ViaTest, args.service, pid)
    IO.inspect(args)
    {:ok, pid}
  end


  @impl true
  def init(arg) do
    {:ok,
     %{
       :ports => arg.ports,
       :address => arg.address,
       :current_port => 0,
       :alive_services => arg.ports
    }}
  end

  @impl true
  def handle_call({:status}, _from, state) do
    IO.puts("good")
    {:reply, "Good", state}
  end
end
