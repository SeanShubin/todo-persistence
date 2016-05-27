package com.seanshubin.todo.persistence.core

case class RequestValue(method: String, path: String, body: String = "")
