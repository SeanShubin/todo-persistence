package com.seanshubin.todo.persistence.core

trait ConfigurationValidator {
  def validate(): Configuration
}
