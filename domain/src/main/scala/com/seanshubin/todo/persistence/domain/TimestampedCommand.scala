package com.seanshubin.todo.persistence.domain

import java.time.Instant

case class TimestampedCommand(timestamp: Instant, commandString: String)
