package com.seanshubin.todo.persistence.domain

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.attribute.FileAttribute
import java.nio.file.{LinkOption, Path, Paths}
import java.util

import com.seanshubin.todo.persistence.contract.FilesNotImplemented
import org.scalatest.FunSuite

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/*
 test-driven-019
 Start with correct initial state
 */
class PreLoaderTest extends FunSuite {
  test("load existing file") {
    //given
    val dataFileDirectory = Paths.get("data/file/directory")
    val dataFileName = "data-file-name.txt"
    val dataFile = dataFileDirectory.resolve(dataFileName)
    val commands = Seq("Command A", "Command B", "Command C")
    val linesByFile = Map(dataFile -> commands)
    val files = new StubFilesContract(existingPaths = Set(dataFile), linesByFile)
    val charset = StandardCharsets.UTF_8
    val interpreter = new StubInterpreter
    val preLoader = new FileSystemPreLoader(dataFileDirectory, files, charset, interpreter, dataFileName)

    //when
    preLoader.loadInitialState()

    //then
    assert(files.createDirectoriesInvocations === Seq(dataFileDirectory))
    assert(files.createFileInvocations === Seq())
    assert(interpreter.linesInterpreted === commands)
  }

  test("create file if it does not exist") {
    //given
    val dataFileDirectory = Paths.get("data/file/directory")
    val dataFileName = "data-file-name.txt"
    val dataFile = dataFileDirectory.resolve(dataFileName)
    val commands = Seq("Command A", "Command B", "Command C")
    val linesByFile = Map(dataFile -> commands)
    val files = new StubFilesContract(existingPaths = Set(), linesByFile)
    val charset = StandardCharsets.UTF_8
    val interpreter = new StubInterpreter
    val preLoader = new FileSystemPreLoader(dataFileDirectory, files, charset, interpreter, dataFileName)

    //when
    preLoader.loadInitialState()

    //then
    assert(files.createDirectoriesInvocations === Seq(dataFileDirectory))
    assert(files.createFileInvocations === Seq(dataFile))
    assert(interpreter.linesInterpreted === commands)
  }

  class StubFilesContract(existingPaths: Set[Path], linesByFile: Map[Path, Seq[String]]) extends FilesNotImplemented {
    val createDirectoriesInvocations = new ArrayBuffer[Path]()
    val createFileInvocations = new ArrayBuffer[Path]()

    override def createDirectories(dir: Path, attrs: FileAttribute[_]*): Path = {
      createDirectoriesInvocations.append(dir)
      dir
    }

    override def exists(path: Path, options: LinkOption*): Boolean = {
      existingPaths.contains(path)
    }

    override def createFile(path: Path, attrs: FileAttribute[_]*): Path = {
      createFileInvocations.append(path)
      path
    }

    override def readAllLines(path: Path, cs: Charset): util.List[String] = {
      linesByFile(path).asJava
    }
  }

  class StubInterpreter extends LoadingInterpreterMarker {
    val linesInterpreted = new ArrayBuffer[String]()

    override def tasks: Tasks = ???

    override def execute(line: String): String = {
      linesInterpreted.append(line)
      "ignored"
    }
  }

}
