package com.seanshubin.todo.persistence.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path
import java.time.Clock

import com.seanshubin.todo.persistence.contract.{FilesContract, FilesDelegate}
import com.seanshubin.todo.persistence.core._
import org.eclipse.jetty.server.Handler

/*
- Negative demo
    - prove that the web service is not working in some way, possibly because of an empty implementation
- Test
    - write a test for the component that is not working
- Implement
    - get a single component working, here you will notice the collaborators you need
- Collaborator Contract
    - create the contracts for the collaborators, and stub these out in the test
- Collaborator Empty Implementation
    - create empty implementations of the collaborators
- Inject
    - update the wiring to constructor inject empty implementations of collaborators
- Positive demo
    - prove that the web service gets further than it did before, and discover which collaborator is needed next
- Repeat
    - The positive demo for the previous component becomes the negative demo for the next component, so repeat the process until everything works
 */
trait ConfigurationWiring {
  def configuration: Configuration

  lazy val initialTasks: Tasks = Tasks.Empty
  lazy val monitor = new AnyRef
  lazy val clock: Clock = Clock.systemUTC()
  lazy val statefulInterpreter: Interpreter =
    new StatefulInterpreterNotThreadSafe(initialTasks)
  lazy val lock: Lock = new JavaMonitorLock(monitor)
  lazy val dataFileName: String = "tasks.txt"
  lazy val files: FilesContract = FilesDelegate
  lazy val dataFileDirectory: Path = configuration.dataFileDirectory
  lazy val healthCheckFileName: String = "health.txt"
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val storingInterpreter: Interpreter = new StoringInterpreter(
    clock, files, statefulInterpreter, dataFileDirectory, lock, dataFileName, charset)
  lazy val healthCheckHandler: HealthCheckHandlerMarker = new HealthCheckHandler(
    files, dataFileDirectory, healthCheckFileName, charset)
  lazy val taskHandler: TaskHandlerMarker = new TaskHandler(statefulInterpreter)
  lazy val taskEventHandler: TaskEventHandlerMarker = new TaskEventHandler(storingInterpreter)
  lazy val dispatcher: RequestValueHandler = new DispatchPaths(healthCheckHandler, taskHandler, taskEventHandler)
  lazy val loadingInterpreter: Interpreter = new LoadingInterpreter(statefulInterpreter)
  lazy val port: Int = configuration.port
  lazy val createJettyServer: Int => JettyServerContract = JettyServerDelegate.create
  lazy val handlerAdapter: Handler = new HandlerAdapter(dispatcher, charset)
  lazy val preLoader: PreLoader = new FileSystemPreLoader(
    dataFileDirectory, files, charset, loadingInterpreter, dataFileName)
  lazy val runner: Runnable = new JettyRunner(
    port, createJettyServer, handlerAdapter, preLoader)
}

/*
Alternative designs to consider:

Should the declarations be lazy?
We could just reify the entire wiring right away.

For constructor injection, should we use by-name parameters instead of by-value parameters?

Should all constants be behind test coverage?

What did you think of the use of "Marker" interfaces?
Can you think of a better way to get compile-time feedback if parameter order is mixed up on DispatchPaths?
Would using named parameters be sufficient to catch this?

Should logic tests be shallow, where each component uses stub collaborators?
Or should they be deep, using real collaborators up to but not including non-determinism?
For the latter case, we could inherit from a wiring that hooks up everything but non-determinism.
*/
