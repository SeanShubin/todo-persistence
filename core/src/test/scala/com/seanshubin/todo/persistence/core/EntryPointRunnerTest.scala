package com.seanshubin.todo.persistence.core

import org.scalatest.FunSuite

class EntryPointRunnerTest extends FunSuite {
  test("valid configuration") {
    //given
    val serverPortString = "12345"
    val dataFileDirectoryString = "data/file/directory"
    val helper = new Helper(serverPortString, dataFileDirectoryString)

    //when
    helper.runner.run()

    //then
    assert(helper.stubRunnable.invocations === 1)
  }

  test("missing port") {
    //given
    val helper = new Helper()

    //when
    val exception = intercept[RuntimeException] {
      helper.runner.run()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 0, expected 'server port', was missing")
  }

  test("non numeric port") {
    //given
    val serverPortString = "foo"
    val dataFileDirectoryString = "data/file/directory"
    val helper = new Helper(serverPortString, dataFileDirectoryString)

    //when
    val exception = intercept[RuntimeException] {
      helper.runner.run()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 0, unable to convert value for 'server port' to an integer, got 'foo'")
  }

  test("missing data directory") {
    //given
    val serverPortString = "12345"
    val helper = new Helper(serverPortString)

    //when
    val exception = intercept[RuntimeException] {
      helper.runner.run()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 1, expected 'data file directory', was missing")
  }

  class StubRunnable(configuration: Configuration) extends Runnable {
    var invocations = 0

    override def run(): Unit = invocations += 1
  }

  class Helper(commandLineArguments: String*) {
    val configurationValidator = new CommandLineArgumentsConfigurationValidator(commandLineArguments)
    var stubRunnable: StubRunnable = null
    val createRunner: Configuration => Runnable = configuration => {
      stubRunnable = new StubRunnable(configuration)
      stubRunnable
    }
    val runner = new EntryPointRunner(configurationValidator, createRunner)
  }

}
