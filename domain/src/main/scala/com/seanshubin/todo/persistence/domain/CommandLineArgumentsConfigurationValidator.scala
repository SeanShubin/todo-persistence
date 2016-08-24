package com.seanshubin.todo.persistence.domain

import java.nio.file.{Path, Paths}

class CommandLineArgumentsConfigurationValidator(commandLineArguments: Seq[String]) extends ConfigurationValidator {
  override def validate(): Configuration = {
    val port = validateCommandLineArgumentInt(0, "server port")
    val dataFileDirectory = validateCommandLineArgumentPath(1, "data file directory")
    val configuration = Configuration(
      port = port,
      dataFileDirectory = dataFileDirectory)
    configuration
  }

  private def validateCommandLineArgumentInt(expectedPosition: Int, name: String): Int = {
    val asString = validateCommandLineArgumentString(expectedPosition, name)
    try {
      asString.toInt
    } catch {
      case ex: NumberFormatException =>
        throw new RuntimeException(s"In command line arguments at position $expectedPosition, unable to convert value for '$name' to an integer, got '$asString'")
    }
  }

  private def validateCommandLineArgumentPath(expectedPosition: Int, name: String): Path = {
    val asString = validateCommandLineArgumentString(expectedPosition, name)
    val path = Paths.get(asString)
    path
  }

  private def validateCommandLineArgumentString(expectedPosition: Int, name: String): String = {
    if (commandLineArguments.size < expectedPosition + 1) {
      throw new RuntimeException(s"In command line arguments at position $expectedPosition, expected '$name', was missing")
    } else {
      commandLineArguments(expectedPosition)
    }
  }
}
