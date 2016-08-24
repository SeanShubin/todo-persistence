package com.seanshubin.todo.persistence.domain

import java.time.Instant

object TimestampedCommandParser {
  private val TimestampedRegex = """([\w-:.]+)\s+(.*)""".r

  def parse(line: String): TimestampedCommand = {
    line match {
      case TimestampedRegex(timestampString, commandString) =>
        val timestamp = Instant.parse(timestampString)
        TimestampedCommand(timestamp, commandString)
    }
  }
}
