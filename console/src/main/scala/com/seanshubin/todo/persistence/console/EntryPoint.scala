package com.seanshubin.todo.persistence.console

object EntryPoint extends App {
  new EntryPointDependencyInjection {
    override def commandLineArguments: Seq[String] = args
  }.runner.run()
}
