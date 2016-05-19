package com.seanshubin.todo.persistence.core

trait Lock {
  def doWithLock[T](f: => T): T
}
