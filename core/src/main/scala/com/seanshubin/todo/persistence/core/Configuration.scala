package com.seanshubin.todo.persistence.core

import java.nio.file.Path

case class Configuration(port: Int, dataFileDirectory: Path)
