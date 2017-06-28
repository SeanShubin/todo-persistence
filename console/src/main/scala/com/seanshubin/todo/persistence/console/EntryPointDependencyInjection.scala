package com.seanshubin.todo.persistence.console

import com.seanshubin.todo.persistence.domain._

trait EntryPointDependencyInjection {
  def commandLineArguments: Seq[String]

  lazy val configurationValidator: ConfigurationValidator = new CommandLineArgumentsConfigurationValidator(commandLineArguments)
  lazy val createRunner: Configuration => Runnable = (theConfiguration) => new ConfigurationDependencyInjection {
    override def configuration: Configuration = theConfiguration
  }.runner
  lazy val runner: Runnable = new EntryPointRunner(configurationValidator, createRunner)
}
