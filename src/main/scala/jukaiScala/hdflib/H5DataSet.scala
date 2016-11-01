package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/10/28.
  */

object H5DataSet {
  def apply[T](data: Seq[T], label: String): H5DataSet[T] =
    new H5DataSet[T](data, label, 1, Seq(data.length))

  def apply[T](data: Seq[T], label: String, ndim: Int, dims: Seq[_ <: Long]): H5DataSet[T] =
    new H5DataSet[T](data, label, ndim, dims)

  def unapplySeq[T](n: H5DataSet[T]) = Some((n.data, n.label, n.ndim, n.dims))
}

class H5DataSet[+A] (val data: Seq[A],
                     val label: String,
                     val ndim: Int,
                     val dims: Seq[_ <: Long]) extends H5Node with Serializable {

  if( data == null)
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with null")

  if( dims.product != data.length )
    throw new IllegalArgumentException("cannot construct " + label +
      "[" + getClass.getSimpleName + "]" + " with invalid dims\n" +
      "product of dims: " + dims.product + "\n" +
      "data length: " + data.length)

  if( ndim != dims.length )
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with unmatched ndim ")

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

//  def apply(y: Int, x: Int) = {
//    if (ndim == 2 && y < dims(1) && x < dims(0))
//      data(y * dims(1).toInt + x)
//    else
//      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
//  }
//
//  def apply(z: Int, y: Int, x: Int) = {
//    if (ndim == 3 && z < dims(2) && y < dims(1) && x < dims(0))
//      data((z * dims(2).toInt + y) * dims(1).toInt + x)
//    else
//      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
//  }
//
//  def apply(i: Int, z: Int, y: Int, x: Int) = {
//    if (ndim == 4 && i < dims(3) && z < dims(2) && y < dims(1) && x < dims(0))
//      data(((i * dims(3).toInt + z) * dims(2).toInt + y) * dims(1).toInt + x)
//    else
//      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
//  }

}
