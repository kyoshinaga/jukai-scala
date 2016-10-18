package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, DenseVector}

/**
  * Created by ubuntu on 10/17/16.
  */
class Linear(indim: Int, outdim: Int) extends Functor{
  override val functorName: String = "Linear"

  private val w = DenseMatrix.zeros[Double](indim, outdim)

  private val b = DenseVector.zeros[Double](outdim)

  def h5load(data: String) = println("hogeLinear")

  def convert(x:DenseMatrix[Double]) = {
    val z = w.t * x
    for (i <- 0 until outdim){
      z(::,i) := z(::,i) + b
    }
    z
  }
}

object Linear {

  def apply(indim:Int, outdim:Int) = new Linear(indim, outdim)

}