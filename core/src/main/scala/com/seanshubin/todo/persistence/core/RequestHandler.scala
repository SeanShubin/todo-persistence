package com.seanshubin.todo.persistence.core

trait RequestHandler {
  def handle(request: Request): Response
}
