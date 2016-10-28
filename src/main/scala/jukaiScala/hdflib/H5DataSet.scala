package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/10/28.
  */
class H5DataSet[+A] (val data: A, val label: String) extends H5Node with Serializable {

  if( data == null)
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with null")

  override protected def basisForHashCode: Seq[Any] = Seq(data)

  override def strict_==(other: H5Equality) = other match {
    case x:H5DataSet[_] => data == x.data
    case _              => false
  }

  override def canEqual(other: Any) = other match {
    case _:H5DataSet[_] => true
    case _              => false
  }

  override def dataType: String = "DataSet"

  override def child: Seq[H5Node] = Nil

  override def text: String = data.toString

}
