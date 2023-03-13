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
      address: "cache",
      port: 4040
    }
  }
