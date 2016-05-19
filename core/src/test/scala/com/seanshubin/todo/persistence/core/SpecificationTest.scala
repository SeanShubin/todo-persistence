package com.seanshubin.todo.persistence.core

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class SpecificationTest extends FunSuite {
  test("load specification") {
    //given
    val request = loadRequest("todo/specification/task/get-task-request.txt")
    val expected = loadResponse("todo/specification/task/get-task-response.txt")
    val tasks = Tasks(lastId = 3, tasks = Map(
      1 -> Task(1, "Task A", done = false),
      2 -> Task(2, "Task B", done = true),
      3 -> Task(3, "Task C", done = false)
    ))
    val dispatcher = createDispatcher(tasks)
    //when
    val actual = dispatcher.handle(request)
    //then
    assert(actual === expected)
  }

  def loadResource(name: String): String = {
    val inputStream = getClass.getClassLoader.getResourceAsStream(name)
    if (inputStream == null) {
      throw new RuntimeException(s"Unable to load resource named $name")
    }
    val bytes = IoUtil.inputStreamToBytes(inputStream)
    val string = new String(bytes, StandardCharsets.UTF_8)
    string
  }

  def createDispatcher(): Dispatcher = {
    val tasks = Tasks.Empty
    val dummyHealth = null
    val interpreter = new StatefulInterpreter(tasks)
    val dispatcher = new Dispatcher(interpreter, dummyHealth)
    dispatcher
  }

  val HttpRequestLineOneRegex = """(\w+) (/\w+) HTTP/1.1""".r
  val HttpResponseLineOneRegex = """HTTP/1.1 (\d+) \w+""".r

  def loadRequest(name: String): RequestValue = {
    val requestHttp = loadResource(name)
    val lines = stringToLines(requestHttp)
    val HttpRequestLineOneRegex(method, subject) = lines(0)
    val body = ""
    RequestValue(method, subject, body)
  }

  def loadResponse(name: String): ResponseValue = {
    val responseHttp = loadResource(name)
    val lines = stringToLines(responseHttp)
    val HttpResponseLineOneRegex(statusCodeString) = lines(0)
    val statusCode = statusCodeString.toInt
    val body = extractBody(lines)
    ResponseValue(statusCode, body)
  }

  def stringToLines(s: String): Seq[String] = s.split("""\r\n|\r|\n""", -1).toSeq

  def extractBody(lines: Seq[String]): String = {
    def skipBeforeBody(remainingLines: List[String]): String = {
      if (remainingLines.isEmpty) {
        ""
      } else if (remainingLines.head == "") {
        remainingLines.tail.mkString("\n")
      } else {
        skipBeforeBody(remainingLines.tail)
      }
    }
    skipBeforeBody(lines.toList)
  }

  class StubInterpreter(theTasks: Tasks) extends Interpreter {
    val linesExecuted = new ArrayBuffer[String]()

    override def tasks: Tasks = theTasks

    override def execute(line: String): String = {
      linesExecuted.append(line)
      ""
    }
  }

  def createDispatcher(tasks: Tasks): Dispatcher = {
    val dummyHealth = null
    val interpreter = new StubInterpreter(tasks)
    val dispatcher = new Dispatcher(interpreter, dummyHealth)
    dispatcher
  }
}
