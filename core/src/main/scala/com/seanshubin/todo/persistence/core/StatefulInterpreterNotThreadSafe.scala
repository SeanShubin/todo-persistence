package com.seanshubin.todo.persistence.core

class StatefulInterpreterNotThreadSafe(initialTasks: Tasks) extends StatefulInterpreterMarker {
  private var mutableTasks: Tasks = initialTasks

  override def tasks: Tasks = {
    mutableTasks
  }

  override def execute(line: String): String = {
    val command = CommandParser.parse(line)
    val (newTasks, message) = command.apply(mutableTasks)
    mutableTasks = newTasks
    message
  }
}
