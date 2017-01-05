package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/05.
  */

import breeze.linalg.{DenseMatrix, DenseVector}
import ucar.nc2.Variable
class Embedding(vocabulary: Int, outdim: Int) extends Functor{

  override def functorName = "Embedding"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val arrayOfId = data.reshape(data.size, 1)
    val length = arrayOfId.size
    val z = DenseMatrix.zeros[Double](length, outdim)
    for (i <- 0 until length){
      z(i, ::) := w(arrayOfId(i, 0).asInstanceOf[Int]).t
    }
    z
  }

  val w = new Array[DenseVector[Double]](vocabulary).map(_ => DenseVector.zeros[Double](outdim))

}

object Embedding{
  def apply(vocabulary: Int, outdim: Int) = new Embedding(vocabulary, outdim)
}
