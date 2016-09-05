package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/09/02.
  */
class HDF5Properties(id:Int, toclose:Boolean=true) extends HDF5Const {
  type Self = HDF5Properties

  def this() = this(H5P_DEFAULT)
}
