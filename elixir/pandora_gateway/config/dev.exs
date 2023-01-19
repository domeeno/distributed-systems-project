import Config

config :pandora_gateway,
  services: [
    %{
      service: "userservice",
      ports: [
        "8080",
        "8085",
        "8086"
      ],
      address: "http://127.0.0.1"
    },
    %{
      service: "subject",
      ports: [
        "8082",
        "8090",
        "8091"
      ],
      address: "http://127.0.0.1"
    },
    %{
      service: "bucket",
      ports: ["8083"],
      address: "http://127.0.0.1"
    }
  ],
  app: %{
    port: 4000,
    cache: %{
      port: 4040
    }
  }
