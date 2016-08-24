package com.seanshubin.todo.persistence.domain

trait HealthCheck {
  def check(): ResponseValue
}
