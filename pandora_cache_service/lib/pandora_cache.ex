defmodule PandoraCache do
  use Application

  @impl true
  def start(_type, _args) do
    PandoraCache.Supervisor.start_link(name: PandoraCache.Supervisor)
  end
end
