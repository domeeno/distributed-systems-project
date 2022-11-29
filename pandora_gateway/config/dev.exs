import Config

config :pandora_gateway,
  services: %{
    user_service: %{
        ports: [
          "8085",
          "8086",
          "8087"
        ],
        address: "127.0.0.1"
    },
    course_service: %{
        ports: [
          "8090",
          "8091",
          "8092",
        ],
        address: "127.0.0.1"
    },
    bucket_service: %{
        port: "8083",
        address: "127.0.0.1"
    },
  }
