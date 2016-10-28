package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/10/28.
  */

object H5Equality {
  def asRef(x: Any): AnyRef = x.asInstanceOf[AnyRef]

  def compareBlithely(x1: AnyRef, x2: String): Boolean = x1 match {
    case x: H5NodeSeq => x.text == x2
    case _ => false
  }

  def compareBlithely(x1: AnyRef, x2: H5Node): Boolean = x1 match {
    case x: H5NodeSeq if x.length == 1 => x2 == x(0)
    case _ => false
  }

  def compareBlithely(x1: AnyRef, x2: AnyRef): Boolean = {
    if(x1 == null | x2 == null)
      return (x1 eq x2)

    x2 match {
      case s: String  => compareBlithely(x1, s)
      case n: H5Node  => compareBlithely(x1, n)
      case _          => false
    }
  }
}

import H5Equality._

trait H5Equality extends scala.Equals {

  protected def basisForHashCode: Seq[Any]

  def strict_==(other: H5Equality): Boolean
  def strict_!=(other: H5Equality) = !strict_==(other)

  override def canEqual(other: Any): Boolean = other match{
    case x: H5Equality  => true
    case _              => false
  }

  override def hashCode() = basisForHashCode.##
  override def equals(other: Any) = doComparison(other, blithe = false)
  final def h5_==(other: Any) = doComparison(other, blithe = true)
  final def h5_!=(other: Any) = !h5_==(other)

  private def doComparison(other: Any, blithe: Boolean) = {
    val strictlyEqual = other match {
      case x: AnyRef if this eq x => true
      case x: H5Equality          => (x canEqual this) && (this strict_== x)
      case _                      => false
    }
    strictlyEqual || (blithe && compareBlithely(this, asRef(other)))
  }
}
