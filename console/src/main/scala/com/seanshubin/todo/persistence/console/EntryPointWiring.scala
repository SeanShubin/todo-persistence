package com.seanshubin.todo.persistence.console

import com.seanshubin.todo.persistence.domain._

trait EntryPointWiring {
  def commandLineArguments: Seq[String]

  lazy val configurationValidator: ConfigurationValidator = new CommandLineArgumentsConfigurationValidator(commandLineArguments)
  lazy val createRunner: Configuration => Runnable = (theConfiguration) => new ConfigurationWiring {
    override def configuration: Configuration = theConfiguration
  }.runner
  lazy val runner: Runnable = new EntryPointRunner(configurationValidator, createRunner)
}
