package com.seanshubin.todo.persistence.core

trait CriticalSection {
  def doWithCriticalSection[T](f: => T): T
}
