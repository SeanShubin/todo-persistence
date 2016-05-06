package com.seanshubin.todo.persistence.core

case class Request(method: String, subject: String, body: String = "")
