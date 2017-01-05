package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import ucar.nc2.NetcdfFile
import ucar.nc2.{Variable, Attribute, Group}

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class KerasModel(path:String) {

  val model = NetcdfFile.open(path, null)

  val rootGroup = model.getRootGroup

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

  val weightGroups = checkAndGetGroup("model_weights")

  def parseConfigToList(config: String): List[Map[String, Any]] = {
    val jsonValue = parse(config)
    implicit val formats = DefaultFormats
    val jsonList = jsonValue.extract[Map[String, Any]]
    jsonList("config").asInstanceOf[List[Map[String, Any]]]
  }

  val modelValues = parseConfigToList(modelAttribute.getValue(0).toString)

  def constructNetwork(values: List[Map[String, Any]]) = values.map(
    x => x("class_name") match{
      case "Activation" => {
        x("config").asInstanceOf[Map[String, String]]("activation") match{
          case "relu" => Relu
          case "softmax" => Softmax
        }
      }
      case "Convolution1D" => Dense(10,5)
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

  val graphSsplit = constructNetwork(modelValues)

}

object KerasModel{
  def apply(path:String): KerasModel = new KerasModel(path)
}