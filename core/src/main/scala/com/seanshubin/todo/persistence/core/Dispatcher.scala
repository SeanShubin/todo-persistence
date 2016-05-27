package com.seanshubin.todo.persistence.core

class Dispatcher(handlersBySubject: Map[String, RequestValueHandler]) extends RequestValueHandler {
  override def handle(request: RequestValue): ResponseValue = {
    val method = request.method
    val subject = subjectOf(request)
    handlersBySubject.get(subject) match {
      case Some(handler) => try {
        handler.handle(request)
      } catch {
        case ex: Exception =>
          val exceptionString = ExceptionUtil.toString(ex)
          val message = s"When trying to apply method $method to subject $subject, got the following exception:\n$exceptionString"
          ResponseValue(500, message)
      }
      case None => ResponseValue(404, s"Not allowed to apply method $method to subject $subject")
    }
  }

  private def subjectOf(request: RequestValue): String = {
    val RequestPattern = "/([^/]*).*".r
    val RequestPattern(subject) = request.path
    subject
  }
}
