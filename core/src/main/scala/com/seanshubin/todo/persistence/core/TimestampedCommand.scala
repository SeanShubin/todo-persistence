package com.seanshubin.todo.persistence.core

import java.time.Instant

case class TimestampedCommand(timestamp: Instant, commandString: String)
