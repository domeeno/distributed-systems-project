defmodule Cache.MixProject do
  use Mix.Project

  def project do
    [
      app: :cache_service,
      version: "0.1.0",
      elixir: "~> 1.12",
      start_permanent: Mix.env() == :prod,
      deps: deps()
    ]
  end

  # Run "mix help compile.app" to learn about applications.
  def application do
    [
      extra_applications: [:logger, :logstash_json],
      mod: {Cache, []}
    ]
  end

  # Run "mix help deps" to learn about dependencies.
  defp deps do
    [
      {:logstash_json, "~> 0.7"}
    ]
  end
end
