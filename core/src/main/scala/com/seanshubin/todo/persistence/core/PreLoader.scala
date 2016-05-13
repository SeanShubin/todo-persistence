package com.seanshubin.todo.persistence.core

trait PreLoader {
  def loadInitialState(): Unit
}
