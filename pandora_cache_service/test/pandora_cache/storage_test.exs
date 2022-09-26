defmodule PandoraCache.StorageTest do
  use ExUnit.Case, async: true

  setup do
    storage = start_supervised!(PandoraCache.Storage)
    %{storage: storage}
  end

  test "store value by key", %{storage: storage} do
    assert PandoraCache.Storage.get(storage, "subject_test") == nil

    PandoraCache.Storage.put(storage, "subject_test", ["cpp", "python", "elixir"])
    assert PandoraCache.Storage.get(storage, "subject_test") == ["cpp", "python", "elixir"]
  end

  test "delete value by key", %{storage: storage} do
    assert PandoraCache.Storage.get(storage, "subject_test") == nil

    PandoraCache.Storage.put(storage, "subject_test", ["cpp", "python", "elixir"])
    assert PandoraCache.Storage.get(storage, "subject_test") == ["cpp", "python", "elixir"]
    PandoraCache.Storage.delete(storage, "subject_test")

    assert PandoraCache.Storage.get(storage, "subject_test") == nil
  end
end
