package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
class kerasSpec extends FlatSpec with Matchers {

  "kerasModel" should "load network" in {

    val filePath = "./target/test-classes/data/keras_testmodel.h5"

    1 should be (1)
  }
}
