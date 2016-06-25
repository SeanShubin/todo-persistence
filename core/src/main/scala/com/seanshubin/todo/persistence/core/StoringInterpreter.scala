package com.seanshubin.todo.persistence.core

import java.nio.charset.Charset
import java.nio.file.{Path, StandardOpenOption}
import java.time.Clock

import com.seanshubin.todo.persistence.contract.FilesContract

import scala.collection.JavaConverters._

class StoringInterpreter(clock: Clock,
                         files: FilesContract,
                         delegate: StatefulInterpreterMarker,
                         dataFileDirectory: Path,
                         lock: Lock,
                         dataFileName: String,
                         charset: Charset) extends StoringInterpreterMarker {
  private val dataFile = dataFileDirectory.resolve(dataFileName)

  override def tasks: Tasks = {
    delegate.tasks
  }

  override def execute(line: String): String = {
    val command = CommandParser.parse(line)
    val timestamp = clock.instant()
    val timestampedLine = TimestampedCommandFormatter.format(timestamp, command)
    val result = lock.doWithLock {
      storeLine(timestampedLine)
      delegate.execute(line)
    }
    result
  }

  private def storeLine(line: String): Unit = {
    val javaLines = Seq(line).asJava
    files.write(dataFile, javaLines, charset, StandardOpenOption.APPEND)
  }
}
