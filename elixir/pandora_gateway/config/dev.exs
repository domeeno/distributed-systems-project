import Config

config :pandora_gateway,
  services: [
    %{
      service: "userservice"
    },
    %{
      service: "subject"
    },
    %{
      service: "bucket"
    }
  ],
  app: %{
    port: 4000,
    cache: %{
      address: "127.0.0.1",
      port: 4040
    }
  }
