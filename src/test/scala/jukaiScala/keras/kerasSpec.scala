package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import hdf.hdf5lib.callbacks.H5P_prp_delete_func_cb
import hdf.hdf5lib.{H5, HDF5Constants}
import jukaiScala.hdflib.H5Util
import org.scalatest.{FlatSpec, Matchers}
class kerasSpec extends FlatSpec with Matchers {

  "kerasModel" should "load network" in {

    val filePath = "./target/test-classes/data/keras_testmodel.h5"

    val fid = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT)

    val objectInfo = H5.H5Oget_info_by_name(fid, "/", HDF5Constants.H5P_DEFAULT)

    // Get name by index
    val aname = H5.H5Aget_name_by_idx(fid,"/",HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT)

    // Attribution information
    val attrInfo = H5.H5Aget_info_by_name(fid, "/", aname, HDF5Constants.H5P_DEFAULT)

    // Location id of attribution
    val aid = H5.H5Aopen(fid, aname, HDF5Constants.H5P_DEFAULT)

    // Location id of dataspace in attribution
    val adsid = H5.H5Aget_space(aid)
    val ndim = H5.H5Sget_simple_extent_ndims(adsid)
    val dims = new Array[Long](ndim)
    H5.H5Sget_simple_extent_dims(adsid, dims, null)
    val tid = H5.H5Aget_type(aid)
    val npoints = H5.H5Sget_simple_extent_npoints(adsid)

    val buff = new Array[String](npoints.toInt)

    println(H5.H5Tget_class(tid))
    println(HDF5Constants.H5T_STRING)

    println(npoints)

    println(aname)
    println(attrInfo)

    1 should be (1)
  }
}
