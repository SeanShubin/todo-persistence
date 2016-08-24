package com.seanshubin.todo.persistence.domain

trait CriticalSection {
  def doWithCriticalSection[T](f: => T): T
}
