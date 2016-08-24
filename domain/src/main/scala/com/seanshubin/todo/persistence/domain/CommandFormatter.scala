package com.seanshubin.todo.persistence.domain

import com.seanshubin.todo.persistence.domain.Command.{AddCommand, ClearCommand, DoneCommand, UndoneCommand}

object CommandFormatter {
  def format(command: Command): String = {
    command match {
      case AddCommand(name) => s"add $name"
      case DoneCommand(id) => s"done $id"
      case UndoneCommand(id) => s"undone $id"
      case ClearCommand => "clear"
    }
  }
}
