package com.seanshubin.todo.persistence.core

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.todo.persistence.contract.FilesContract

import scala.collection.JavaConverters._

class FileSystemHealth(files: FilesContract, dataFileDirectory: Path, healthCheckFileName: String, charset: Charset) extends Health {
  private val healthCheckFile = dataFileDirectory.resolve(healthCheckFileName)

  override def check(): ResponseValue = {
    try {
      initialize()
      val oldNumber = readNumber()
      writeNumber(oldNumber + 1)
      val newNumber = readNumber()
      if (oldNumber == newNumber) {
        ResponseValue(503, "File system not responding to modifications")
      } else {
        ResponseValue(200, "OK")
      }
    } catch {
      case ex: Exception =>
        ResponseValue(500, s"server exception with message: ${ex.getMessage}")
    }
  }

  private def initialize(): Unit = {
    if (!files.exists(dataFileDirectory)) {
      files.createDirectories(dataFileDirectory)
    }
    if (!files.exists(healthCheckFile)) {
      files.createFile(healthCheckFile)
    }
    val lines = files.readAllLines(healthCheckFile, charset).asScala
    if (lines.isEmpty) {
      files.write(healthCheckFile, Seq("0").asJava)
    }
  }

  private def readNumber(): Int = {
    val lines = files.readAllLines(healthCheckFile, charset).asScala
    val number = lines.head.toInt
    number
  }

  private def writeNumber(number: Int): Unit = {
    val lines = Seq(number.toString)
    files.write(healthCheckFile, lines.asJava)
  }
}
