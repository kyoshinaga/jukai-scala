package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/26.
  */
import breeze.linalg.{DenseMatrix, DenseVector}
import ucar.nc2.Variable

class Convolution1D(outCh: Int, width: Int, inputDim: Int, padding: Boolean) extends Functor{

  override def functorName = "Convolution1D"

  override final def convert(data: DenseMatrix[Float]) = data

  val w = DenseMatrix.zeros[Float](width * inputDim, outCh)
  val b = DenseVector.zeros[Float](outCh)

  val paddingRow = padding match {
    case true => (width - 1) / 2
    case false => 0
  }

  def im2col(x: DenseMatrix[Float]): DenseMatrix[Float] = {
    val inputSize = width * inputDim
    val work = DenseMatrix.zeros[Float](x.rows, inputSize)

    val x1 = x.rows

    for (k2 <- 0 until x1) {
      for (k1 <- 0 until 1) {
        for (d2 <- 0 until width) {
          for (d1 <- 0 until inputDim) {
            val i1 = k1 + d1
            val i2 = k2 - paddingRow + d2
            val j1 = d1 + d2 * inputDim
            val j2 = k2
            if(i2 >= 0 & i2 < x1)
              work(j2, j1) = x(i2, i1)
            else
              work(j2, j1) = 0.0.toFloat
          }
        }
      }
    }
    work
  }

  override def toString: String = "Convolution1D: {outCh: " + outCh + ", width: " + width + ", inputDim: " + inputDim + ", padding: " + padding + "}"

}

object Convolution1D {
  def apply(outCh: Int, width: Int, inputDim: Int, padding: Boolean) = new Convolution1D(outCh, width, inputDim, padding)

  def apply(weight: Variable, bias: Variable, padding: Boolean) = new Convolution1D(1,1,1,true)

}
