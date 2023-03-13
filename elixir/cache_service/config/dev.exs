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
