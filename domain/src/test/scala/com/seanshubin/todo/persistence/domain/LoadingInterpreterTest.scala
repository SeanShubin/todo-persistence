package com.seanshubin.todo.persistence.domain

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

/*
 test-driven-020
 Load from disk
 */
class LoadingInterpreterTest extends FunSuite {
  test("remove timestamps before forwarding command") {
    //given
    val delegate = new StubInterpreter("command result")
    val interpreter = new LoadingInterpreter(delegate)

    //when
    val result = interpreter.execute("2016-05-13T19:18:18.647Z add Task A")

    //then
    assert(result === "command result")
    assert(delegate.forwardedCommands === Seq("add Task A"))
  }

  class StubInterpreter(result: String) extends StatefulInterpreterMarker {
    val forwardedCommands = new ArrayBuffer[String]

    override def tasks: Tasks = ???

    override def execute(line: String): String = {
      forwardedCommands.append(line)
      result
    }
  }

}
