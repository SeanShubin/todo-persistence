package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

class DispatcherTest extends FunSuite {
  test("unknown request") {
    //given
    val dispatcher = createDispatcher()
    val request = Request("foo", "bar", "baz")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === Response(405, "Not allowed to apply method foo to subject bar"))
  }

  test("add") {
    //given
    val dispatcher = createDispatcher()
    val request = Request("POST", "task-event", "add Task A")
    //when
    val response = dispatcher.handle(request)
    //then
    assert(response === Response(201, "1 false Task A"))
  }

  def createDispatcher(): Dispatcher = {
    val tasks = Tasks.Empty
    val interpreter = new StatefulInterpreter(tasks)
    val dispatcher = new Dispatcher(interpreter)
    dispatcher
  }
}

/*
POST task-event add Task A
  201
  1 false Task A
POST task-event add Task B
  201
  1 false Task B
POST task-event add Task C
  201
  1 false Task C
GET task
  200
  1 false Task A
  1 false Task B
  1 false Task C
POST task-event done 2
  201
POST task-event undone 2
  201
POST task-event clear
  201
*/
