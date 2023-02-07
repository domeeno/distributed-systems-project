defmodule Cache.Bucket do
  use Agent, restart: :temporary
  require Logger

  @caching Application.compile_env!(:cache_service, :caching)
  @interval @caching.interval
  @allowed_overhead @caching.allowed_overhead
  @optimal_nr_entries @caching.optimal_nr_entries

  def start_link(_opts) do
    {:ok, pid} = Agent.start_link(fn -> %{} end)
    GenServer.start_link(Cache.Cleaner, {pid, @interval})
    {:ok, pid}
  end

  def get(bucket, key) do
    Agent.get(bucket, &Map.get(&1, key))
  end

  def put(bucket, key, value) do
    if(length(get_keys(bucket)) >= @allowed_overhead + @optimal_nr_entries) do
      Logger.warn("Cache exceeded allowed limit")
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

  defp clean(bucket) do
    keys = get_keys(bucket)
    keys_length = length(keys)

    if keys_length >= @allowed_overhead + @optimal_nr_entries do
      Logger.info("Cache cleanup...")
      delete_key(bucket, keys, keys_length - @optimal_nr_entries)
    end
  end

  def clean_timer(bucket) do
    keys = get_keys(bucket)
    keys_length = length(keys)

    if keys_length > @optimal_nr_entries do
      Logger.info("Cache cleanup...")
      delete_key(bucket, keys, keys_length - @optimal_nr_entries)
    end
  end

  defp delete_key(_bucket, _remaining_data, 0) do
    Logger.info("Cache cleanup done ✓")
  end

  defp delete_key(bucket, [head | tail], counter) do
    delete(bucket, head)
    delete_key(bucket, tail, counter - 1)
  end
end
