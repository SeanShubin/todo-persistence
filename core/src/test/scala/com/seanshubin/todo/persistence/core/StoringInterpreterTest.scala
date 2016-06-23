package com.seanshubin.todo.persistence.core

import java.lang.Iterable
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{OpenOption, Path, Paths}
import java.time.{Clock, Instant, ZoneId}

import com.seanshubin.todo.persistence.contract.FilesNotImplemented
import org.scalatest.FunSuite

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/*
 test-driven-018
 Persist to disk
 */
class StoringInterpreterTest extends FunSuite {
  val charset = StandardCharsets.UTF_8
  test("remove timestamps before forwarding command") {
    //given
    val now = Instant.parse("2016-05-13T19:18:36.091Z")
    val clock = new StubClock(now)
    val files = new StubFiles
    val delegate = new StubInterpreter("command result")
    val dataFileDirectory = Paths.get("/data/file/directory")
    val dataFileName = "data-file.txt"
    val lock = new StubLock
    val interpreter = new StoringInterpreter(clock, files, delegate, dataFileDirectory, lock, dataFileName, charset)

    //when
    val result = interpreter.execute("add Task A")

    //then
    assert(result === "command result")
    assert(delegate.forwardedCommands === Seq("add Task A"))
    assert(files.contentsByFile.size === 1)
    assert(files.contentsByFile((Paths.get("/data/file/directory/data-file.txt"), charset)) === Seq("2016-05-13T19:18:36.091Z add Task A"))
    assert(lock.invocationCount === 1)
  }

  class StubInterpreter(result: String) extends Interpreter {
    val forwardedCommands = new ArrayBuffer[String]

    override def tasks: Tasks = ???

    override def execute(line: String): String = {
      forwardedCommands.append(line)
      result
    }
  }

  class StubClock(nowAsFarAsTestIsConcerned: Instant) extends Clock {
    override def getZone: ZoneId = ???

    override def instant(): Instant = nowAsFarAsTestIsConcerned

    override def withZone(zone: ZoneId): Clock = ???
  }

  class StubFiles extends FilesNotImplemented {
    var contentsByFile = Map[(Path, Charset), Seq[String]]()

    override def write(path: Path, lines: Iterable[_ <: CharSequence], cs: Charset, options: OpenOption*): Path = {
      contentsByFile += (path, cs) -> linesToSeq(lines)
      path

    }

    def linesToSeq(lines: Iterable[_ <: CharSequence]): Seq[String] = {
      lines.asScala.map(_.asInstanceOf[String]).toSeq
    }
  }

  class StubLock extends Lock {
    var invocationCount = 0

    override def doWithLock[T](f: => T): T = {
      invocationCount += 1
      f
    }
  }

}
