import Config

config :pandora_gateway,
  services: [
    %{
      service: "user",
      ports: [
        "8085",
        "8086",
        "8087"
      ],
      address: "127.0.0.1"
    },
    %{
      service: "subject",
      ports: [
        "8090",
        "8091",
        "8092"
      ],
      address: "127.0.0.1"
    },
    %{
      service: "bucket",
      ports: ["8083"],
      address: "127.0.0.1"
    }
  ]
