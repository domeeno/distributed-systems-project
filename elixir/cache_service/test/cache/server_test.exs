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
    json = """
    {
    "subject": {
        "id": "95aa62ad-933a-4d6d-9d3d-0d03b51262b0",
        "subjectName": "Distributed Systems",
        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
        "description": "Introduction to Distributed Systems principles",
        "rootTopic": "eae9e487-57e4-4b00-a658-76fa042d9d41",
        "tags": [
            "Distributed",
            "Systems",
            "Architecture"
        ],
        "likes": 0,
        "saves": 0,
        "createTimestamp": "2022-12-19T23:08:15.364",
        "updateTimestamp": "2022-12-19T23:08:15.364"
    },
    "tree": {
        "id": "eae9e487-57e4-4b00-a658-76fa042d9d41",
        "parentId": null,
        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
        "topicName": "Distributed Systems",
        "childIds": [
            "2d186eb0-1179-4c92-b281-abb085a63c45",
            "a5418c92-2f1b-4dcd-a9b0-76fd6c2dbbbb"
        ],
        "documentId": null,
        "createTimestamp": "2022-12-19T23:08:15.2",
        "updateTimestamp": "2022-12-19T23:08:15.201",
        "allTopics": [],
        "childTopics": [
            {
                "id": "a5418c92-2f1b-4dcd-a9b0-76fd6c2dbbbb",
                "parentId": "eae9e487-57e4-4b00-a658-76fa042d9d41",
                "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                "topicName": "Bingo",
                "childIds": [],
                "documentId": null,
                "createTimestamp": "2022-12-19T23:38:19.722",
                "updateTimestamp": "2022-12-19T23:38:19.722",
                "allTopics": [],
                "childTopics": []
            },
            {
                "id": "2d186eb0-1179-4c92-b281-abb085a63c45",
                "parentId": "eae9e487-57e4-4b00-a658-76fa042d9d41",
                "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                "topicName": "Ok",
                "childIds": [
                    "393e593b-b867-47be-af98-7d298993f2fa"
                ],
                "documentId": null,
                "createTimestamp": "2022-12-19T23:38:24.772",
                "updateTimestamp": "2022-12-19T23:38:24.772",
                "allTopics": [],
                "childTopics": [
                    {
                        "id": "393e593b-b867-47be-af98-7d298993f2fa",
                        "parentId": "2d186eb0-1179-4c92-b281-abb085a63c45",
                        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                        "topicName": "Okk",
                        "childIds": [
                            "280270b9-bad9-43e5-90df-236506318e9b",
                            "36b46276-72e9-4174-b469-430f9985ce26"
                        ],
                        "documentId": null,
                        "createTimestamp": "2022-12-20T00:07:37.447",
                        "updateTimestamp": "2022-12-20T00:07:37.447",
                        "allTopics": [],
                        "childTopics": [
                            {
                                "id": "280270b9-bad9-43e5-90df-236506318e9b",
                                "parentId": "393e593b-b867-47be-af98-7d298993f2fa",
                                "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                                "topicName": "Dok",
                                "childIds": [],
                                "documentId": null,
                                "createTimestamp": "2022-12-20T01:23:50.925",
                                "updateTimestamp": "2022-12-20T01:23:50.925",
                                "allTopics": [],
                                "childTopics": []
                            },
                            {
                                "id": "36b46276-72e9-4174-b469-430f9985ce26",
                                "parentId": "393e593b-b867-47be-af98-7d298993f2fa",
                                "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                                "topicName": "Nok",
                                "childIds": [
                                    "73ca47d5-cade-477e-b055-4bfcecb5fddf",
                                    "42d9371c-f960-4473-9ba5-2637a482c91f",
                                    "9c49d3e5-20f0-4cbd-b1dd-25e31e45aae5"
                                ],
                                "documentId": null,
                                "createTimestamp": "2022-12-20T01:23:46.499",
                                "updateTimestamp": "2022-12-20T01:23:46.499",
                                "allTopics": [],
                                "childTopics": [
                                    {
                                        "id": "73ca47d5-cade-477e-b055-4bfcecb5fddf",
                                        "parentId": "36b46276-72e9-4174-b469-430f9985ce26",
                                        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                                        "topicName": "K",
                                        "childIds": [],
                                        "documentId": null,
                                        "createTimestamp": "2022-12-20T23:25:30.719",
                                        "updateTimestamp": "2022-12-20T23:25:30.719",
                                        "allTopics": [],
                                        "childTopics": []
                                    },
                                    {
                                        "id": "42d9371c-f960-4473-9ba5-2637a482c91f",
                                        "parentId": "36b46276-72e9-4174-b469-430f9985ce26",
                                        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                                        "topicName": "O",
                                        "childIds": [],
                                        "documentId": null,
                                        "createTimestamp": "2022-12-20T23:25:26.34",
                                        "updateTimestamp": "2022-12-20T23:25:26.34",
                                        "allTopics": [],
                                        "childTopics": []
                                    },
                                    {
                                        "id": "9c49d3e5-20f0-4cbd-b1dd-25e31e45aae5",
                                        "parentId": "36b46276-72e9-4174-b469-430f9985ce26",
                                        "userId": "845c01ca-35ca-4ab9-a381-2a80cc531d52",
                                        "topicName": "N",
                                        "childIds": [],
                                        "documentId": null,
                                        "createTimestamp": "2022-12-20T23:25:23.309",
                                        "updateTimestamp": "2022-12-20T23:25:23.309",
                                        "allTopics": [],
                                        "childTopics": []
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    }
    }
    """

    assert send_and_recv(socket, "create|subject|#uuid|#json|\r\n") ==
             "OK\r\n"

    # GET returns two lines
    assert send_and_recv(socket, "get|subject|#uuid|\r\n") == "#json\r\n"
    IO.inspect(socket)
    assert send_and_recv(socket, "") == "OK\r\n"
  end

  defp send_and_recv(socket, command) do
    :ok = :gen_tcp.send(socket, command)
    {:ok, data} = :gen_tcp.recv(socket, 0, 1000)
    data
  end
end
