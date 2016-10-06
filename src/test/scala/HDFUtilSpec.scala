/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

package jukaiScala.main

import org.scalatest._

import jukaiScala.hdflib._

import hdf.hdf5lib.H5
import hdf.hdf5lib.HDF5Constants

import scala.collection.mutable.StringBuilder

class HDFUtilSpec extends FlatSpec with Matchers {

  def findPath(localPath: String) = getClass.getClassLoader.getResource(localPath).getPath

  "load" should "can read HDF5 file" in {

    val filePath = findPath("./data/tokenizer_20161006.h5")
    val outputFilePath = findPath("./data/")

    val fid = H5Util.openFile(filePath)
    println("fid: %d".format(fid))
    val gid = H5Util.openGroup(fid, "Merlin")
    println("Merlin group id: %d".format(gid))
    val datatypeId = H5Util.openDataset(gid, "#TYPE")
    println("Data type id : %d".format(datatypeId))
    val tid = H5.H5Dget_type(datatypeId)
    val sid = H5.H5Dget_space(datatypeId)
    val stringLength = H5.H5Tget_size(tid)
    val buffersize = stringLength * 2
    val buff = new Array[Byte](buffersize)

    println(stringLength)
    H5.H5Dread(datatypeId, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
      HDF5Constants.H5P_DEFAULT, buff)

    val str = new String(buff)
    println(str)
    H5Util.closeFile(fid)

    H5Util.createFile("%s/test.h5".format(outputFilePath))

    val fileId = H5Util.openFile("%s/test.h5".format(outputFilePath))
    val datasetId = H5Util.openDataset(fileId,"2D 32-bit integer 20x10")
    val dataRead = Array.ofDim[Long](20 * 10)

    H5.H5Dread(datasetId,HDF5Constants.H5T_NATIVE_INT,
      HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
      HDF5Constants.H5P_DEFAULT, dataRead)

    H5Util.closeDataset(datasetId)
    H5Util.closeFile(fileId)

    1 should be (1)
  }
}