package jukaiScala.merlin

import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix
import jukaiScala.hdflib.H5Node

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */
class Embedding private(val indim: Int, val outdim: Int) extends Functor{

  override val functorName = "Embedding"

  val w = new Array[DenseVector[Float]](indim).map(_ => DenseVector.zeros[Float](outdim))

  def h5load(data: H5Node): Unit = {
    for(y <- 0 until data.dims(0).asInstanceOf[Long].toInt)
      for(x <- 0 until data.dims(1).asInstanceOf[Long].toInt)
        w(y)(x) = data(y,x).asInstanceOf[Float]
  }

  override final def convert(data: DenseMatrix[Float]): DenseMatrix[Float] = {
    val arrayOfId = data.reshape(1, data.size)
    val length = arrayOfId.size
    val z = DenseMatrix.zeros[Float](outdim, length)
    for ( i <- 0 until length){
      z(::, i) := w(arrayOfId(0,i).asInstanceOf[Int])
    }
    z
  }
}

object Embedding{

  def apply(indim:Int, outdim: Int):Embedding = new Embedding(indim, outdim)

  def unapply(e: Embedding) = Option((e.indim, e.outdim))
}

