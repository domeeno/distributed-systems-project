defmodule CacheServerTest do
  use ExUnit.Case

  setup do
    Application.stop(:cache_service)
    :ok = Application.start(:cache_service)
  end

  setup do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = :gen_tcp.connect('localhost', 4040, opts)
    %{socket: socket}
  end

  test "server interaction", %{socket: socket} do
    assert send_and_recv(socket, "create shopping eggs 2\r\n") ==
             "OK\r\n"

    # GET returns two lines
    assert send_and_recv(socket, "get shopping eggs\r\n") == "2\r\n"
    IO.inspect(socket)
    assert send_and_recv(socket, "") == "OK\r\n"
  end

  defp send_and_recv(socket, command) do
    :ok = :gen_tcp.send(socket, command)
    {:ok, data} = :gen_tcp.recv(socket, 0, 1000)
    data
  end
end
