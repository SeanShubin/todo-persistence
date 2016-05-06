package com.seanshubin.todo.persistence.core

class Dispatcher(interpreter: Interpreter) extends RequestHandler {
  override def handle(request: Request): Response = {
    (request.method, request.subject) match {
      case ("POST", "task-event") =>
        val responseBody = interpreter.execute(request.body)
        Response(201, responseBody)
      case ("GET", "task") =>
        val responseBody = TaskFormatter.tasksToString(interpreter.tasks)
        Response(200, responseBody)
      case _ => Response(405, s"Not allowed to apply method ${request.method} to subject ${request.subject}")
    }
  }
}
