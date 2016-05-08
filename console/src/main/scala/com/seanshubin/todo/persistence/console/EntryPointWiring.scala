package com.seanshubin.todo.persistence.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.todo.persistence.core._
import org.eclipse.jetty.server.Handler

trait EntryPointWiring {
  def commandLineArguments: Seq[String]

  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val initialTasks: Tasks = Tasks.Empty
  lazy val interpreter: Interpreter = new StatefulInterpreter(initialTasks)
  lazy val dispatcher: RequestValueHandler = new Dispatcher(interpreter)
  lazy val jettyHandler: Handler = new HandlerAdapter(dispatcher, charset)
  lazy val runner: Runnable = new EntryPointRunner(commandLineArguments, JettyServerDelegate.create, jettyHandler)
}
