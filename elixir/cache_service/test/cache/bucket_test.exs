defmodule Cache.BucketTest do
  use ExUnit.Case, async: true

  setup do
    bucket = start_supervised!(Cache.Bucket)
    %{bucket: bucket}
  end

  test "stores values by key", %{bucket: bucket} do
    assert Cache.Bucket.get(bucket, "test_subject") == nil

    Cache.Bucket.put(bucket, "test_subject", "json_subject_tree")
    assert Cache.Bucket.get(bucket, "test_subject") == "json_subject_tree"
  end

  test "delete value from bucket", %{bucket: bucket} do
    Cache.Bucket.put(bucket, "test_subject", "json_subject_tree")
    assert Cache.Bucket.delete(bucket, "test_subject") == "json_subject_tree"
    assert Cache.Bucket.get(bucket, "test_subject") == nil
  end

  test "ensure temproary worker" do
    assert Supervisor.child_spec(Cache.Bucket, []).restart == :temporary
  end
end
