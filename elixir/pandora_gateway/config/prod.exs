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


config :logger,
  backends: [
    :console,
    {LogstashJson.TCP, :logstash}
  ]

config :logger, :logstash,
  level: :info,
  fields: %{application: "Gateway"},
  formatter: {AppLogger, :formatter},
  host: {:system, "LOGSTASH_TCP_HOST", "elk"},
  port: {:system, "LOGSTASH_TCP_PORT", "5055"},
  workers: 2,
  buffer_size: 10_000

