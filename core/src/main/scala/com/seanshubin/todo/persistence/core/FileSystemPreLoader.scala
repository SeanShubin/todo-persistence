package com.seanshubin.todo.persistence.core

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.todo.persistence.contract.FilesContract

import scala.collection.JavaConverters._

class FileSystemPreLoader(dataFileDirectory: Path,
                          files: FilesContract,
                          charset: Charset,
                          interpreter: Interpreter,
                          dataFileName: String) extends PreLoader {
  override def loadInitialState(): Unit = {
    files.createDirectories(dataFileDirectory)
    val dataFile = dataFileDirectory.resolve(dataFileName)
    if (!files.exists(dataFile)) {
      files.createFile(dataFile)
    }
    val lines = files.readAllLines(dataFile, charset).asScala
    lines.foreach(interpreter.execute)
  }
}
