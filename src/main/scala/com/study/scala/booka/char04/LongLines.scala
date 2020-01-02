package com.study.scala.booka.char04

import scala.io.Source

object LongLines {
  def processFile(filename: String, width: Int) = {
    val source = Source.fromFile(filename)
    for (line <- source.getLines()) {
      processLine(filename, width, line)
    }
  }

  private def processLine(fileName: String, width: Int, line: String) = {
    if (line.length > width) {
      println(fileName + ":" + line.trim)
    }
  }
}
