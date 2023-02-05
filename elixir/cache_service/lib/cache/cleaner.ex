defmodule Cache.Cleaner do
  use GenServer

  def init(pid, interval) do
    IO.puts("Cleaner for #{pid} started")
    :timer.send_interval(interval, :clean)
    {:ok, %{bucket: pid}}
  end

  def init(init_arg) do
    {:ok, init_arg}
  end

  def handle_info(:clean, state) do
    Cache.Bucket.clean(state.bucket)
    {:noreply, state}
  end
end
