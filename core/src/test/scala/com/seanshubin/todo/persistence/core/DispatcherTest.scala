package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

/*
 test-driven-013
 Dispatch logic can be generic
 */
class DispatcherTest extends FunSuite {
  test("unknown request") {
    //given
    val handlersBySubject = Map[String, RequestValueHandler]()
    val dispatcher = createDispatcher(handlersBySubject)
    val request = RequestValue(method = "foo", path = "/bar/path", body = "baz")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === ResponseValue(404, "Not allowed to apply method foo to subject bar"))
  }

  test("handle exception") {
    //given
    val exception = new RuntimeException("the exception")
    val expectedExceptionString = ExceptionUtil.toString(exception)
    val handlerThatThrows = new HandlerThatThrows(exception)
    val handlersBySubject = Map("fragile" -> handlerThatThrows)
    val dispatcher = createDispatcher(handlersBySubject)
    //when
    val response = dispatcher.handle(RequestValue("GET", "/fragile/path"))
    //then
    assert(response === ResponseValue(500, "When trying to apply method GET to subject fragile, got the following exception:\n" + expectedExceptionString))
  }

  test("typical") {
    //given
    val stubResponse = ResponseValue(200, "stub")
    val stubHandler = new StubHandler(stubResponse)
    val handlersBySubject = Map("stub" -> stubHandler)
    val dispatcher = createDispatcher(handlersBySubject)
    val request = RequestValue("GET", "/stub/path")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === stubResponse)

  }

  def createDispatcher(handlersBySubject: Map[String, RequestValueHandler]): Dispatcher = {
    val dispatcher = new Dispatcher(handlersBySubject)
    dispatcher
  }

  class StubHandler(response: ResponseValue) extends RequestValueHandler {
    val requestsHandled = new ArrayBuffer[RequestValue]

    override def handle(request: RequestValue): ResponseValue = {
      requestsHandled.append(request)
      response
    }
  }

  class HandlerThatThrows(exception: Exception) extends RequestValueHandler {
    override def handle(request: RequestValue): ResponseValue = {
      throw exception
    }
  }

}
