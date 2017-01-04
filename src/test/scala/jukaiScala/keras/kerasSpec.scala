package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
import ucar.nc2.NetcdfFile
import ucar.nc2.{Variable, Attribute, Group}
import ucar.ma2._
import java.io.IOException

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class kerasSpec extends FlatSpec with Matchers {
/*
  "kerasModel" should "load linear-network" in {

    val filePath = "./target/test-classes/data/keras_sample.h5"

    val dataFile = NetcdfFile.open(filePath, null)

    val rootGroup = dataFile.getRootGroup()

    val rootAttributes = rootGroup.getAttributes()


    def checkAndGetAttribute(name: String): Attribute = Option(rootGroup.findAttribute(name)) match {
      case Some(x) => x
      case None => throw new IllegalArgumentException("cannot get " + name + " attribute from input model file")
    }

    def checkAndGetGroup(name: String): Group = Option(rootGroup.findGroup(name)) match {
      case Some(x) => x
      case None => throw new IllegalArgumentException("cannot get " + name + " group from input model file")
    }

    val kerasAttribute = checkAndGetAttribute("keras_version")
    val modelAttribute = checkAndGetAttribute("model_config")

    val jsonValue = parse(modelAttribute.getValue(0).toString)
    implicit val formats = DefaultFormats
    val jsonList = jsonValue.extract[Map[String, Any]]

    val configList = jsonList("config").asInstanceOf[List[Map[String, Any]]]

    println("")
    println("Total layers")
    println(configList.size)
    println("")

    val weightGroups = checkAndGetGroup("model_weights")

    def constNetwork(values: List[Map[String, Any]]) = values.map(
      x => x("class_name") match{
        case "Activation" => {
          x("config").asInstanceOf[Map[String, String]]("activation") match{
            case "relu" => Relu
            case "softmax" => Softmax
          }
        }
        case "Dense" => {
          val layerName = x("config").asInstanceOf[Map[String, String]]("name")
          val params = weightGroups.findGroup(layerName)
          val weight = params.findVariable(layerName + "_W:0")
          val bias   = params.findVariable(layerName + "_b:0")
          Dense(weight, bias)
        }
        case "Flatten" => Flatten
      }
    )

    val testNetwork = constNetwork(configList)


    1 should be (1)
  }
*/
  "kerasModel" should "load conv-network" in {

    val filePath = "./target/test-classes/data/keras_sample_conv.h5"

    val dataFile = NetcdfFile.open(filePath, null)

    val rootGroup = dataFile.getRootGroup()

    val rootAttributes = rootGroup.getAttributes()


    def checkAndGetAttribute(name: String): Attribute = Option(rootGroup.findAttribute(name)) match {
      case Some(x) => x
      case None => throw new IllegalArgumentException("cannot get " + name + " attribute from input model file")
    }

    def checkAndGetGroup(name: String): Group = Option(rootGroup.findGroup(name)) match {
      case Some(x) => x
      case None => throw new IllegalArgumentException("cannot get " + name + " group from input model file")
    }

    val kerasAttribute = checkAndGetAttribute("keras_version")
    val modelAttribute = checkAndGetAttribute("model_config")

    val jsonValue = parse(modelAttribute.getValue(0).toString)
    implicit val formats = DefaultFormats
    val jsonList = jsonValue.extract[Map[String, Any]]

    val configList = jsonList("config").asInstanceOf[List[Map[String, Any]]]

    println("")
    println("Total layers")
    println(configList.size)
    println("")

    val weightGroups = checkAndGetGroup("model_weights")

    def constNetwork(values: List[Map[String, Any]]) = values.map(
      x => x("class_name") match{
        case "Activation" => {
          x("config").asInstanceOf[Map[String, String]]("activation") match{
            case "relu" => Relu
            case "softmax" => Softmax
          }
        }
        case "Dense" => {
          val layerName = x("config").asInstanceOf[Map[String, String]]("name")
          val params = weightGroups.findGroup(layerName)
          val weightNames = params.findAttribute("weight_names")
          val weight = params.findVariable(weightNames.getStringValue(0))
          val bias   = params.findVariable(weightNames.getStringValue(1))
          Dense(weight, bias)
        }
        case "Flatten" => Flatten
        case _ => Dense(10,2)
      }
    )

    val testNetwork = constNetwork(configList)

    println(testNetwork(0).toString)

    val lname = configList(0)("config").asInstanceOf[Map[String, String]]("name")

    val pr = weightGroups.findGroup(lname)
    val weightNames = pr.findAttribute("weight_names")

    println(weightNames.getStringValue(0))
    println(weightNames.getStringValue(1))

    1 should be (1)
  }
}
