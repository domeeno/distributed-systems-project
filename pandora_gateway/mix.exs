defmodule Gateway.MixProject do
  use Mix.Project

  def project do
    [
      app: :pandora_gateway,
      version: "0.1.0",
      elixir: "~> 1.12",
      start_permanent: Mix.env() == :dev,
      deps: deps()
    ]
  end

  # Run "mix help compile.app" to learn about applications.
  def application do
    [
      mod: {Gateway, []},
      extra_applications: [:logger]
    ]
  end

  # Run "mix help deps" to learn about dependencies.
  defp deps do
    [
      {:cowboy, "~> 2.4"},
      {:plug, "~> 1.7"},
      {:plug_cowboy, "~> 2.0"},
      {:httpoison, "~> 1.8"},
      {:poison, "~> 5.0"}
    ]
  end
end
