package com.seanshubin.todo.persistence.domain

trait ConfigurationValidator {
  def validate(): Configuration
}
