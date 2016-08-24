package com.seanshubin.todo.persistence.domain

class DispatchPaths(/*healthCheckHandler: HealthCheckHandlerMarker,
                    taskHandler: TaskHandlerMarker,
                    taskEventHandler: TaskEventHandlerMarker*/) extends RequestValueHandler {
//  private val handlersBySubject: Map[String, RequestValueHandler] = Map(
//    "health" -> healthCheckHandler,
//    "task-event" -> taskEventHandler,
//    "task" -> taskHandler)
//  private val dispatcher: RequestValueHandler = new Dispatcher(handlersBySubject)

  override def handle(request: RequestValue): ResponseValue = {
//    dispatcher.handle(request)
    ???
  }
}
