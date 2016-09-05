package jukaiScala.main

import breeze.linalg._

class JukaiScala

object JukaiScala {
  def main(args: Array[String]): Unit = {
    println("hello")
    val x = DenseVector.zeros[Double](5)
    println(x)
    println(Transpose(x))
  }
}