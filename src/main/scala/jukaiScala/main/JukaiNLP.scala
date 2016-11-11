package jukaiScala.main

import java.io._

import jukaiScala.hdflib._
import jukaiScala.merlin.{Functor, Tokenizer}

/**
  * Created by ubuntu on 10/14/16.
  */

import jukaiScala.merlin._

class JukaiNLP(val t: Tokenizer) {

/*  def openStandardIn: BufferedReader = bufReader(System.in)

  def bufReader(stream: InputStream) = new BufferedReader((new InputStreamReader(stream)))

  def run = {
    val reader = openStandardIn

    reader.ready() match {
      case false => shell(reader)
    }
  }

  private[thid] def shell(reader: BufferedReader) = {
    def readLine: String = {
      System.err.print("> ")
      reader.readLine match {
        case null => ""
        case l if l.trim().size == 0 => readLine
        case l => l
      }
    }

    var in = readLine
    while (in != "") {
      val tokenizedStr = t.tokenize(in)
    }

  }*/
}

object JukaiNLP {
  def main(args: Array[String]): Unit = {
    val filePath = args(0)
    val model = H5Util.loadData(filePath)
    val t = Tokenizer(model)
    val outputs = t.tokenize(args(1))
    println("Input String:")
    println(args(1))
    println("Tokenized:")
    println(outputs)
  }
}
