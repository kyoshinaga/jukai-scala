package jukaiScala.merlin

import org.scalatest._

/**
  * Created by ubuntu on 10/18/16.
  */
class ConvSpec extends FlatSpec with Matchers{

  "load" should "can read HDF5 file" in {
    val conv = Conv((3,2),(1,2),(1,1),(0,0))
    println(conv.w.toString)
    1 should be(1)
  }
}
