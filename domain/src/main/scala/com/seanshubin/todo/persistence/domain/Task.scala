package com.seanshubin.todo.persistence.domain

case class Task(id: Int, name: String, done: Boolean = false)

object Task {
  val SortById: (Task, Task) => Boolean = (left, right) => {
    left.id < right.id
  }
}
