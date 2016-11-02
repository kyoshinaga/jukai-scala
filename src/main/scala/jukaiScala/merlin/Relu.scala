package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, DenseVector}

/**
  * Created by ubuntu on 10/17/16.
  */
// class Relu extends Functor {
//   override val functorName: String = "relu"
//
//   override final def convert(data: DenseMatrix[Double]) = data.map(x => if (x > 0.0) x else 0.0)
//}

object Relu extends Functor{

  override def functorName:String = "Relu"

  override final def convert(data: DenseMatrix[Float]) = data.map(x => if (x > 0.0) x else 0.0.toFloat)

  def apply(m : DenseMatrix[Float]) = this.convert(m)

}
