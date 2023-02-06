defmodule Cache.Bucket do
  use Agent, restart: :temporary

  @caching Application.compile_env!(:cache_service, :caching)
  @interval @caching.interval
  @allowed_overhead @caching.allowed_overhead
  @optimal_nr_entries @caching.optimal_nr_entries

  def start_link(_opts) do
    {:ok, pid} = Agent.start_link(fn -> %{} end)
    # GenServer.start_link(Cache.Cleaner, {pid, @interval})
    {:ok, pid}
  end

  def get(bucket, key) do
    Agent.get(bucket, &Map.get(&1, key))
  end

  def put(bucket, key, value) do
    if(length(get_keys(bucket)) >= @allowed_overhead + @optimal_nr_entries) do
      clean(bucket)
    end

    Agent.update(bucket, &Map.put(&1, key, value))
  end

  def delete(bucket, key) do
    Agent.get_and_update(bucket, &Map.pop(&1, key))
  end

  def get_keys(bucket) do
    Agent.get(bucket, &Map.keys(&1))
  end

  def clean(bucket) do
    IO.inspect("Cleanup...")
    keys = get_keys(bucket)
    keys_length = length(keys)
    delete_key(bucket, keys, keys_length - @optimal_nr_entries)
  end

  defp delete_key(bucket, _remaining_data, 0) do
    IO.inspect(get_keys(bucket))
  end

  defp delete_key(bucket, [head | tail], counter) do
    delete(bucket, head)
    delete_key(bucket, tail, counter - 1)
  end
end
