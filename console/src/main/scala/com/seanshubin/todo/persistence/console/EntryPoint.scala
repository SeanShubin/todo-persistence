package com.seanshubin.todo.persistence.console

object EntryPoint extends App {
  new EntryPointWiring {
    override def commandLineArguments: Seq[String] = args
  }.runner.run()
}
