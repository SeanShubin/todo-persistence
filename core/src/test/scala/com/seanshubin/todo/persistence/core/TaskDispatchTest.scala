package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

/*
 test-driven-009
 Start the test with the high level expectations, without working about implementation details
 Especially don't create a connection with javas servlet API, we want our design to be platform neutral
 This allows us to focus on making our code easy to test
 It also makes our code more generic
 */
class TaskDispatchTest extends FunSuite {
  test("unknown request") {
    //given
    val dispatcher = createDispatcher()
    val request = RequestValue(method = "foo", subject = "bar", body = "baz")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === ResponseValue(405, "Not allowed to apply method foo to subject bar"))
  }

  test("add") {
    //given
    val dispatcher = createDispatcher()
    val request = RequestValue("POST", "/task-event", "add Task A")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === ResponseValue(201, "1 false Task A"))
  }

  test("add several") {
    //given
    val dispatcher = createDispatcher()
    //when
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task A"))
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task B"))
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task C"))
    val response = dispatcher.handle(RequestValue("GET", "/task"))
    //then
    assert(response === ResponseValue(200, "1 false Task A\n2 false Task B\n3 false Task C"))
  }

  test("done") {
    //given
    val dispatcher = createDispatcher()
    //when
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task A"))
    dispatcher.handle(RequestValue("POST", "/task-event", "done 1"))
    val response = dispatcher.handle(RequestValue("GET", "/task"))
    //then
    assert(response === ResponseValue(200, "1 true Task A"))
  }

  test("undone") {
    //given
    val dispatcher = createDispatcher()
    //when
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task A"))
    dispatcher.handle(RequestValue("POST", "/task-event", "done 1"))
    dispatcher.handle(RequestValue("POST", "/task-event", "undone 1"))
    val response = dispatcher.handle(RequestValue("GET", "/task"))
    //then
    assert(response === ResponseValue(200, "1 false Task A"))
  }

  test("clear done") {
    //given
    val dispatcher = createDispatcher()
    //when
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task A"))
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task B"))
    dispatcher.handle(RequestValue("POST", "/task-event", "add Task C"))
    dispatcher.handle(RequestValue("POST", "/task-event", "done 2"))
    dispatcher.handle(RequestValue("POST", "/task-event", "clear"))
    val response = dispatcher.handle(RequestValue("GET", "/task"))
    //then
    assert(response === ResponseValue(200, "1 false Task A\n3 false Task C"))
  }

  def createDispatcher(): Dispatcher = {
    val tasks = Tasks.Empty
    val dummyHealth = null
    val interpreter = new StatefulInterpreter(tasks)
    val dispatcher = new Dispatcher(interpreter, dummyHealth)
    dispatcher
  }
}
