package jukaiScala.merlin

import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */
class Embedding private(val indim: Int, val outdim: Int) extends Functor{

  override val functorName = "Embedding"

  val w = new Array[DenseVector[Double]](indim).map(_ => DenseVector.rand[Double](outdim))

  def h5load(data: String): Unit = println("hogehoge")

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val arrayOfId = data.reshape(1, data.size)
    val length = arrayOfId.size
    val z = DenseMatrix.zeros[Double](outdim, length)
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

