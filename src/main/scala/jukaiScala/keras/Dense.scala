package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/26.
  */
import breeze.linalg.{DenseVector, DenseMatrix}

class Dense (inputDim: Int, outputDim: Int) extends Functor {

  private val w = DenseMatrix.zeros[Float](inputDim, outputDim)

  private val b = DenseVector.zeros[Float](outputDim)

  override def functorName = "Dense"

  override final def convert(data: DenseMatrix[Float]) = {
    val z = w.t * data
    for (i <- 0 until data.cols){
      z(::,i) := z(::,i) + b
    }
    z
  }
}

object Dense {
  def apply(inputDim:Int, outputDim: Int) = new Dense(inputDim, outputDim)

}