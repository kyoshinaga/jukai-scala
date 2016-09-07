package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */

import java.io._

import hdf.hdf5lib.H5
import hdf.hdf5lib.HDF5Constants

object H5Util {
  def openFile(filePath:String): Int = H5.H5Fcreate(filePath, HDF5Constants.H5F_ACC_TRUNC,
    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT)

  def closeFile(fid:Int) :Unit = H5.H5Fclose(fid)
}
