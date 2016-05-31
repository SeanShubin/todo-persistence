package com.seanshubin.todo.persistence.core

import java.lang.Iterable
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file._
import java.nio.file.attribute.FileAttribute
import java.util

import com.seanshubin.todo.persistence.contract.FilesNotImplemented
import org.scalatest.FunSuite

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/*
 test-driven-014
 Expose an endpoint for clients of this service to determine if they are configured correctly and that this service has what it needs to run
 */
class HealthCheckHandlerTest extends FunSuite {
  val charset = StandardCharsets.UTF_8

  test("health check data already exists") {
    //given
    val path = Paths.get("/data/file/directory/health-check-file.txt")
    val helper = new Helper {
      val existingFiles = Set[Path]()

      override def contentsByFile: Map[(Path, Charset), Seq[String]] = Map((path, charset) -> Seq("123"))
    }
    val dummyRequest = RequestValue("GET", "/health")

    //when
    val response = helper.healthCheck.handle(dummyRequest)

    //then
    assert(response.statusCode === 200)
    assert(response.body === "OK")

    assert(helper.files.createDirectoriesInvocations === Seq(Paths.get("/data/file/directory")))
    assert(helper.files.createFileInvocations === Seq(Paths.get("/data/file/directory/health-check-file.txt")))
    assert(helper.files.linesWritten === Map())
  }

  trait Helper {
    def existingFiles: Set[Path]

    def contentsByFile: Map[(Path, Charset), Seq[String]]

    lazy val files = new StubFiles(existingFiles, contentsByFile)
    lazy val dataFileDirectory = Paths.get("/data/file/directory")
    lazy val healthCheckFileName = "health-check-file.txt"
    lazy val healthCheck = new HealthCheckHandler(files, dataFileDirectory, healthCheckFileName, charset)
  }

  class StubFiles(existingFiles: Set[Path], initialContentsByFile: Map[(Path, Charset), Seq[String]]) extends FilesNotImplemented {
    val createDirectoriesInvocations = new ArrayBuffer[Path]
    val createFileInvocations = new ArrayBuffer[Path]
    var linesWritten = Map[(Path, Charset), Seq[String]]()
    var contentsByFile = initialContentsByFile

    override def exists(path: Path, options: LinkOption*): Boolean = existingFiles.contains(path)

    override def createDirectories(dir: Path, attrs: FileAttribute[_]*): Path = {
      createDirectoriesInvocations.append(dir)
      dir
    }

    override def createFile(path: Path, attrs: FileAttribute[_]*): Path = {
      createFileInvocations.append(path)
      path
    }

    override def readAllLines(path: Path, cs: Charset): util.List[String] = {
      contentsByFile((path, cs)).asJava
    }

    override def write(path: Path, lines: Iterable[_ <: CharSequence], cs: Charset, options: OpenOption*): Path = {
      contentsByFile += (path, cs) -> linesToSeq(lines)
      path
    }

    def linesToSeq(lines: Iterable[_ <: CharSequence]): Seq[String] = {
      lines.asScala.map(_.asInstanceOf[String]).toSeq
    }
  }

}

