package com.seanshubin.todo.persistence.core

case class Tasks(lastId: Int, tasks: Map[Int,Task]) {
  def add(name: String): (Tasks, Task) = {
    val nextId = lastId + 1
    val newTask = Task(nextId, name)
    val newTasks = tasks.updated(nextId, newTask)
    (Tasks(nextId, newTasks), newTask)
  }

  def done(id: Int, newValueForDone: Boolean): Tasks = ???

  def clearDone(): Tasks = ???
}

object Tasks {
  val Empty = Tasks(0, Map())
}
