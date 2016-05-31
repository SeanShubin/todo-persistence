package com.seanshubin.todo.persistence.core

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Handler, Request}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

/*
 test-driven-011
 Since we are using service oriented architecture, start with a jetty server
 */
class JettyRunnerTest extends FunSuite {
  test("jetty runner") {
    //given
    val port = 123
    var stubJettyServer: StubJettyServerContract = null
    def createJettyServer = (port: Int) => {
      stubJettyServer = new StubJettyServerContract(port)
      stubJettyServer
    }
    val handler = new StubHandler
    val preLoader = new StubPreLoader
    val jettyRunner = new JettyRunner(port, createJettyServer, handler, preLoader)

    //when
    jettyRunner.run()

    //then
    assert(preLoader.invocations === 1)
    assert(stubJettyServer.handler === handler)
    assert(stubJettyServer.invocations === Seq("start", "join"))
  }

  class StubJettyServerContract(port: Int) extends JettyServerContract {
    var handler: Handler = null
    val invocations = new ArrayBuffer[String]()

    override def start(): Unit = invocations.append("start")

    override def setHandler(newHandler: Handler): Unit = handler = newHandler

    override def join(): Unit = invocations.append("join")
  }

  class StubHandler extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = ???
  }

  class StubPreLoader extends PreLoader {
    var invocations = 0

    override def loadInitialState(): Unit = invocations += 1
  }

}
