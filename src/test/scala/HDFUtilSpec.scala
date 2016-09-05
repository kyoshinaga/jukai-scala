/**
  * Created by kenta-yoshinaga on 2016/08/25.
  */

package jukaiScala.main

import org.scalatest._

class HDFUtilSpec extends FlatSpec with Matchers {

  def findPath(localPath: String) = getClass.getClassLoader.getResource(localPath).getPath

  "load" should "can read HDF5 file" in {

    val filePath = findPath("./data/hdf5.h5")

    1 should be (1)
  }

}
