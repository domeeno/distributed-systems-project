defmodule AppLogger do
  require Logger

  def level_name_to_syslog_level(level_name, default_level \\ 6) do
     case level_name do
        :error -> 3
        :warn -> 4
        :info -> 6
        :debug -> 7
        level when is_integer(level) -> level
        _ -> default_level
     end
  end

  def formatter(event) do
    event
    |> Map.put(:level, level_name_to_syslog_level(event[:level]))
    |> Map.put(:beam_pid, event[:pid])
    |> Map.delete(:pid)
    |> Map.delete(:file)
    |> Map.delete(:line)
  end

  def try_to_log(message) do
    Logger.info(message)
  end
end

