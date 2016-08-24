package com.seanshubin.todo.persistence.domain

trait Interpreter {
  def tasks: Tasks

  def execute(line: String): String
}
