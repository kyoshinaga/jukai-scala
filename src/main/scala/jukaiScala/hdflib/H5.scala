package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */

import java.io._
import jukaiScala.hdflib.HDF5Const._
import jukaiScala.hdflib._

object H5 {

  val DEFAULT_CONST = new HDF5Properties(H5P_DEFAULT, false)

  def open(filename: String, rd: Boolean, wr: Boolean,
           cr: Boolean, tr: Boolean, ff: Boolean,
           cpl: HDF5Properties = DEFAULT_CONST,
           apl: HDF5Properties = DEFAULT_CONST): Unit =
  {
    if(ff & !wr) {
      System.err.println("HDF5 does not support appending without writeing")
    }
    val close_apl: Boolean = false
    println("Open file\n")
  }
}
