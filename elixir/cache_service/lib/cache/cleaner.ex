defmodule Cache.Cleaner do
  use GenServer

  def init({pid, interval}) do
    IO.inspect(pid)
    :timer.send_interval(interval, :clean)
    {:ok, %{bucket: pid}}
  end

  def handle_info(:clean, state) do
    
    Cache.Bucket.clean_timer(state.bucket)
    {:noreply, state}
  end
end
