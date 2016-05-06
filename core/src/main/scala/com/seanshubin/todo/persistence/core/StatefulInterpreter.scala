package com.seanshubin.todo.persistence.core

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

class StatefulInterpreter(initialTasks: Tasks) extends Interpreter {
  private val atomicTasks: AtomicReference[Tasks] = new AtomicReference[Tasks](initialTasks)

  override def tasks: Tasks = atomicTasks.get()

  override def execute(line: String): String = {
    val command = CommandParser.parse(line)
    executeCommandInSerial(command)
  }

  @tailrec
  private def executeCommandInSerial(command: Command): String = {
    val oldTasks = atomicTasks.get()
    val (newTasks, message) = command.apply(oldTasks)
    if (atomicTasks.compareAndSet(oldTasks, newTasks)) {
      message
    } else {
      executeCommandInSerial(command)
    }
  }
}
