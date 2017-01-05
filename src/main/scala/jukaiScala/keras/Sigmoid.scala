package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/05.
  */
import breeze.linalg.DenseMatrix
import breeze.numerics.exp

object Sigmoid extends Functor {

  override def functorName = "Sigmoid"

  override final def convert(data: DenseMatrix[Float]) = data.map(x => 1 / (1 + exp(-x)))

  def apply(x: DenseMatrix[Float]) = this.convert(x)
}
