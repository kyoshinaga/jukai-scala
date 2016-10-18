package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, DenseVector}

/**
  * Created by ubuntu on 10/17/16.
  */
// class Relu {
// }

object Relu{

  val functorName: String = "relu"

  def apply(m : DenseMatrix[Double]) = m.map(x => if (x > 0.0) x else 0.0)

}
