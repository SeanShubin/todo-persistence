package com.seanshubin.todo.persistence.domain

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets
import javax.servlet.{ServletInputStream, ServletOutputStream}

import org.eclipse.jetty.server.{HttpChannel, HttpInput, Request}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

/*
 test-driven-012
 Get a contract between us and jetty, so we isolate our business logic from jetty specific details
 */
class HandlerAdapterTest extends FunSuite {
//  val charset = StandardCharsets.UTF_8
//
//  test("forward request and response") {
//    //given
//    val responseStatusCode = 12345
//    val responseBody = "response body"
//    val responseValue = ResponseValue(responseStatusCode, responseBody)
//    val delegate = new RequestValueHandlerStub(responseValue)
//    val handlerAdapter = new HandlerAdapter(delegate, charset)
//    val target = "the target"
//    val baseRequest = new StubRequest
//    val request = new StubHttpServletRequest(
//      method = "the method",
//      pathInfo = "/path info",
//      inputStreamContent = "request content"
//    )
//    val response = new StubHttpServletResponse
//
//    //when
//    handlerAdapter.handle(target, baseRequest, request, response)
//
//    //then
//    assert(delegate.requests.size === 1)
//    assert(delegate.requests.head.method === "the method")
//    assert(delegate.requests.head.path === "/path info")
//    assert(delegate.requests.head.body === "request content")
//    assert(response.status === responseStatusCode)
//    assert(response.outputStream.content === "response body")
//    assert(baseRequest.isHandled === true)
//  }
//
//  class RequestValueHandlerStub(response: ResponseValue) extends RequestValueHandler {
//    val requests = new ArrayBuffer[RequestValue]
//
//    override def handle(request: RequestValue): ResponseValue = {
//      requests.append(request)
//      response
//    }
//  }
//
//  val channel: HttpChannel = null
//  val input: HttpInput = null
//
//  class StubRequest extends Request(channel, input) {
//  }
//
//  class StubHttpServletRequest(val method: String,
//                               val pathInfo: String,
//                               val inputStreamContent: String) extends HttpServletRequestNotImplemented {
//    val inputStream = new StubServletInputStream(inputStreamContent)
//
//    override def getMethod: String = method
//
//    override def getPathInfo: String = pathInfo
//
//    override def getInputStream: ServletInputStream = inputStream
//  }
//
//  class StubHttpServletResponse extends HttpServletResponseNotImplemented {
//    var status = -1
//    val outputStream = new StubServletOutputStream
//
//    override def setStatus(sc: Int): Unit = status = sc
//
//    override def getOutputStream: ServletOutputStream = outputStream
//  }
//
//  class StubServletInputStream(content: String) extends ServletInputStreamNotImplemented {
//    val delegate = new ByteArrayInputStream(content.getBytes(charset))
//
//    override def read(): Int = delegate.read()
//  }
//
//  class StubServletOutputStream extends ServletOutputStreamNotImplemented {
//    val delegate = new ByteArrayOutputStream()
//
//    def content: String = new String(delegate.toByteArray, charset)
//
//    override def write(b: Int): Unit = delegate.write(b)
//  }
//
}
