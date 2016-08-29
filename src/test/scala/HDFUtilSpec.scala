/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

package jukaiScala

import org.scalatest._
import jukaiScala.HDFUtil._
import ncsa.hdf.hdf5lib.H5._
import ncsa.hdf.hdf5lib.HDF5Constants._
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception

class HDFUtilSpec extends FlatSpec with Matchers {

  def findPath(localPath: String) = getClass.getClassLoader.getResource(localPath).getPath

  "load" should "can read HDF5 file" in {
    val file_id = H5Fopen(findPath("./data/hdf5.h5"), H5F_ACC_RDONLY, H5P_DEFAULT)

    println("Fild ID: " + file_id)

    1 should be (1)
  }

}
