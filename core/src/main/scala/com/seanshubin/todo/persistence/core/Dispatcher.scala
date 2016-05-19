package com.seanshubin.todo.persistence.core

class Dispatcher(interpreter: Interpreter, health: HealthCheck) extends RequestValueHandler {
  override def handle(request: RequestValue): ResponseValue = {
    try {
      (request.method, request.subject) match {
        case ("POST", "/task-event") =>
          val responseBody = StringUtil.normalizeLines(interpreter.execute(request.body))
          ResponseValue(200, responseBody)
        case ("GET", "/task") =>
          val responseBody = StringUtil.normalizeLines(TaskFormatter.tasksToString(interpreter.tasks))
          ResponseValue(200, responseBody)
        case ("GET", "/health") =>
          health.check()
        case _ => ResponseValue(405, s"Not allowed to apply method ${request.method} to subject ${request.subject}")
      }
    } catch {
      case ex: Exception => ResponseValue(500, s"Exception with message '${ex.getMessage}' when trying to apply method ${request.method} to subject ${request.subject}")
    }
  }
}
