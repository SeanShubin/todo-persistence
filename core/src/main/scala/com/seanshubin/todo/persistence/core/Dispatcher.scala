package com.seanshubin.todo.persistence.core

class Dispatcher(interpreter: Interpreter) extends RequestValueHandler {
  override def handle(request: RequestValue): ResponseValue = {
    (request.method, request.subject) match {
      case ("POST", "/task-event") =>
        val responseBody = interpreter.execute(request.body)
        ResponseValue(201, responseBody)
      case ("GET", "/task") =>
        val responseBody = TaskFormatter.tasksToString(interpreter.tasks)
        ResponseValue(200, responseBody)
      case _ => ResponseValue(405, s"Not allowed to apply method ${request.method} to subject ${request.subject}")
    }
  }
}
