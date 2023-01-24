defmodule Cache.Parser do
  @doc ~S"""
  Parses queries from gateway service.

  ## Example
    iex> Cache.Parser.parse("create|bucket|key|value|\r\n")
    {:ok, {:create, "bucket", "key", "value"}}


    iex> Cache.Parser.parse("put|bucket|key|valuer|\r\n")
    {:ok, {:put, "bucket", "key", "value"}}

    iex> Cache.Parser.parse("get|bucket|key|\r\n")
    {:ok, {:get, "bucket", "key"}}

    iex> Cache.Parser.parse("del|bucket|key|\r\n")
    {:ok, {:del, "bucket", "key", "value"}}

    iex> 
  """
  def parse(line) do
    case String.split(line, "|") do
      ["create", bucket, key, value, _endclause] -> {:ok, {:create, bucket, key, value}}
      ["put", bucket, key, value, _endclause] -> {:ok, {:put, bucket, key, value}}
      ["get", bucket, key, _endclause] -> {:ok, {:get, bucket, key}}
      ["del", bucket, key, _endclause] -> {:ok, {:del, bucket, key}}
      _ -> {:error, :bad_operation}
    end
  end

  @doc """
  Runs parsed operation
  """
  def run(operation)

  def run({:create, bucket, key, value}) do
    Cache.Registry.create(Cache.Registry, bucket)
    IO.puts("OK THIS WORKS!!!")

    lookup(bucket, fn pid ->
      Cache.Bucket.put(pid, key, value)
      {:ok, "OK\r\n"}
    end)
  end

  def run({:get, bucket, key}) do
    lookup(bucket, fn pid ->
      value = Cache.Bucket.get(pid, key)
      {:ok, "#{value}\r\nOK\r\n"}
    end)
  end

  def run({:put, bucket, key, value}) do
    lookup(bucket, fn pid ->
      Cache.Bucket.put(pid, key, value)
      {:ok, "OK\r\n"}
    end)
  end

  def run({:del, bucket, key}) do
    lookup(bucket, fn pid ->
      Cache.Bucket.delete(pid, key)
      {:ok, "OK\r\n"}
    end)
  end

  defp lookup(bucket, callback) do
    case Cache.Registry.lookup(Cache.Registry, bucket) do
      {:ok, pid} ->
        callback.(pid)

      :error ->
        {:error, :not_found}
    end
  end
end
