package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/27.
  */

import breeze.linalg.{DenseVector, DenseMatrix, softmax}
import breeze.numerics.exp

object Softmax extends Functor{

  override def functorName = "Softmax"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    for (x <- 0 until data.cols) {
      val v = data(::, x)
      data(::, x) := (exp(v) :/= exp(softmax(v)))
    }
    data
  }

  def apply(x: DenseMatrix[Double]) = this.convert(x)
}
