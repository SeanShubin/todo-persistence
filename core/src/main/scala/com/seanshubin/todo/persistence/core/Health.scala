package com.seanshubin.todo.persistence.core

trait Health {
  def check(): ResponseValue
}
