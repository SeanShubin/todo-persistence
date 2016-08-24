package com.seanshubin.todo.persistence.domain

class JavaMonitorCriticalSection(/*monitor: AnyRef*/) extends CriticalSection {
  override def doWithCriticalSection[T](f: => T): T = {
//    monitor.synchronized {
//      f
//    }
    ???
  }
}
