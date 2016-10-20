package jukaiScala.merlin

import org.scalatest._
import breeze.linalg.{DenseMatrix, DenseVector}

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
class EmbeddingSpec extends FlatSpec with Matchers{

  "convert" should "can looking up and return matrix" in {
    val embed = Embedding(100, 4)
    val sampleData = DenseMatrix(Array[Double](2,3,4))
    println(sampleData)
    println(embed.getFunctorName())
    println(embed.w(2))
    println(embed.w(3))
    println(embed.w(4))
    println(embed.convert(sampleData))
    1 should be (1)
  }

}
