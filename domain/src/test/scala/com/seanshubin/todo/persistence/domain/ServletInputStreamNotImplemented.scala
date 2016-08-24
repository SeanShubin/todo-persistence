package com.seanshubin.todo.persistence.domain

import javax.servlet.{ReadListener, ServletInputStream}

class ServletInputStreamNotImplemented extends ServletInputStream {
  override def isFinished: Boolean = ???

  override def isReady: Boolean = ???

  override def setReadListener(readListener: ReadListener): Unit = ???

  override def read(): Int = ???
}
