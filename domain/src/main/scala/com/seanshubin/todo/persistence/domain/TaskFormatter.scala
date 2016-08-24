package com.seanshubin.todo.persistence.domain

object TaskFormatter {
  def tasksToString(tasks: Tasks): String = {
    tasks.asOrderedSequence().map(taskToString).mkString("\n")

  }

  def taskToString(task: Task): String = {
    val Task(id, name, done) = task
    val asString = s"$id $done $name"
    asString
  }
}
