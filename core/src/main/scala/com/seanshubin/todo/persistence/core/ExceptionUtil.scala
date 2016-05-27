package com.seanshubin.todo.persistence.core

import java.io.{PrintWriter, StringWriter}

object ExceptionUtil {
  def toString(throwable: Throwable): String = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    throwable.printStackTrace(printWriter)
    stringWriter.toString
  }
}
