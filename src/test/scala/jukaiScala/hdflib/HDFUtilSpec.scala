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

    val results = H5Util.loadData(filePath)

    val model = results.child(0).child(2)

    val numOfFunctor = model.child.length - 1
    val nameList = new Array[String](numOfFunctor)

    model.child.foreach(x => {
      if (x.label != "#TYPE")
        nameList(x.label.toInt - 1) = x.child(0).child(0).data(0).toString
    })

    println(nameList.mkString(","))

    val iddict = results.child(0).child(1)

    println(iddict.child.mkString(","))

    val key2id = iddict.child(3)

    import scala.collection.mutable.Map

    val key2idMap = Map[String, Int]()

    for(c <- key2id.child){
      key2idMap += (c.label -> c.data(0).asInstanceOf[Int])
    }

    println(key2idMap("村").toString)

    1 should be(1)
  }
}