package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */

import java.io._

import hdf.hdf5lib.H5
import hdf.hdf5lib.HDF5Constants

object H5Util {

  // File
  def openFile(filePath:String): Int = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDONLY,
    HDF5Constants.H5P_DEFAULT)

  def closeFile(fid:Int): Unit = H5.H5Fclose(fid)

  // Group
  def openGroup(locationId:Int, groupName:String) = H5.H5Gopen(locationId, groupName,
    HDF5Constants.H5P_DEFAULT)

  def closeGroup(groupID:Int) = H5.H5Gclose(groupID)

  // Dataset
  def openDataset(locationId:Int, datasetName:String) = H5.H5Dopen(locationId, datasetName,
    HDF5Constants.H5P_DEFAULT)

  def closeDataset(datasetId:Int) = H5.H5Dclose(datasetId)

  def createFile(file:String): Unit = {
    val dims2D = Array[Long](20, 10)
    val dsname = "2D 32-bit integer 20x10"
    var file_id = -1
    var dataspace_id = -1
    var dataset_id = -1

    try {
      file_id = H5.H5Fcreate(file, HDF5Constants.H5F_ACC_TRUNC,
        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT)
    }
    catch {
      case e:Exception => System.err.println(e.getMessage)
    }

    try {
      dataspace_id = H5.H5Screate_simple(2, dims2D, null)
    }
    catch{
      case e:Exception => System.err.println(e.getMessage)
    }

    try{
      if((file_id >= 0) & (dataspace_id >= 0)){
        dataset_id = H5.H5Dcreate(file_id, dsname,
          HDF5Constants.H5T_STD_I32LE, dataspace_id,
          HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT,
          HDF5Constants.H5P_DEFAULT)
      }
    }
    catch{
      case e:Exception => System.err.println(e.getMessage)
    }
    try {
      if (dataspace_id >= 0)
        H5.H5Sclose(dataspace_id)
    }
    catch{
      case e:Exception => System.err.println(e.getMessage)
    }

    //var dataIn = scala.collection.mutable.Seq.fill[Long](20 * 10)(0)
    val dataIn = Array.ofDim[Long](20 * 10)

    for (i <- 0 until 20){
      for (j <- 0 until 10){
        dataIn(i * 10 + j) = i * 100 + j
      }
    }

    try{
      if (dataset_id >= 0)
        H5.H5Dwrite(dataset_id, HDF5Constants.H5T_NATIVE_INT,
          HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
          HDF5Constants.H5P_DEFAULT, dataIn)
    }
    catch{
      case e:Exception => System.err.println("H5DWrite Error: %s".format(e.getMessage))
    }
    try {
      if(dataset_id >= 0)
        H5.H5Dclose(dataset_id)
    }
    catch{
      case e:Exception => System.err.println(e.getMessage)
    }

    try {
      if (file_id >= 0)
        H5.H5Fclose(file_id)
    }
    catch{
      case e:Exception => System.err.println(e.getMessage)
    }
  }
}
