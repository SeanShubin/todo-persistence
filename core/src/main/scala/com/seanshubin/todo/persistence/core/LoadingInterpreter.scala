package com.seanshubin.todo.persistence.core

class LoadingInterpreter(delegate: StatefulInterpreterMarker) extends LoadingInterpreterMarker {
  override def tasks: Tasks = {
    delegate.tasks
  }

  override def execute(line: String): String = {
    val timestampedCommand = TimestampedCommandParser.parse(line)
    delegate.execute(timestampedCommand.commandString)
  }
}
