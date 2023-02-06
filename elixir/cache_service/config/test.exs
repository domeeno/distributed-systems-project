import Config

config :cache_service,
  app: %{
    port: 4045
  },
  caching: %{
    allowed_overhead: 2,
    optimal_nr_entries: 5,
    interval: 60 * 1000
  }
