package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/26.
  */
import breeze.linalg.DenseMatrix
class Convolution1D(outCh: Int, width: Int, inputDim: Int, stride: Int, padding: Boolean) extends Functor{

  override def functorName = "Convolution1D"

  override def convert(data: DenseMatrix[Float]) = data

}

object Convolution1D {
  def apply(outCh: Int, width: Int, inputDim: Int, stride: Int, padding: Boolean) = new Convolution1D(outCh, width, inputDim, stride, padding)

}
