package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/12/12.
  */

object H5Attribute {
  def apply[T](data: Seq[T], label: String): Unit = println("test")
}

class H5Attribute[+A](val data:Seq[A],
                      val label: String,
                      val ndim: Int,
                      val dims: Seq[_ <: Long]) extends H5Node with Serializable {
  if (data == null)
    throw new IllegalArgumentException("cannot constract " + getClass.getSimpleName + " with null")

  override protected def basisForHashCode:Seq[Any] = Seq(data)

  override def strict_==(other: H5Equality) = other match {
    case x:H5Attribute[_] => data == x.data
    case _ => false
  }

  override def canEqual(other: Any) = other match {
    case _:H5DataSet[_] => true
    case _              => false
  }

  override def dataType: String = "Attribute"

  override def child: Seq[H5Node] = Nil

  override def text: String = data.toString

}
