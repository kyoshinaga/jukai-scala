package jukaiScala.main

import java.io._

import jukaiScala.hdflib._
import jukaiScala.merlin.{Functor, Tokenizer}

/**
  * Created by ubuntu on 10/14/16.
  */

import jukaiScala.merlin._

class JukaiNLP(val t: Tokenizer) {
  var file = "hoge"

  def openStandardIn: BufferedReader = bufReader(System.in)

  def bufReader(stream: InputStream) = new BufferedReader(new InputStreamReader(stream))

  def run() = {
    val reader = openStandardIn

    reader.ready() match {
      case false => shell(reader)
      case true =>
    }
  }

  private[this] def shell(reader: BufferedReader) = {
    def readLine: String = {
      System.err.print("> ")
      reader.readLine match {
        case null => ""
        case l if l.trim().isEmpty => readLine
        case l => l
      }
    }

    var in = readLine
    while (in != "") {
      val tokenizedStr = t.tokenize(in)
      println(tokenizedStr)
      in = readLine
    }
  }

  def close() = t.close()
}

object JukaiNLP {
  def main(args: Array[String]): Unit = {
    val filePath = args(0)
    val t = Tokenizer(filePath)

    val jukaiNLPTokenize = new JukaiNLP(t)
    try {
      jukaiNLPTokenize.run()
    }finally jukaiNLPTokenize.close()
  }
}
