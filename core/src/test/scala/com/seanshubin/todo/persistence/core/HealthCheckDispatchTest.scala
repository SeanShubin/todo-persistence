package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

class HealthCheckDispatchTest extends FunSuite {
  test("health check") {
    //given
    val healthResponse = ResponseValue(200, "OK")
    val health = new StubHealth(healthResponse)
    val dummyInterpreter = null
    val dispatcher = new Dispatcher(dummyInterpreter, health)

    //when
    val response = dispatcher.handle(RequestValue("GET", "/health"))

    //then
    assert(response === healthResponse)
  }

  class StubHealth(response: ResponseValue) extends Health {
    override def check(): ResponseValue = response
  }

}
