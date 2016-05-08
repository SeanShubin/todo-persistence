package com.seanshubin.todo.persistence.core

import java.io._
import java.nio.charset.Charset

import scala.annotation.tailrec

object IoUtil {
  def inputStreamToString(inputStream: InputStream, charset: Charset): String = {
    val bytes = inputStreamToBytes(inputStream)
    new String(bytes, charset)
  }

  def stringToInputStream(s: String, charset: Charset): InputStream = {
    new ByteArrayInputStream(s.getBytes(charset))
  }

  def inputStreamToBytes(inputStream: InputStream): Array[Byte] = {
    val outputStream = new ByteArrayOutputStream
    feedInputStreamToOutputStream(inputStream, outputStream)
    val byteArray = outputStream.toByteArray
    byteArray
  }

  def bytesToOutputStream(bytes: Seq[Byte], outputStream: OutputStream): Unit = {
    val inputStream = new ByteArrayInputStream(bytes.toArray)
    feedInputStreamToOutputStream(inputStream, outputStream)
  }

  def feedInputStreamToOutputStream(inputStream: InputStream, outputStream: OutputStream) {
    @tailrec
    def loop(byte: Int) {
      if (byte != -1) {
        outputStream.write(byte)
        loop(inputStream.read())
      }
    }
    loop(inputStream.read())
  }
}
