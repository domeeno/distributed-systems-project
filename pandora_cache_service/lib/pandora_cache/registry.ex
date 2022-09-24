defmodule PandoraCache.Registry do
  use GenServer

  @impl true
  def init(:ok) do
    {:ok, %{}}
  end

  @impl true
  def handle_call({:lookup, storage_name}, _from, storage_names) do
    {:reply, Map.fetch(storage_names, storage_name), storage_names}
  end

  @impl true
  def handle_cast({:create, pid_name}, pids_register) do
    if Map.has_key?(pids_register, pid_name) do
      {:noreply, pids_register}
    else
      {:ok, storage} = PandoraCache.Storage.start_link([])
      {:noreply, Map.put(pids_register, pid_name, storage)}
    end
  end
end
