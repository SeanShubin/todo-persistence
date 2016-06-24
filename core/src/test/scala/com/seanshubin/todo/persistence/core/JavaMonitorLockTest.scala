package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

/*
 test-driven-021
 There are some limitations to what you can prove in a demo
 For example, if the lock mechanism is not working, you won't be able to tell by demoing it
 */
class JavaMonitorLockTest extends FunSuite {
  test("java monitor lock") {
    //given
    val monitor = new AnyRef
    val lock = new JavaMonitorLock(monitor)

    //when
    val result = lock.doWithLock {
      2 + 2
    }

    //then
    assert(result === 4)
  }
}
