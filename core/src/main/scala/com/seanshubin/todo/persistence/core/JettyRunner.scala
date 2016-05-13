package com.seanshubin.todo.persistence.core

import org.eclipse.jetty.server.Handler

class JettyRunner(port: Int,
                  createJettyServer: Int => JettyServerContract,
                  handler: Handler,
                  preLoader: PreLoader) extends Runnable {
  override def run(): Unit = {
    preLoader.loadInitialState()
    val jettyServer = createJettyServer(port)
    jettyServer.setHandler(handler)
    jettyServer.start()
    jettyServer.join()
  }
}
