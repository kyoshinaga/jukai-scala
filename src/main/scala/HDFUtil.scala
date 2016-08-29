package jukaiScala

/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

import ncsa.hdf.hdf5lib.H5._
import ncsa.hdf.hdf5lib.HDF5Constants._
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception
import org.slf4j.LoggerFactory
import java.io.File

import org.apache.spark.mllib.linalg.distributed.IndexedRowMatrix
import org.apache.spark.mllib.linalg.distributed.IndexedRow
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.SparkContext
import scala.collection.immutable.NumericRange

object HDFUtil {
  private def getDimentions(file: String, variable: String): (Array[Long], Int) = {
    val logger = LoggerFactory.getLogger(getClass)
    var file_id = -2
    var dataset_id = -2
    var dataspace_id = -2
    try {
      file_id = H5Fopen(file, H5F_ACC_RDONLY, H5P_DEFAULT)
      dataset_id = H5Dopen(file_id, variable, H5P_DEFAULT)
    }
    catch {
      case e: Exception => logger.info("\nFile error: " + file)
    }
    dataspace_id = H5Dget_space(dataset_id)
    val ranks = H5Sget_simple_extent_ndims(dataspace_id)
    val dset_dims = new Array[Long](ranks)
    H5Sget_simple_extent_dims(dataspace_id, dset_dims, null)

    H5Sclose(dataspace_id)
    H5Dclose(dataset_id)
    H5Fclose(file_id)
    (dset_dims, ranks)
  }

  private def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

  private def read_whole_dataset(FILENAME: String, DATASETNAME: String): (Array[Array[Double]]) = {
    val logger = LoggerFactory.getLogger(getClass)
    var file_id = -2
    var dataset_id = -2
    var dataspace_id = -2
    var dset_dims = new Array[Long](2)
    dset_dims = Array(1,1)

    try {
      file_id = H5Fopen(FILENAME, H5F_ACC_RDONLY, H5P_DEFAULT)
    }
    catch {
      case e: Exception => logger.info("\nFile open error, filename:" + FILENAME)
    }

    try {
      dataset_id = H5Dopen(file_id, DATASETNAME, H5P_DEFAULT)
    }
    catch {
      case e: Exception => logger.info("\nDataset open error: " + DATASETNAME)
    }
    try {
      dataspace_id = H5Dget_space(dataset_id)
      H5Sget_simple_extent_dims(dataspace_id, dset_dims, null)
    }
    catch {
      case e: Exception => logger.info("dataspace open error, dataspace_id: " + dataspace_id)
    }
    if (dset_dims(0) > 0 && dset_dims(1) > 0){
      val dset_data = Array.ofDim[Double](dset_dims(0).toInt, dset_dims(1).toInt)
      var dread_id = -1
      try {
        dread_id = H5Dread(dataset_id, H5T_NATIVE_DOUBLE,
          H5S_ALL, H5S_ALL,
          H5P_DEFAULT, dset_data)
      }
      catch {
        case e: java.lang.NullPointerException => logger.info("data object is null")
        case e@(_: HDF5LibraryException | _: HDF5Exception) =>
          logger.info("Error from HDF5 library|Failure in the data conersion. Read error info: " +
          e.getMessage + e.printStackTrace)
        case e: java.lang.NegativeArraySizeException => logger.info("emptyjavaarray" + e.getMessage + e.printStackTrace + FILENAME)
      }
      if (dread_id < 0)
        logger.info("Dataset open error" + FILENAME)
      dset_data
    }
    else {
      val dset_data = Array.ofDim[Double](1, 1)
      logger.info("file empty" + FILENAME)
      dset_data
    }
  }

