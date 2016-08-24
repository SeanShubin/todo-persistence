package com.seanshubin.todo.persistence.domain

import org.eclipse.jetty.server.Handler

trait JettyServerContract {
  def start()

  def join()

  def setHandler(handler: Handler)
}
