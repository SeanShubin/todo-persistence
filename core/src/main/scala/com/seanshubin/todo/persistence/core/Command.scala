package com.seanshubin.todo.persistence.core

sealed trait Command {
  def apply(tasks: Tasks): (Tasks, String)
}

object Command {

  case class AddCommand(name: String) extends Command {
    override def apply(tasks: Tasks): (Tasks, String) = {
      val (newTasks, task) = tasks.add(name)
      val message = TaskFormatter.taskToString(task)
      (newTasks, message)
    }
  }

  case class DoneCommand(id: Int) extends Command {
    override def apply(tasks: Tasks): (Tasks, String) = {
      (tasks.done(id, newValueForDone = true), "")
    }
  }

  case class UndoneCommand(id: Int) extends Command {
    override def apply(tasks: Tasks): (Tasks, String) = {
      (tasks.done(id, newValueForDone = false), "")
    }
  }

  case object ClearCommand extends Command {
    override def apply(tasks: Tasks): (Tasks, String) = {
      (tasks.clearDone(), "")
    }
  }

}
