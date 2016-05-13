package com.seanshubin.todo.persistence.core

import java.nio.file.{Path, StandardOpenOption}
import java.time.Clock

import com.seanshubin.todo.persistence.contract.FilesContract

import scala.collection.JavaConverters._

class StoringInterpreter(clock: Clock, files: FilesContract, delegate: Interpreter, dataFileDirectory: Path, dataFileName: String) extends Interpreter {
  private val dataFile = dataFileDirectory.resolve(dataFileName)

  override def tasks: Tasks = delegate.tasks

  override def execute(line: String): String = {
    val command = CommandParser.parse(line)
    val timestamp = clock.instant()
    val timestampedLine = TimestampedCommandFormatter.format(timestamp, command)
    storeLine(timestampedLine)
    val result = delegate.execute(line)
    result
  }

  private def storeLine(line: String): Unit = {
    val javaLines = Seq(line).asJava
    files.write(dataFile, javaLines, StandardOpenOption.APPEND)
  }
}
