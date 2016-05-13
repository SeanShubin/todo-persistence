package com.seanshubin.todo.persistence.console

import java.nio.charset.{Charset, StandardCharsets}
import java.time.Clock

import com.seanshubin.todo.persistence.contract.{FilesContract, FilesDelegate}
import com.seanshubin.todo.persistence.core._
import org.eclipse.jetty.server.Handler

trait ConfigurationWiring {
  def configuration: Configuration

  lazy val dataFileName: String = "tasks.txt"
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val initialTasks: Tasks = Tasks.Empty
  lazy val statefulInterpreter: Interpreter = new StatefulInterpreter(initialTasks)
  lazy val clock: Clock = Clock.systemUTC()
  lazy val files: FilesContract = FilesDelegate
  lazy val storingInterpreter: Interpreter = new StoringInterpreter(clock, files, statefulInterpreter, configuration.dataFileDirectory, dataFileName)
  lazy val loadingInterpreter: Interpreter = new LoadingPersistentInterpreter(statefulInterpreter)
  lazy val dispatcher: RequestValueHandler = new Dispatcher(storingInterpreter)
  lazy val jettyHandler: Handler = new HandlerAdapter(dispatcher, charset)
  lazy val preLoader: PreLoader = new FileSystemPreLoader(configuration.dataFileDirectory, files, charset, loadingInterpreter, dataFileName)
  lazy val runner: Runnable = new JettyRunner(configuration.port, JettyServerDelegate.create, jettyHandler, preLoader)
}
