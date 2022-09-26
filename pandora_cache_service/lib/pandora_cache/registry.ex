defmodule PandoraCache.Registry do
  use GenServer

  # Client API
  def start_link(opts) do
    GenServer.start_link(__MODULE__, :ok, opts)
  end

  def lookup(server, name) do
    GenServer.call(server, {:lookup, name})
  end

  def create(server, name) do
    GenServer.cast(server, {:create, name})
  end

  # Server funcs
  @impl true
  def init(:ok) do
    storage_names = %{}
    refs = %{}
    {:ok, {storage_names, refs}}
  end

  @impl true
  def handle_call({:lookup, storage_name}, _from, state) do
    {storage_names, _} = state
    {:reply, Map.fetch(storage_names, storage_name), state}
  end

  @impl true
  def handle_cast({:create, pid_name}, {storage_names, refs}) do
    if Map.has_key?(storage_names, pid_name) do
      {:noreply, {storage_names, refs}}
    else
      {:ok, storage} =
        DynamicSupervisor.start_child(PandoraCache.StorageSupervisor, PandoraCache.Storage)

      ref = Process.monitor(storage)
      refs = Map.put(refs, ref, pid_name)
      storage_names = Map.put(storage_names, pid_name, storage)
      {:noreply, {storage_names, refs}}
    end
  end

  @impl true
  def handle_info({:DOWN, ref, :process, _pid, _reason}, {storage_names, refs}) do
    {pid_name, refs} = Map.pop(refs, ref)
    storage_names = Map.delete(storage_names, pid_name)
    {:noreply, {storage_names, refs}}
  end

  def handle_info(msg, state) do
    require Logger
    Logger.debug("Registry Error #{inspect(msg)}. PandoraCache.Registry")
    {:noreply, state}
  end
end
