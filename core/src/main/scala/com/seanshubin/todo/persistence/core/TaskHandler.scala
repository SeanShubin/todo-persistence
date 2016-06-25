package com.seanshubin.todo.persistence.core

class TaskHandler(interpreter: StatefulInterpreterMarker) extends TaskHandlerMarker {
  override def handle(request: RequestValue): ResponseValue = {
    val responseBody = StringUtil.normalizeLines(TaskFormatter.tasksToString(interpreter.tasks))
    ResponseValue(200, responseBody)
  }
}
