package com.seanshubin.todo.persistence.domain

case class RequestValue(method: String, path: String, body: String = "")
