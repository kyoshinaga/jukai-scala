package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */

import java.io._

import org.saddle._
import org.saddle.io.H5Store
import org.saddle.time._

object H5Util {
  def openFile(filePath:String): Int = H5Store.createFile(filePath)

  def closeFile(fid:Int) :Unit = H5Store.closeFile(fid)
}
