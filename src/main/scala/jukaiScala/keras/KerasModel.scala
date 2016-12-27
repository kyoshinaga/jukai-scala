package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import ucar.nc2.NetcdfFile

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class KerasModel(path:String) {

  val model = NetcdfFile.open(path, null)

  val rootAttributes = model.getRootGroup.getAttributes

  if (rootAttributes.size() == 0)
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with illegal model file")

}

object KerasModel{
  def apply(path:String): KerasModel = new KerasModel(path)
}
