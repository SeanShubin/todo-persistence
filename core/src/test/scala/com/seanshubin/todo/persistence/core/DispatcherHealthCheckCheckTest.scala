package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

class DispatcherHealthCheckCheckTest extends FunSuite {
  test("health check") {
    //given
    val healthResponse = ResponseValue(200, "OK")
    val health = new StubHealthCheck(healthResponse)
    val dummyInterpreter = null
    val dispatcher = new Dispatcher(dummyInterpreter, health)

    //when
    val response = dispatcher.handle(RequestValue("GET", "/health"))

    //then
    assert(response === healthResponse)
  }

  class StubHealthCheck(response: ResponseValue) extends HealthCheck {
    override def check(): ResponseValue = response
  }

}
