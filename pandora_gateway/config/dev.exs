import Config

config :pandora_gateway,
  services: %{
    user_service: %{
        port: "8080",
        address: "127.0.0.1"
    },
    course_service: %{
        port: "8082",
        address: "127.0.0.1"
    },
    bucket_service: %{
        port: "8083",
        address: "127.0.0.1"
    },
  }
