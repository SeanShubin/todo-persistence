package com.seanshubin.todo.persistence.domain

import org.scalatest.FunSuite

/*
 test-driven-017
 Test the business logic, isolated from any knowledge of how it is served
 */
class StatefulInterpreterTest extends FunSuite {
  test("add") {
    //given
    val interpreter = createInterpreter()
    val command = "add Task A"

    //when
    val response = interpreter.execute(command)

    //then
    assert(response === "1 false Task A")
  }

  test("add several") {
    //given
    val interpreter = createInterpreter()

    //when
    interpreter.execute("add Task A")
    interpreter.execute("add Task B")
    interpreter.execute("add Task C")

    //then
    assert(interpreter.tasks === Tasks(3, Map(
      1 -> Task(1, "Task A", done = false),
      2 -> Task(2, "Task B", done = false),
      3 -> Task(3, "Task C", done = false)
    )))

  }

  test("done") {
    //given
    val interpreter = createInterpreter()

    //when
    interpreter.execute("add Task A")
    interpreter.execute("done 1")

    //then
    assert(interpreter.tasks === Tasks(1, Map(
      1 -> Task(1, "Task A", done = true)
    )))
  }

  test("undone") {
    //given
    val interpreter = createInterpreter()

    //when
    interpreter.execute("add Task A")
    interpreter.execute("done 1")
    interpreter.execute("undone 1")

    //then
    assert(interpreter.tasks === Tasks(1, Map(
      1 -> Task(1, "Task A", done = false)
    )))
  }

  test("clear done") {
    //given
    val interpreter = createInterpreter()

    //when
    interpreter.execute("add Task A")
    interpreter.execute("add Task B")
    interpreter.execute("add Task C")
    interpreter.execute("done 2")
    interpreter.execute("clear")

    //then
    assert(interpreter.tasks === Tasks(3, Map(
      1 -> Task(1, "Task A", done = false),
      3 -> Task(3, "Task C", done = false)
    )))
  }

  def createInterpreter(): Interpreter = {
    new StatefulInterpreterNotThreadSafe(Tasks.Empty)
  }
}
