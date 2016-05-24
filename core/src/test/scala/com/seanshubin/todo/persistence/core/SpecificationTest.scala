package com.seanshubin.todo.persistence.core

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class SpecificationTest extends FunSuite {
  test("get tasks") {
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
    assert(StringUtil.escape(actual.body) === StringUtil.escape(expected.body))
    assert(actual === expected)
  }

  test("add task") {
    //given
    val request = loadRequest("todo/specification/task-event/post-task-event-add-request.txt")
    val expected = loadResponse("todo/specification/task-event/post-task-event-add-response.txt")
    val tasks = Tasks.Empty
    val dispatcher = createDispatcher(tasks)
    //when
    val actual = dispatcher.handle(request)
    //then
    assert(StringUtil.escape(actual.body) === StringUtil.escape(expected.body))
    assert(actual === expected)
  }

  test("clear tasks") {
    //given
    val request = loadRequest("todo/specification/task-event/post-task-event-clear-request.txt")
    val expected = loadResponse("todo/specification/task-event/post-task-event-clear-response.txt")
    val tasks = Tasks(lastId = 3, tasks = Map(
      1 -> Task(1, "Task A", done = false),
      2 -> Task(2, "Task B", done = true),
      3 -> Task(3, "Task C", done = false)
    ))
    val dispatcher = createDispatcher(tasks)
    //when
    val actual = dispatcher.handle(request)
    //then
    assert(StringUtil.escape(actual.body) === StringUtil.escape(expected.body))
    assert(actual === expected)
  }

  test("task done") {
    //given
    val request = loadRequest("todo/specification/task-event/post-task-event-done-request.txt")
    val expected = loadResponse("todo/specification/task-event/post-task-event-done-response.txt")
    val tasks = Tasks(lastId = 1, tasks = Map(1 -> Task(1, "Task A", done = false)))
    val dispatcher = createDispatcher(tasks)
    //when
    val actual = dispatcher.handle(request)
    //then
    assert(StringUtil.escape(actual.body) === StringUtil.escape(expected.body))
    assert(actual === expected)
  }

  test("task undone") {
    //given
    val request = loadRequest("todo/specification/task-event/post-task-event-undone-request.txt")
    val expected = loadResponse("todo/specification/task-event/post-task-event-undone-response.txt")
    val tasks = Tasks(lastId = 1, tasks = Map(1 -> Task(1, "Task A", done = true)))
    val dispatcher = createDispatcher(tasks)
    //when
    val actual = dispatcher.handle(request)
    //then
    assert(StringUtil.escape(actual.body) === StringUtil.escape(expected.body))
    assert(actual === expected)
  }

  def loadResource(rawName: String): String = {
    val name = "serve-from-classpath/" + rawName
    val inputStream = getClass.getClassLoader.getResourceAsStream(name)
    if (inputStream == null) {
      throw new RuntimeException(s"Unable to load resource named $name")
    }
    val bytes = IoUtil.inputStreamToBytes(inputStream)
    val rawString = new String(bytes, StandardCharsets.UTF_8)
    val normalizedString = StringUtil.normalizeLines(rawString)
    normalizedString
  }

  def createDispatcher(): Dispatcher = {
    val tasks = Tasks.Empty
    val dummyHealth = null
    val interpreter = new StatefulInterpreterNotThreadSafe(tasks)
    val dispatcher = new Dispatcher(interpreter, dummyHealth)
    dispatcher
  }

  val HttpRequestLineOneRegex = """(\w+) (/[\w\-]+) HTTP/1.1""".r
  val HttpResponseLineOneRegex = """HTTP/1.1 (\d+) \w+""".r

  def loadRequest(name: String): RequestValue = {
    val requestHttp = loadResource(name)
    val lines = StringUtil.stringToLines(requestHttp)
    val HttpRequestLineOneRegex(method, subject) = lines(0)
    val body = extractBody(lines)
    RequestValue(method, subject, body)
  }

  def loadResponse(name: String): ResponseValue = {
    val responseHttp = loadResource(name)
    val lines = StringUtil.stringToLines(responseHttp)
    val HttpResponseLineOneRegex(statusCodeString) = lines(0)
    val statusCode = statusCodeString.toInt
    val body = extractBody(lines)
    ResponseValue(statusCode, body)
  }

  def extractBody(lines: Seq[String]): String = {
    def skipBeforeBody(remainingLines: List[String]): String = {
      if (remainingLines.isEmpty) {
        ""
      } else if (remainingLines.head == "") {
        StringUtil.linesToString(remainingLines.tail)
      } else {
        skipBeforeBody(remainingLines.tail)
      }
    }
    skipBeforeBody(lines.toList)
  }

  def createDispatcher(tasks: Tasks): Dispatcher = {
    val dummyHealth = null
    val interpreter = new StatefulInterpreterNotThreadSafe(tasks)
    val dispatcher = new Dispatcher(interpreter, dummyHealth)
    dispatcher
  }
}
