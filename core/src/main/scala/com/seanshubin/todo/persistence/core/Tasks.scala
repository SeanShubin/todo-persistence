package com.seanshubin.todo.persistence.core

case class Tasks(lastId: Int, tasks: Map[Int,Task]) {
  def add(name: String): (Tasks, Task) = {
    val nextId = lastId + 1
    val newTask = Task(nextId, name)
    val newTasks = tasks.updated(nextId, newTask)
    (Tasks(nextId, newTasks), newTask)
  }

  def done(id: Int, newValueForDone: Boolean): Tasks = {
    val oldTask = tasks(id)
    val newTask = oldTask.copy(done = newValueForDone)
    val newTasks = tasks.updated(id, newTask)
    copy(tasks = newTasks)
  }

  def clearDone(): Tasks = {
    val tasksNotDone = for {
      (id, task) <- tasks
      if !task.done
    } yield (id, task)
    copy(tasks = tasksNotDone)
  }

  def asOrderedSequence(): Seq[Task] = {
    tasks.values.toSeq.sortWith(Task.SortById)
  }
}

object Tasks {
  val Empty = Tasks(0, Map())
}
