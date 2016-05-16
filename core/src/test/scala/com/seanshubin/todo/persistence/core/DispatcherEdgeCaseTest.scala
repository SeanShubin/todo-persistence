package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

class DispatcherEdgeCaseTest extends FunSuite {
  test("unknown request") {
    //given
    val dispatcher = createDispatcher()
    val request = RequestValue(method = "foo", subject = "bar", body = "baz")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === ResponseValue(405, "Not allowed to apply method foo to subject bar"))
  }

  test("handle exception") {
    //given
    val dispatcher = createDispatcher()
    //when
    val response = dispatcher.handle(RequestValue("GET", "/task"))
    //then
    assert(response === ResponseValue(500, "Exception with message 'null' when trying to apply method GET to subject /task"))
  }

  def createDispatcher(): Dispatcher = {
    val dummyHealth = null
    val dummyInterpreter = null
    val dispatcher = new Dispatcher(dummyInterpreter, dummyHealth)
    dispatcher
  }
}
