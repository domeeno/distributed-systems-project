defmodule CacheServerTest do
  use ExUnit.Case

  setup do
    Application.stop(:cache_service)
    :ok = Application.start(:cache_service)
  end

  setup do
    opts = [:binary, packet: :line, active: false]
    {:ok, socket} = :gen_tcp.connect('localhost', 4045, opts)
    %{socket: socket}
  end

  test "server interaction", %{socket: socket} do
    uuid = "a396d27d-ee66-41a1-be81-e7620be3e587"
    
    json = "{ \"subject\": { \"id\": \"95aa62ad-933a-4d6d-9d3d-0d03b51262b0\", \"subjectName\": \"Distributed Systems\" } }"

    assert send_and_recv(socket, "0|0|create|subject|\r\n") ==
             "OK|CREATE\r\n"

    assert send_and_recv(socket, "0|0|put|subject|#{uuid}|#{json}|\r\n") ==
             "OK|PUT|\r\n"

    # GET returns two lines
    assert send_and_recv(socket, "0|0|get|subject|#{uuid}|\r\n") == "OK|#{json}|\r\n"
    assert send_and_recv(socket, "") == "OK\r\n"
  end

  defp send_and_recv(socket, command) do
    :ok = :gen_tcp.send(socket, command)
    {:ok, data} = :gen_tcp.recv(socket, 0, 1000)
    data
  end
end
