package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/26.
  */
import breeze.linalg.{DenseMatrix, DenseVector}
import ucar.nc2.{Variable, Group}

class Convolution1D(outCh: Int, width: Int, inputDim: Int, padding: Boolean) extends Functor{

  override def functorName = "Convolution1D"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val work = im2col(data) * w

    for (i <- 0 until work.rows)
      work(i, ::) :+= b.t

    work
  }

  private val w = DenseMatrix.zeros[Double](width * inputDim, outCh)
  private val b = DenseVector.zeros[Double](outCh)

  private val paddingRow = padding match {
    case true => (width - 1) / 2
    case false => 0
  }

  def im2col(x: DenseMatrix[Double]): DenseMatrix[Double] = {
    val inputSize = width * inputDim
    val work = DenseMatrix.zeros[Double](x.rows, inputSize)
    val x1 = x.rows
    for (k2 <- 0 until x1)
      for (k1 <- 0 until 1)
        for (d2 <- 0 until width)
          for (d1 <- 0 until inputDim) {
            val i1 = k1 + d1
            val i2 = k2 - paddingRow + d2
            val j1 = d1 + d2 * inputDim
            val j2 = k2
            if (i2 >= 0 & i2 < x1)
              work(j2, j1) = x(i2, i1)
            else
              work(j2, j1) = 0.0.toFloat
          }
    work
  }

  private def h5load(weight: Variable, bias: Variable): Unit = {
    val weightData = weight.read
    val weightIndex = weightData.getIndex
    val biasData = bias.read
    val biasIndex = biasData.getIndex
    for(i <- 0 until width)
      for(j <- 0 until inputDim)
        for(k <- 0 until outCh){
          val x = k
          val y = i * inputDim + j
          w(y, x) = weightData.getFloat(weightIndex.set(i, 0, j, k))
          if(y == 0)
            b(x) = biasData.getFloat(biasIndex.set(x))
        }
  }

  override def toString: String = "Convolution1D: {outCh: " + outCh + ", width: " + width + ", inputDim: " + inputDim + ", padding: " + padding + "}"

}

object Convolution1D {
  def apply(outCh: Int, width: Int, inputDim: Int, padding: Boolean) = new Convolution1D(outCh, width, inputDim, padding)

  def apply(configs: Map[String, Any], weightGroups: Group) = {
    val layerName = configs("name").toString
    val params = weightGroups.findGroup(layerName)
    val weightNames = params.findAttribute("weight_names")
    val borderMode = configs("border_mode").toString match {
      case "same" => true
      case _ => false
    }
    val weight = params.findVariable(weightNames.getStringValue(0))
    val bias = params.findVariable(weightNames.getStringValue(1))
    val dims = weight.getDimensions
    if(dims.size != 4){
      throw new IllegalArgumentException("invalid dimension for Convolution1D class")
    }

    val c = new Convolution1D(dims.get(3).getLength, dims.get(0).getLength,
      dims.get(2).getLength, borderMode)
    c.h5load(weight, bias)
    c
  }

}
