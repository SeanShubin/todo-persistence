package com.seanshubin.todo.persistence.core

import javax.servlet.{ServletOutputStream, WriteListener}

class ServletOutputStreamNotImplemented extends ServletOutputStream {
  override def isReady: Boolean = ???

  override def setWriteListener(writeListener: WriteListener): Unit = ???

  override def write(b: Int): Unit = ???
}
