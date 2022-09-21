defmodule PandoraCache.Storage do
  use Agent

  @doc """
    Starts storage for the Key Value pairs,
  """
  def start_link(_opts) do
    Agent.start_link(fn -> %{} end)
  end

  @doc """
    Get a cached value from storage
  """
  def get(storage, key) do
    Agent.get(storage, &Map.get(&1, key))
  end

  @doc """
    Put a new value in the cache storage
  """
  def put(storage, key, value) do
    Agent.update(storage, &Map.put(&1, key, value))
  end
  
  @doc """
    Delete subject from storage
  """
  def delete(storage, key) do
    Agent.get_and_update(storage, &Map.pop(&1, key))
  end

end
