package com.seanshubin.todo.persistence.core

trait RequestValueHandler {
  def handle(request: RequestValue): ResponseValue
}
