package com.seanshubin.todo.persistence.domain

import java.nio.file.Path

case class Configuration(port: Int, dataFileDirectory: Path)
