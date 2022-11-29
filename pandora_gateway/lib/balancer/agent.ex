defmodule Balancer.Agent do
    use GenServer

    def start_link(args) do
        IO.puts("Load Balancer is running for " <> args.address)
        IO.inspect(args.ports)
        GenServer.start_link(__MODULE__, args, name: __MODULE__)
    end

    def init(arg) do
        {:ok, %{
            :ports => arg.ports,
            :address => arg.address,
            :current_port => 0,
            :dead_services => [],
        }}
    end

end