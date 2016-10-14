package jukaiScala.main

import jukaiScala.hdflib._

/**
  * Created by ubuntu on 10/14/16.
  */
class JukaiNLP {

}

object JukaiNLP {
  def main(args: Array[String]): Unit = {
    println("hello")
    val fid = H5Util.openFile(args(0))
    println(fid)
  }
}
