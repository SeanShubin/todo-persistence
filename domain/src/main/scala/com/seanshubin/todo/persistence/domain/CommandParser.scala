package com.seanshubin.todo.persistence.domain

object CommandParser {
  private val AddRegex = """add (.*)""".r
  private val DoneRegex = """done (\d+)""".r
  private val UndoneRegex = """undone (\d+)""".r
  private val ClearRegex = """clear""".r

  def parse(line: String): Command = {
    line match {
      case AddRegex(name) => Command.AddCommand(name)
      case DoneRegex(idString) => Command.DoneCommand(idString.toInt)
      case UndoneRegex(idString) => Command.UndoneCommand(idString.toInt)
      case ClearRegex() => Command.ClearCommand
    }
  }
}
