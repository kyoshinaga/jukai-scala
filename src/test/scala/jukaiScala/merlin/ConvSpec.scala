package jukaiScala.merlin

import org.scalatest._
import breeze.linalg.DenseMatrix

/**
  * Created by ubuntu on 10/18/16.
  */
class ConvSpec extends FlatSpec with Matchers{

  "load" should "can read HDF5 file" in {
    val sampleData = DenseMatrix((1.0,2.0,3.0),(4.0,5.0,6.0),(7.0,8.0,9.0))
    val conv = Conv((3,3),(1,2),(1,1),(0,1))
    for(i <- 0 until 2){
      val m = DenseMatrix(
        (1.0+i*10, 2.0+i*10, 3.0+i*10),
        (4.0+i*10, 5.0+i*10, 6.0+i*10),
        (7.0+i*10, 8.0+i*10, 9.0+i*10))
      conv.w(0)(i) = m
    }

    val conved = conv.convert(sampleData)

    println(conved)

    1 should be(1)
  }
}
