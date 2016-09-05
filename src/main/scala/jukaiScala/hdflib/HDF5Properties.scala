package jukaiScala.hdflib

import jukaiScala.hdflib.HDF5Const._

/**
  * Created by kenta-yoshinaga on 2016/09/02.
  */
class HDF5Properties(id:Int, toclose:Boolean=true) {
  type Self = HDF5Properties

  def this() = this(H5P_DEFAULT)
}
