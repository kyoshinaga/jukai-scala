/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

package jukaiScala.hdflib

import hdf.hdf5lib.{H5, HDF5Constants}
import org.scalatest._

class HDFUtilSpec extends FlatSpec with Matchers {

  def findPath(localPath: String) = getClass.getClassLoader.getResource(localPath).getPath

  "load" should "can read HDF5 file" in {
    val filePath = "./target/test-classes/data/tokenizer_test.h5"
    val fid = H5Util.openFile(filePath)

    val ginfo = H5.H5Gget_info(fid)

    for (i <- 0 until ginfo.nlinks.asInstanceOf[Int]) {
      val gname = H5.H5Lget_name_by_idx(fid, "/", HDF5Constants.H5_INDEX_NAME,
        HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT)
      val oi = H5.H5Oget_info_by_name(fid, gname, HDF5Constants.H5P_DEFAULT)
      oi.`type` match {
        case HDF5Constants.H5O_TYPE_GROUP =>
          val gid = H5.H5Gopen(fid, gname, HDF5Constants.H5P_DEFAULT)
          val subginfo = H5.H5Gget_info(gid)
          println("\tGroup %s".format(gname))
          val nlinks = subginfo.nlinks.asInstanceOf[Int]
          for (j <- 0 until nlinks) {
            val subgname = H5.H5Lget_name_by_idx(fid, gname, HDF5Constants.H5_INDEX_NAME,
              HDF5Constants.H5_ITER_INC, j, HDF5Constants.H5P_DEFAULT)
            println("\t\t%s".format(subgname))
          }
        case HDF5Constants.H5O_TYPE_DATASET =>
          println("\tDataset %s".format(gname))
          val did = H5.H5Dopen(fid, gname, HDF5Constants.H5P_DEFAULT)
          val tid = H5.H5Dget_type(did)
      }
    }

    val gname = H5.H5Lget_name_by_idx(fid, "/", HDF5Constants.H5_INDEX_NAME,
      HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT)
    val gid = H5.H5Gopen(fid, gname, HDF5Constants.H5P_DEFAULT)
    val iddictid = H5.H5Gopen(gid, "iddict", HDF5Constants.H5P_DEFAULT)
    val did = H5.H5Dopen(iddictid, "id2count", HDF5Constants.H5P_DEFAULT)
    val did2 = H5.H5Dopen(iddictid, "#TYPE", HDF5Constants.H5P_DEFAULT)
    val tid2 = H5.H5Dget_type(did2)
    val dataTypeClass = H5.H5Tget_class(tid2)

    println(HDF5Constants.H5T_STRING)
    println(dataTypeClass)

    val data = H5Util.readData(did)
    val data2 = H5Util.readData(did2)

    println(data2.map(_.asInstanceOf[Byte].toChar).mkString(""))

    val did3 = H5.H5Gopen(iddictid, "key2id", HDF5Constants.H5P_DEFAULT)
    val dinfo = H5.H5Gget_info(did3)
    println(dinfo.nlinks.asInstanceOf[Int])
    val dname = H5.H5Lget_name_by_idx(iddictid, "key2id", HDF5Constants.H5_INDEX_NAME,
      HDF5Constants.H5_ITER_INC, 1000, HDF5Constants.H5P_DEFAULT)
    val did4 = H5.H5Dopen(did3, dname, HDF5Constants.H5P_DEFAULT)
    val data4 = H5Util.readData(did4)

    println(data4.mkString(""))

    println(dname)

    val glist = H5Util.getNameTypeList(fid, fid, "/")
    println(glist.mkString(","))

    val modelid = H5.H5Gopen(gid, "model", HDF5Constants.H5P_DEFAULT)
    val embid = H5.H5Gopen(modelid, "12", HDF5Constants.H5P_DEFAULT)
    val typeid = H5.H5Gopen(embid, "1", HDF5Constants.H5P_DEFAULT)
    val dataid = H5.H5Dopen(typeid, "w", HDF5Constants.H5P_DEFAULT)
    val tid = H5.H5Dget_type(dataid)
    val dsid = H5.H5Dget_space(dataid)
    val dataClass = H5.H5Tget_class(tid)
    val ndim = H5.H5Sget_simple_extent_ndims(dsid)
    val npoint = H5.H5Sget_simple_extent_npoints(dsid)
    val dimsArray = new Array[Long](2)
    val dimsmax = new Array[Long](2)
    val dims = H5.H5Sget_simple_extent_dims(dsid, dimsArray, dimsmax)
    val buff = new Array[Float](npoint.toInt)
    println(ndim)
    println(dimsArray.mkString(","))
    println(dimsmax.mkString(","))
    H5.H5Dread(dataid, tid, dsid, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buff)
    println(buff.mkString(","))
    val elem = buff(3 * dimsArray(1).toInt + 0)
    println("access to 3,0: %f".format(elem))

    val elemDummy = H5Elem("test","test")
    val elemDummy2 = H5Elem("test2","test")

    val dummyData = List[Int](1,2,3)
    val dummyDataSet = new H5DataSet[List[Int]](dummyData, label="hoge")

    val elems = H5Elem("test", "test", dummyDataSet)

    println(elems.label)
    println(elems.hasChild)
    println(elems.child(0).label)

    H5Util.closeFile(fid)

//    val rootElem = H5Util.loadData(filePath)

    1 should be(1)
  }
}