package com.seanshubin.todo.persistence.core

import java.time.Instant

object TimestampedCommandFormatter {
  def format(timestamp: Instant, command: Command): String = {
    val commandString = CommandFormatter.format(command)
    s"$timestamp $commandString"
  }
}
