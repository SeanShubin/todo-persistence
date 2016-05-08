package com.seanshubin.todo.persistence.core

import org.eclipse.jetty.server.Handler

class EntryPointRunner(commandLineArguments: Seq[String],
                       createJettyServer: Int => JettyServerContract,
                       handler: Handler) extends Runnable {
  override def run(): Unit = {
    val portAsString = commandLineArguments(0)
    val port = portAsString.toInt
    val jettyServer = createJettyServer(port)
    jettyServer.setHandler(handler)
    jettyServer.start()
    jettyServer.join()
  }
}
