package com.seanshubin.todo.persistence.domain

object StringUtil {
  val LineSeparator = "\r\n"

  def normalizeLines(s: String): String = {
    linesToString(stringToLines(s.trim))
  }

  def stringToLines(s: String): Seq[String] = s.split("""\r\n|\r|\n""", -1).toSeq

  def linesToString(lines: Seq[String]): String = lines.mkString(LineSeparator)

  def escape(target: String): String = {
    target.flatMap {
      case '\n' => "\\n"
      case '\b' => "\\b"
      case '\t' => "\\t"
      case '\f' => "\\f"
      case '\r' => "\\r"
      case '\"' => "\\\""
      case '\'' => "\\\'"
      case '\\' => "\\\\"
      case x => x.toString
    }
  }
}
