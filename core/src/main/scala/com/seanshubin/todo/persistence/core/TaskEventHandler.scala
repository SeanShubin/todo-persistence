package com.seanshubin.todo.persistence.core

class TaskEventHandler(interpreter: Interpreter) extends RequestValueHandler {
  override def handle(request: RequestValue): ResponseValue = {
    val responseBody = StringUtil.normalizeLines(interpreter.execute(request.body))
    ResponseValue(200, responseBody)
  }
}
