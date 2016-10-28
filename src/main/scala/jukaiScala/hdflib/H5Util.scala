package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */

import java.io._

import hdf.hdf5lib.H5
import hdf.hdf5lib.HDF5Constants
import hdf.hdf5lib.callbacks.H5P_prp_delete_func_cb

import scala.collection.immutable.IndexedSeq

object H5Util {

//  def loadData(filePath: String): H5Elem = {
//    val fid = openFile(filePath)
//    val nameList = getNameTypeList(fid, fid, "/")
//    val elemSeq = nameList.map(x => recursiveLoad(fid,x._1,x._2))
//    val rootElem = H5Elem("ROOT", "Root", elemSeq: _*)
//    rootElem
//  }
//
//  def recursiveLoad(locid: Int, name: String, dataType: Int): H5Elem = dataType match{
//    case HDF5Constants.H5O_TYPE_GROUP => {
//      val gid = H5.H5Gopen(locid, name, HDF5Constants.H5P_DEFAULT)
//      val gnameList = getNameTypeList(locid, gid, name)
//      val elemSeq = gnameList.map(x => recursiveLoad(gid, x._1, x._2))
//      H5Elem(name,"Group", elemSeq: _*)
//    }
//    case HDF5Constants.H5O_TYPE_DATASET => {
//      val did = H5.H5Dopen(locid, name, HDF5Constants.H5P_DEFAULT)
//      val data = readData(did)
//      val text = data.map(_.asInstanceOf[Byte].toChar).mkString("")
//      H5Elem(name,"DataSet")
//    }
//  }

  // File
  def openFile(filePath:String): Int = {
    H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDONLY,HDF5Constants.H5P_DEFAULT)
  }

  def closeFile(fid:Int): Unit = H5.H5Fclose(fid)

  def readData(did: Int): Array[_ >: Long with Byte <: AnyVal] = {
    val tid = H5.H5Dget_type(did)
    val dsid = H5.H5Dget_space(did)
    val dataTypeClass = H5.H5Tget_class(tid)
    val data = dataTypeClass match {
      case HDF5Constants.H5T_INTEGER => {
        val ndim = H5.H5Sget_simple_extent_ndims(dsid)
        val dims = new Array[Long](ndim)
        val dimsmax = new Array[Long](ndim)
        val npoints = H5.H5Sget_simple_extent_npoints(dsid)
        val buff = new Array[Long](npoints.toInt)
        H5.H5Dread(did, tid, dsid, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buff)
        buff
      }
      //case HDF5Constants.H5T_FLOAT => {
//
 //     }
      case HDF5Constants.H5T_STRING => {
        val size = H5.H5Tget_size(tid)
        val buff =  new Array[Byte](size)
        H5.H5Dread(did, tid, dsid, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buff)
        buff
      }
    }
    data
  }

  def getNameTypeList(fid:Int, locid:Int, rootName: String): List[(String, Int)] = {
    val gInfo = H5.H5Gget_info(locid)
    val nlink = gInfo.nlinks.toInt
    val gnameList = for {
      i <- 0 until nlink
      gname = H5.H5Lget_name_by_idx(fid, rootName, HDF5Constants.H5_INDEX_NAME,
        HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT)
      oi = H5.H5Oget_info_by_name(locid, gname, HDF5Constants.H5P_DEFAULT)
      nameAndType = gname -> oi.`type`
    } yield nameAndType
    gnameList.toList
  }
}
