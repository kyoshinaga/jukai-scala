package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/26.
  */
import breeze.linalg.{DenseVector, DenseMatrix}
import ucar.nc2.{Variable, Group}

class Dense (inputDim: Int, outputDim: Int) extends Functor {

  val w = DenseMatrix.zeros[Double](inputDim, outputDim)

  private val b = DenseVector.zeros[Double](outputDim)

  override def functorName = "Dense"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val z = w * data
    for (i <- 0 until data.cols){
      z(::,i) :+= b
    }
    z
  }

  def h5load(weight: Variable, bias: Variable):Unit = {
    val weightData = weight.read
    val weightIndex = weightData.getIndex
    val biasData = bias.read
    val biasIndex = biasData.getIndex
    for(y <- 0 until inputDim) {
      for (x <- 0 until outputDim) {
        w(y, x) = weightData.getFloat(weightIndex.set(y, x))
        if (y == 0)
          b(x) = biasData.getFloat(biasIndex.set(x))
      }
    }
  }

  override def toString: String = "Dense: {inputDim: " + inputDim + ", outputDim: " + outputDim + "}"

  def head: String = w(0 until 2, ::).toString
}

object Dense {
  def apply(inputDim:Int, outputDim: Int) = new Dense(inputDim, outputDim)

  def apply(configs: Map[String, Any], weightGroups: Group) = {
    val layerName = configs("name").toString
    val params = weightGroups.findGroup(layerName)
    val weightNames = params.findAttribute("weight_names")
    val weight = params.findVariable(weightNames.getStringValue(0))
    val bias = params.findVariable(weightNames.getStringValue(1))
    val dims = weight.getDimensions
    if (dims.size != 2){
      throw new IllegalArgumentException("invalid dimension for Dense class")
    }
    val d = new Dense(dims.get(0).getLength, dims.get(1).getLength)
    d.h5load(weight, bias)
    d
  }
}