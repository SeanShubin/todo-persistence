package com.seanshubin.todo.persistence.domain

import java.time.Instant

object TimestampedCommandFormatter {
  def format(timestamp: Instant, command: Command): String = {
    val commandString = CommandFormatter.format(command)
    s"$timestamp $commandString"
  }
}
