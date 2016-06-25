package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

/*
 test-driven-021
 There are some limitations to what you can prove in a demo
 For example, if the critical section mechanism is not working, you won't be able to tell by demoing it
 */
class JavaMonitorCriticalSectionTest extends FunSuite {
  test("java monitor critical section") {
    //given
    val monitor = new AnyRef
    val criticalSection = new JavaMonitorCriticalSection(monitor)

    //when
    val result = criticalSection.doWithCriticalSection {
      2 + 2
    }

    //then
    assert(result === 4)
  }
}
