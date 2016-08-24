package com.seanshubin.todo.persistence.domain

trait PreLoader {
  def loadInitialState(): Unit
}
