package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/05.
  */

import breeze.linalg.DenseMatrix
import breeze.numerics.tanh

object Tanh extends Functor{

  override def functorName = "Tanh"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = data.map(x => tanh(x))

  def apply(x: DenseMatrix[Double]) = this.convert(x)

}
