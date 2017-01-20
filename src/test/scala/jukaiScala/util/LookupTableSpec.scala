package jukaiScala.util

/**
  * Created by kenta-yoshinaga on 2017/01/17.
  */

import breeze.linalg.DenseMatrix
import org.scalatest.{FlatSpec, Matchers}

class LookupTableSpec extends FlatSpec with Matchers {

  "decode" should "return matrix" in {

    val lookup = LookupTable("./target/test-classes/data/jpnLookup.json")

    val str = "これはサンプルです。hogehoge。⊿."

    val encStr = lookup.encode(str)

    encStr shouldBe a [DenseMatrix[Double]]
  }

}
