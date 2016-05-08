package com.seanshubin.todo.persistence.core

import java.nio.charset.Charset
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler

class HandlerAdapter(delegate: RequestValueHandler, charset: Charset) extends AbstractHandler {
  override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    val method = request.getMethod
    val subject = extractSubject(request.getPathInfo)
    val body = IoUtil.inputStreamToString(request.getInputStream, charset)
    val requestValue = RequestValue(method, subject, body)
    val responseValue = delegate.handle(requestValue)
    val responseInputStream = IoUtil.stringToInputStream(responseValue.body, charset)
    response.setStatus(responseValue.statusCode)
    IoUtil.feedInputStreamToOutputStream(responseInputStream, response.getOutputStream)
    baseRequest.setHandled(true)
  }

  private def extractSubject(path: String): String = {
    if (path.startsWith("/")) {
      path.substring(0)
    } else {
      throw new RuntimeException(s"Expected path to start with '/', got $path")
    }
  }
}