package jukaiScala.hdflib

import sun.text.normalizer.Utility

/**
  * Created by kenta-yoshinaga on 2016/10/28.
  */
object H5Node{

  def unapplySeq(n: H5Node) = Some((n.label, n.child))

}

abstract class H5Node extends H5NodeSeq {

  def label: String

  def dataType: String

  def child: Seq[H5Node]

  def nonEmptyChildren: Seq[H5Node] = child filterNot (_.toString == "")

  def descendant: List[H5Node] =
    child.toList.flatMap { x => x :: x.descendant }

  def descendant_or_self: List[H5Node] = this :: descendant

  override def canEqual(other: Any) = other match {
    case x: H5Group => false
    case x: H5Node  => true
    case _          => false
  }

  override protected def basisForHashCode: Seq[Any] = label :: dataType :: nonEmptyChildren.toList

  override def strict_==(other: H5Equality) = other match {
    case _: H5Group => false
    case x: H5Node =>
      (label == x.label) &&
        (dataType == x.dataType) &&
        (nonEmptyChildren sameElements x.nonEmptyChildren)
    case _ =>
      false
  }

  def theSeq: Seq[H5Node] = this :: Nil

  override def toString(): String = this.label

  override def text: String = super.text

  def data: Seq[Any]
  def ndim: Int
  def dims: Seq[_ <: Long]

  def apply(y: Int, x: Int) = {
    if (data != Nil && ndim == 2 && y < dims(0) && x < dims(1))
      data(y * dims(1).toInt + x)
    else
      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
  }

  def apply(z: Int, y: Int, x: Int) = {
    if (data != Nil && ndim == 3 && z < dims(0) && y < dims(1) && x < dims(2))
      data((z * dims(2).toInt + y) * dims(1).toInt + x)
    else
      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
  }

  def apply(i: Int, z: Int, y: Int, x: Int) = {
    if (data != Nil && ndim == 4 && i < dims(0) && z < dims(1) && y < dims(2) && x < dims(3))
      data(((i * dims(3).toInt + z) * dims(2).toInt + y) * dims(1).toInt + x)
    else
      throw new IllegalArgumentException("cannot access to data of " + getClass.getSimpleName + " with invalid arguments.")
  }

}
