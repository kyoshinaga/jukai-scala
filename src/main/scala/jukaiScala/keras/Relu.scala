package jukaiScala.keras


/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import breeze.linalg.DenseMatrix

object Relu extends Functor{

  override def functorName = "Relu"

  override final def convert(data: DenseMatrix[Float]) = data.map(x => if(x > 0.0) x else 0.0.toFloat)

  def apply(x: DenseMatrix[Float]) = this.convert(x)

}