  private def read_hyperslab(FILENAME: String, DATASETNAME: String, start: Long, end: Long): (Array[Double], Array[Long]) = {
    val logger = LoggerFactory.getLogger(getClass)
    var file_id = -2
    var dataset_id = -2
    var dataspace_id = -2
    var endl = end
    try {
      file_id = H5Fopen(FILENAME, H5F_ACC_RDONLY, H5P_DEFAULT)
    }
    catch {
      case e: Exception => logger.info("\nFile error: " + FILENAME)
    }
    try {
      dataset_id = H5Dopen(file_id, DATASETNAME, H5P_DEFAULT)
    }
    catch {
      case e: Exception => logger.info("\nDataset error\n")
    }
    var (dset_dims: Array[Long], ranks: Int) = getDimentions(FILENAME, DATASETNAME)
    if (endl > dset_dims(0))
      endl = dset_dims(0)
    val step = endl - start
    var subset_length: Long = 1
    logger.info("Ranks=" + ranks)
    logger.info("Dim 0=" + dset_dims(0))
    for (i <- 1 to ranks - 1){
      subset_length *= dset_dims(i)
      logger.info("Dim " + i + "=" + dset_dims(i))
    }

    val dset_datas = Array.ofDim[Double](step.toInt * subset_length.toInt)

    val start_dims: Array[Long] = new Array[Long](ranks)
    val count_dims: Array[Long] = new Array[Long](ranks)
    start_dims(0) = start.toInt
    count_dims(0) = step.toInt
    for (i <- 1 to ranks - 1) {
      start_dims(i) = 0.toLong
      count_dims(i) = dset_dims(i)
    }

    var hyper_id = -2
    var dread_id = -2
    var memspace = -2
    H5Sclose(dataspace_id)
    try {
      dataspace_id = H5Dget_space(dataset_id)
      memspace = H5Screate_simple(ranks, count_dims, null)
      hyper_id = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start_dims, null, count_dims, null)
      dread_id = H5Dread(dataset_id, H5T_NATIVE_DOUBLE, memspace, dataspace_id, H5P_DEFAULT, dset_datas)
    }
    catch {
      case e: java.lang.NullPointerException => logger.info("data object is null")
      case e@(_: HDF5LibraryException | _:HDF5Exception) =>
        logger.info("Error from HDF5 library|Failuer in the data conversion. Read error info : " +
           e.getMessage + e.printStackTrace)
    }
    var global_start = (start - 1) * subset_length
    if (global_start < 0) global_start = 0
    var global_end = endl * subset_length
    import Array._
    val index: Array[Long] = NumericRange(global_start, global_end, 1L).toArray
    (dset_datas, index)
  }

  private def read_array(FILENAME: String, DATASETNAME: String, start: Long, end: Long): (Array[Array[Double]]) = {
    var endl = end
    var (dset_dims: Array[Long], ranks: Int) = getDimentions(FILENAME, DATASETNAME)
    if (endl > dset_dims(0))
      endl = dset_dims(0)
    var subset_length: Long = 1
    for (i <- 1 to ranks - 1){
      subset_length *= dset_dims(i)
    }
    var dset_data: Array[Array[Double]] = Array.ofDim((endl - start).toInt, subset_length.toInt)
    var (dset_datas: Array[Double], index: Array[Long]) = read_hyperslab(FILENAME, DATASETNAME, start, end)
    for (id <- 0 to (endl - start).toInt - 1){
      for (jd <- 0 to subset_length.toInt - 1){
        dset_data(id)(jd) = dset_datas(id * subset_length.toInt + jd)
      }
    }
    dset_data
  }

  def h5read_point(sc: SparkContext, inpath: String, variable: String, partitions: Long): RDD[(Double, Long)] = {
    val file = new File(inpath)
    val logger = LoggerFactory.getLogger(getClass)
    if (file.exists && file.isFile){
      logger.info("Read Single file: " + inpath)
      val (dims: Array[Long], ranks:Int) = getDimentions(inpath, variable)
      val rows: Long = dims(0)
      var num_partitions: Long = partitions
      if (rows < num_partitions) {
        num_partitions = rows
      }
      val step: Long = rows / num_partitions
      val arr = sc.range(0, rows, step, partitions.toInt).flatMap(x => {
        var data_array = read_hyperslab(inpath, variable, x, x + step)
        (data_array._1 zip data_array._2)
      })
      arr
    }
    else {
      val okext = List("h5", "hdf5")
      val listf = getListOfFiles(file, okext)
      logger.info("Read" + listf.length + "files from directory:" + inpath)
      val arr = sc.parallelize(listf, partitions.toInt).map(x => x.toString).flatMap(x => {
        var data_array = read_hyperslab(inpath, variable, 0, 1e100.toInt)
        (data_array._1 zip data_array._2)
      })
      arr
    }
  }

  def h5read_array(sc: SparkContext, inpath: String, variable: String, partitions: Long): RDD[Array[Double]] = {
    val file = new File(inpath)
    val logger = LoggerFactory.getLogger(getClass)
    if (file.exists && file.isFile) {
      logger.info("Read Single file:" + inpath)

      val (dims: Array[Long], ranks: Int) = getDimentions(inpath, variable)
      val rows: Long = dims(0)
      var num_partitions: Long = partitions
      if (rows < num_partitions) {
        num_partitions = rows
      }
      val step: Long = rows / num_partitions
      val arr = sc.range(0, rows, step, partitions.toInt).flatMap( x =>
        read_array(inpath, variable, x, x + step))
      arr
    }
    else {
      val okext = List("h5", "hdf5")
      val listf = getListOfFiles(file, okext)
      logger.info("Read" + listf.length + " files from directory: " + inpath)
      val arr = sc.parallelize(listf, partitions.toInt).map( x => x.toString).flatMap(x => read_whole_dataset(x, variable))
      arr
    }
  }

  def h5read_vec(sc: SparkContext, inpath: String, variable: String, partitions:Long): RDD[DenseVector] = {
    val arr = h5read_array(sc, inpath, variable, partitions).map {
      case a: Array[Double] =>
        new DenseVector(a)
    }
    arr
  }

  def h5read_irow(sc: SparkContext, inpath: String, variable: String, partitions: Long): RDD[IndexedRow] = {
    val irow = h5read_vec(sc, inpath, variable, partitions).zipWithIndex().map(k =>
      (k._1, k._2)
    ).map(k =>
      new IndexedRow(k._2, k._1)
    )
    irow
  }

  def h5read_imat(sc: SparkContext, inpath: String, variable: String, partitions: Long): IndexedRowMatrix = {
    val irow = h5read_irow(sc,inpath, variable, partitions)
    val imat = new IndexedRowMatrix(irow)
    imat
  }
}
