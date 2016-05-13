package com.seanshubin.todo.persistence.core

import com.seanshubin.todo.persistence.core.Command.{AddCommand, ClearCommand, DoneCommand, UndoneCommand}

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
