package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/05.
  */

import breeze.linalg.DenseMatrix
import breeze.numerics.tanh

object Tanh extends Functor{

  override def functorName = "Tanh"

  override final def convert(data: DenseMatrix[Float]) = data.map(x => tanh(x))

  def apply(x: DenseMatrix[Float]) = this.convert(x)

}
