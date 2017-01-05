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

    def getConfigs(x: Map[String, Any]):Map[String, String] = x("config").asInstanceOf[Map[String, String]]

    def constNetwork(values: List[Map[String, Any]]) = values.map(
      x =>
        x("class_name") match{
        case "Activation" => {
          getConfigs(x)("activation") match{
            case "relu" => Relu
            case "softmax" => Softmax
          }
        }
        case "Dense" => {
          val layerName = getConfigs(x)("name")
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
    println(getConfigs(configList(0)))

    val lname = getConfigs(configList(0))("name")
    val borderMode = getConfigs(configList(0))("border_mode") match {
      case "same" => true
      case _ => false
    }

    println(borderMode)

    val pr = weightGroups.findGroup(lname)
    val weightNames = pr.findAttribute("weight_names")

    println(weightNames.getStringValue(0))

    val weightConv = pr.findVariable(weightNames.getStringValue(0))
    val dimsConv = weightConv.getDimensions
    println(dimsConv.get(0).getLength) // Width
    println(dimsConv.get(1).getLength) // Input Channel?
    println(dimsConv.get(2).getLength) // Input dim
    println(dimsConv.get(3).getLength) // Output Channel

    val conv = Convolution1D(2, 5, 2, padding = true)

    val m = DenseMatrix((1.0.toFloat, 2.0.toFloat), (3.0.toFloat, 4.0.toFloat), (5.0.toFloat, 6.0.toFloat))

    println(conv.w)
    println(conv.im2col(m))
    println(conv.im2col(m) * conv.w)

    println(conv.toString)

    1 should be (1)
  }
}
