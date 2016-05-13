package com.seanshubin.todo.persistence.core

class EntryPointRunner(configurationValidator: ConfigurationValidator,
                       createRunner: Configuration => Runnable) extends Runnable {
  override def run(): Unit = {
    val configuration = configurationValidator.validate()
    val runner = createRunner(configuration)
    runner.run()
  }
}
