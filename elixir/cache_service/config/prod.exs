import Config

config :cache_service,
  app: %{
    port: 4040
  },
  caching: %{
    allowed_overhead: 150,
    optimal_nr_entries: 300,
    interval: 60 * 5 * 1000
  }

config :logger,
  backends: [
    :console,
    {LogstashJson.TCP, :logstash}
  ]

config :logger, :logstash,
  level: :info,
  fields: %{application: "Cache"},
  formatter: {AppLogger, :formatter},
  host: {:system, "LOGSTASH_TCP_HOST", "elk"},
  port: {:system, "LOGSTASH_TCP_PORT", "5055"},
  workers: 2,
  buffer_size: 10_000

