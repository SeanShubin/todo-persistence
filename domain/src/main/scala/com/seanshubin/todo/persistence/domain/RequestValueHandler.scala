package com.seanshubin.todo.persistence.domain

trait RequestValueHandler {
  def handle(request: RequestValue): ResponseValue
}
