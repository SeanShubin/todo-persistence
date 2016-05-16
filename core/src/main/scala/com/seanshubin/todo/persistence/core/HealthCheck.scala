package com.seanshubin.todo.persistence.core

trait HealthCheck {
  def check(): ResponseValue
}
