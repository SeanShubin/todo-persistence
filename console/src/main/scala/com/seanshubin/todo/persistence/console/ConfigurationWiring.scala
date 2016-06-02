package com.seanshubin.todo.persistence.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path
import java.time.Clock

import com.seanshubin.todo.persistence.contract.{FilesContract, FilesDelegate}
import com.seanshubin.todo.persistence.core._
import org.eclipse.jetty.server.Handler

trait ConfigurationWiring {
  def configuration: Configuration

  lazy val port: Int = configuration.port
  lazy val dataFileDirectory: Path = configuration.dataFileDirectory
  lazy val monitor = new AnyRef
  lazy val dataFileName: String = "tasks.txt"
  lazy val healthCheckFileName: String = "health.txt"
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val initialTasks: Tasks = Tasks.Empty
  lazy val statefulInterpreter: Interpreter = new StatefulInterpreterNotThreadSafe(initialTasks)
  lazy val clock: Clock = Clock.systemUTC()
  lazy val files: FilesContract = FilesDelegate
  lazy val lock: Lock = new JavaMonitorLock(monitor)
  lazy val storingInterpreter: Interpreter = new StoringInterpreter(
    clock, files, statefulInterpreter, dataFileDirectory, lock, dataFileName, charset)
  lazy val loadingInterpreter: Interpreter = new LoadingInterpreter(statefulInterpreter)
  lazy val healthCheckHandler: RequestValueHandler = new HealthCheckHandler(
    files, dataFileDirectory, healthCheckFileName, charset)
  lazy val taskHandler: RequestValueHandler = new TaskHandler(statefulInterpreter)
  lazy val taskEventHandler: RequestValueHandler = new TaskEventHandler(storingInterpreter)
  lazy val handlersBySubject: Map[String, RequestValueHandler] = Map(
    "health" -> healthCheckHandler,
    "task-event" -> taskEventHandler,
    "task" -> taskHandler
  )
  lazy val dispatcher: RequestValueHandler = new Dispatcher(handlersBySubject)
  lazy val handlerAdapter: Handler = new HandlerAdapter(dispatcher, charset)
  lazy val preLoader: PreLoader = new FileSystemPreLoader(
    dataFileDirectory, files, charset, loadingInterpreter, dataFileName)
  lazy val runner: Runnable = new JettyRunner(
    port, JettyServerDelegate.create, handlerAdapter, preLoader)
}
