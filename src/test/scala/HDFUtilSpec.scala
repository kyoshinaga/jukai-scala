/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

package jukaiScala.Main

import org.scalatest._
import jukaiScala.hdflib._
import hdf.hdf5lib.H5
import hdf.hdf5lib.HDF5Constants
import hdf.hdf5lib.callbacks.H5P_prp_delete_func_cb

class HDFUtilSpec extends FlatSpec with Matchers {

  def findPath(localPath: String) = getClass.getClassLoader.getResource(localPath).getPath

  "load" should "can read HDF5 file" in {

    val testString = "本日は晴れです。"

    val filePath = findPath("./data/tokenizer_test.h5")
    val outputFilePath = findPath("./data/")

    val fid = H5Util.openFile(filePath)
    println("fid: %d".format(fid))

    val ginfo = H5.H5Gget_info(fid)

    println(ginfo.nlinks)

    for(i <- 0 until ginfo.nlinks.asInstanceOf[Int]){
      val gname = H5.H5Lget_name_by_idx(fid, "/", HDF5Constants.H5_INDEX_NAME,
        HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT)
      println(gname)
      val oi = H5.H5Oget_info_by_name(fid,gname,HDF5Constants.H5P_DEFAULT)
      oi.`type` match {
        case HDF5Constants.H5O_TYPE_GROUP =>
          val gid = H5.H5Gopen(fid, gname, HDF5Constants.H5P_DEFAULT)
          val subginfo = H5.H5Gget_info(gid)
          println("\tGroup %s".format(gname))
          val nlinks = subginfo.nlinks.asInstanceOf[Int]
          for (j <- 0 until nlinks) {
            val subgname = H5.H5Lget_name_by_idx(fid, gname, HDF5Constants.H5_INDEX_NAME,
              HDF5Constants.H5_ITER_INC, j, HDF5Constants.H5P_DEFAULT)
            println("\t%s".format(subgname))
          }
        case HDF5Constants.H5O_TYPE_DATASET =>
          println("dataset")
          val did = H5.H5Dopen(fid, gname, HDF5Constants.H5P_DEFAULT)
          val tid = H5.H5Dget_type(did)
          val buff = H5Util.readData(did,tid)
          val str = new String(buff)
          println("\t%s".format(str))
      }
    }

    val gname = H5.H5Lget_name_by_idx(fid, "/", HDF5Constants.H5_INDEX_NAME,
      HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT)
    val gid = H5.H5Gopen(fid, gname, HDF5Constants.H5P_DEFAULT)
    val iddictid = H5.H5Gopen(gid, "iddict", HDF5Constants.H5P_DEFAULT)
    val did = H5.H5Dopen(iddictid, "id2count", HDF5Constants.H5P_DEFAULT)
    val tid = H5.H5Dget_type(did)
    val dsid = H5.H5Dget_space(did)

    H5Util.closeFile(fid)

    // val ffid =

    1 should be (1)
  }
}