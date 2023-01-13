# Gateway

A gateway is an entry point to a microservices system that manages all or the majority of the outbound calls to other services of the app.
A gateway should be able to (but not limited):
- Balance the load - redirect requests to replica in a fashion that it can distribute the load evenly between them,
- Break the circuit - have a B plan in case a requests takes too long,
- Notice repeated failure of a service call and stop sending request to a malfunctioning replica.


## Installation

If [available in Hex](https://hex.pm/docs/publish), the package can be installed
by adding `pandora_gateway` to your list of dependencies in `mix.exs`:

```elixir
def deps do
  [
    {:pandora_gateway, "~> 0.1.0"}
  ]
end
```

Documentation can be generated with [ExDoc](https://github.com/elixir-lang/ex_doc)
and published on [HexDocs](https://hexdocs.pm). Once published, the docs can
be found at [https://hexdocs.pm/pandora_gateway](https://hexdocs.pm/pandora_gateway).

