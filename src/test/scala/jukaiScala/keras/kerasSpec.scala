package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
import ucar.nc2.NetcdfFile
import ucar.nc2.Variable
import ucar.ma2._
import java.io.IOException

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class kerasSpec extends FlatSpec with Matchers {

  "kerasModel" should "load network" in {

    val filePath = "./target/test-classes/data/keras_testmodel.h5"

    val dataFile = NetcdfFile.open(filePath, null)

    val rootGroup = dataFile.getRootGroup()

    val rootAttributes = rootGroup.getAttributes()

    println(rootAttributes.size())

    if (rootAttributes.size() > 0){
      println("This is keras.")
    }else{
      println("Merlin")
    }

    val childGroups = rootGroup.findGroup("model_weights")

    println(childGroups.getAttributes())

    val modelConfig = rootAttributes.get(1).getValues().getObject(0)

    val jsonValue = parse(modelConfig.toString())
    implicit val formats = DefaultFormats
    val jsonList = jsonValue.extract[Map[String, Any]]


    1 should be (1)
  }
}
