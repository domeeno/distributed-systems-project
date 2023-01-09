defmodule Cache.RegistryTest do
  use ExUnit.Case, async: true

  setup do
    registry = start_supervised!(Cache.Registry)
    %{registry: registry}
  end

  test "spawns buckets", %{registry: registry} do
    assert Cache.Registry.lookup(registry, "programming") == :error

    Cache.Registry.create(registry, "programming")
    assert {:ok, bucket} = Cache.Registry.lookup(registry, "programming")

    Cache.Bucket.put(bucket, "science", "computer")
    assert Cache.Bucket.get(bucket, "science") == "computer"
  end

  test "removes bucket on exit", %{registry: registry} do
    Cache.Registry.create(registry, "programming")
    {:ok, bucket} = Cache.Registry.lookup(registry, "programming")
    Agent.stop(bucket)
    assert Cache.Registry.lookup(registry, "programming") == :error
  end

  test "removes bucket on crash", %{registry: registry} do
    Cache.Registry.create(registry, "programming")
    {:ok, bucket} = Cache.Registry.lookup(registry, "programming")

    Agent.stop(bucket, :shutdown)
    assert Cache.Registry.lookup(registry, "programming") == :error
  end
end
