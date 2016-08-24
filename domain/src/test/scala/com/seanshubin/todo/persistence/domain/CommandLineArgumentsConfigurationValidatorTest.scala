package com.seanshubin.todo.persistence.domain

import java.nio.file.Paths

import org.scalatest.FunSuite

class CommandLineArgumentsConfigurationValidatorTest extends FunSuite {
  test("valid configuration") {
    //given
    val commandLineArguments = Seq("12345", "data-file-directory")
    val validator = createValidator(commandLineArguments)

    //when
    val configuration = validator.validate()

    //then
    assert(configuration.port === 12345)
    assert(configuration.dataFileDirectory === Paths.get("data-file-directory"))
  }

  test("server port is required") {
    //given
    val commandLineArguments = Seq()
    val validator = createValidator(commandLineArguments)

    //when
    val exception = intercept[RuntimeException] {
      validator.validate()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 0, expected 'server port', was missing")
  }

  test("server port mut be an integer") {
    //given
    val commandLineArguments = Seq("blah")
    val validator = createValidator(commandLineArguments)

    //when
    val exception = intercept[RuntimeException] {
      validator.validate()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 0, unable to convert value for 'server port' to an integer, got 'blah'")
  }

  test("data file directory is required") {
    //given
    val commandLineArguments = Seq("12345")
    val validator = createValidator(commandLineArguments)

    //when
    val exception = intercept[RuntimeException] {
      validator.validate()
    }

    //then
    assert(exception.getMessage === "In command line arguments at position 1, expected 'data file directory', was missing")
  }

  def createValidator(commandLineArguments: Seq[String]): CommandLineArgumentsConfigurationValidator = {
    new CommandLineArgumentsConfigurationValidator(commandLineArguments)
  }
}
