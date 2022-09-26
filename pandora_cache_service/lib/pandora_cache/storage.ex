defmodule PandoraCache.Storage do
  use Agent, restart: :temporary

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
    Agent.get(storage, fn state ->
      Map.get(state, key)
    end)
  end

  @doc """
    Put a new value in the cache storage
  """
  def put(storage, key, value) do
    Agent.update(storage, fn state ->
      Map.put(state, key, value)
    end)
  end

  @doc """
    Delete subject from storage
  """
  def delete(storage, key) do
    Agent.get_and_update(storage, fn state ->
      Map.pop(state, key)
    end)
  end
end
