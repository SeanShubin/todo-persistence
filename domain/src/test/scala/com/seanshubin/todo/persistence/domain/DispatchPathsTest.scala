package com.seanshubin.todo.persistence.domain

import org.scalatest.FunSuite

/*
 test-driven-013
 Put the actual dispatch paths behind a contract, so we can test it
 */
class DispatchPathsTest extends FunSuite {
  test("get task") {
    //given
    val dispatcher = createDispatcher()

    //when
    val response = dispatcher.handle(RequestValue("get", "/task"))

    //then
    assert(response === ResponseValue(200, "task response"))
  }

  test("post task event") {
    //given
    val dispatcher = createDispatcher()

    //when
    val response = dispatcher.handle(RequestValue("post", "/task-event"))

    //then
    assert(response === ResponseValue(200, "task event response"))
  }

  test("get health") {
    //given
    val dispatcher = createDispatcher()

    //when
    val response = dispatcher.handle(RequestValue("get", "/health"))

    //then
    assert(response === ResponseValue(200, "health check response"))
  }

  test("not found") {
    //given
    val dispatcher = createDispatcher()

    //when
    val response = dispatcher.handle(RequestValue("get", "/not-found"))

    //then
    assert(response === ResponseValue(404, "Not allowed to apply method get to subject not-found"))
  }

  def createDispatcher(): RequestValueHandler = {
    val healthCheckHandler: HealthCheckHandlerMarker = new HealthCheckHandlerMarker {
      override def handle(request: RequestValue): ResponseValue = ResponseValue(200, "health check response")
    }
    val taskHandler: TaskHandlerMarker = new TaskHandlerMarker {
      override def handle(request: RequestValue): ResponseValue = ResponseValue(200, "task response")
    }
    val taskEventHandler: TaskEventHandlerMarker = new TaskEventHandlerMarker {
      override def handle(request: RequestValue): ResponseValue = ResponseValue(200, "task event response")
    }
    val dispatcher: RequestValueHandler = new DispatchPaths(healthCheckHandler, taskHandler, taskEventHandler)
    dispatcher
  }
}
