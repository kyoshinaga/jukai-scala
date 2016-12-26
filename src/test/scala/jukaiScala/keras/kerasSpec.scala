package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
import ucar.nc2.NetcdfFile
import ucar.nc2.Variable
import ucar.ma2._

import java.io.IOException

class kerasSpec extends FlatSpec with Matchers {

  "kerasModel" should "load network" in {

    val filePath = "./target/test-classes/data/keras_testmodel.h5"

    val dataFile = NetcdfFile.open(filePath, null)

    1 should be (1)
  }
}
