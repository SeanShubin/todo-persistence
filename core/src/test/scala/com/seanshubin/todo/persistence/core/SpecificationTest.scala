package com.seanshubin.todo.persistence.core

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class SpecificationTest extends FunSuite {
  test("load specification"){
    val specification = loadResource("todo/specification/task/get-task.txt")
    println(specification)
  }
  def loadResource(name:String): String ={
    val inputStream = getClass.getClassLoader.getResourceAsStream(name)
    if(inputStream == null){
      throw new RuntimeException(s"Unable to load resource named $name")
    }
    val bytes = IoUtil.inputStreamToBytes(inputStream)
    val string = new String(bytes, StandardCharsets.UTF_8)
    string
  }
}
