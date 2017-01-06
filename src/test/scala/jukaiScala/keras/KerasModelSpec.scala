package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
import ucar.nc2.NetcdfFile
import ucar.nc2.{Attribute, Group, Variable}
import ucar.ma2._
import java.io.IOException

import breeze.linalg.DenseMatrix
import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class KerasModelSpec extends FlatSpec with Matchers {
  "kerasModel" should "load linear-network" in {

    val filePath = "./target/test-classes/data/keras_sample.h5"

    val model = KerasModel(filePath)

    model.graph.foreach(x => println(x.toString))

    1 should be (1)
  }
}
