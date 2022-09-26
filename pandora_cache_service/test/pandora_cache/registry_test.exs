defmodule PandoraCache.RegistryTest do
  use ExUnit.Case, async: true

  setup do
    registry = start_supervised!(PandoraCache.Registry)
    %{registry: registry}
  end

  test "lookup non-existent registry error", %{registry: registry} do
    assert PandoraCache.Registry.lookup(registry, "topic_subject") == :error
  end

  test "create registry success", %{registry: registry} do
    assert PandoraCache.Registry.create(registry, "topic_subject") == :ok
    {:ok, str} = PandoraCache.Registry.lookup(registry, "topic_subject")
    PandoraCache.Storage.put(str, "cpp", ["cppadv"])
    assert PandoraCache.Storage.get(str, "cpp") == ["cppadv"]
  end

  test "remove registry on storage agent stop", %{registry: registry} do
    PandoraCache.Registry.create(registry, "topic_subject")
    {:ok, storage} = PandoraCache.Registry.lookup(registry, "topic_subject")
    Agent.stop(storage)
    assert PandoraCache.Registry.lookup(registry, "topic_subject") == :error
  end

  test "delete storage on failure", %{registry: registry} do
    PandoraCache.Registry.create(registry, "topic_subject")
    {:ok, storage} = PandoraCache.Registry.lookup(registry, "topic_subject")

    Agent.stop(storage, :shutdown)
    assert PandoraCache.Registry.lookup(registry, "topic_subject") == :error
  end
end
