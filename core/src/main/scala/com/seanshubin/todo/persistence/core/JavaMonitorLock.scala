package com.seanshubin.todo.persistence.core

class JavaMonitorLock(monitor: AnyRef) extends Lock {
  override def doWithLock[T](f: => T): T = {
    monitor.synchronized {
      f
    }
  }
}
