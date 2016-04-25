package com.seanshubin.todo.persistence.core

trait Interpreter {
  def tasks: Tasks

  def execute(line: String): String
}
