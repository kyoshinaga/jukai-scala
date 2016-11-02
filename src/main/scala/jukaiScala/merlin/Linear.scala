package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, DenseVector}
import jukaiScala.hdflib.H5Node

/**
  * Created by ubuntu on 10/17/16.
  */
class Linear(indim: Int, outdim: Int) extends Functor{
  override def functorName: String = "Linear"

  val w = DenseMatrix.zeros[Float](indim, outdim)

  val b = DenseVector.zeros[Float](outdim)

  def h5load(W: H5Node, B: H5Node):Unit ={
    for(y <- 0 until W.dims(0).toInt)
      for(x <- 0 until W.dims(1).toInt) {
        w(y, x) = W(y, x).asInstanceOf[Float]
        if (y == 0){
          b(x) = B.data(x).asInstanceOf[Float]
        }
      }
  }

  override final def convert(x:DenseMatrix[Float]) = {
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