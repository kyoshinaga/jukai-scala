package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/27.
  */

import breeze.linalg.{DenseVector, DenseMatrix, softmax}
import breeze.numerics.exp
object Softmax extends Functor{

  override def functorName = "Softmax"

  override def convert(data: DenseMatrix[Float]) = {
    for (x <- 0 until data.cols) {
      val v = data(::, x)
      data(::, x) := (exp(v) :/= exp(softmax(v)))
    }
    data
  }

  override def toString: String = functorName

}
