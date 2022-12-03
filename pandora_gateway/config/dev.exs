import Config

config :pandora_gateway,
  services: [
    %{
      service: "userservice",
      ports: [
        "8085",
        "8086",
        "8087"
      ],
      address: "http://127.0.0.1"
    },
    %{
      service: "subject",
      ports: [
        "8090",
        "8091",
        "8092"
      ],
      address: "http://127.0.0.1"
    },
    %{
      service: "bucket",
      ports: ["8083"],
      address: "http://127.0.0.1"
    }
  ]
