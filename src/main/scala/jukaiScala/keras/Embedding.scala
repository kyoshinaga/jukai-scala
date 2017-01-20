package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/05.
  */

import breeze.linalg.{DenseMatrix, DenseVector}
import ucar.nc2.{Variable, Group}
class Embedding(vocabulary: Int, outdim: Int) extends Functor{

  override def functorName = "Embedding"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val arrayOfId = data.reshape(data.size, 1)
    val length = arrayOfId.size
    val z = DenseMatrix.zeros[Double](length, outdim)
    for (i <- 0 until length){
      z(i, ::) := w(arrayOfId(i, 0).asInstanceOf[Int]).t
    }
    z
  }

  private val w = new Array[DenseVector[Double]](vocabulary).map(_ => DenseVector.zeros[Double](outdim))

  def h5load(weight: Variable):Unit = {
    val weightData = weight.read
    val weightIndex = weightData.getIndex
    for(y <- 0 until vocabulary)
      for(x <- 0 until outdim){
        w(y)(x) = weightData.getFloat(weightIndex.set(y, x))
      }
  }

}

object Embedding{
  def apply(vocabulary: Int, outdim: Int) = new Embedding(vocabulary, outdim)

  def apply(configs: Map[String, Any], weightGroups: Group) = {
    val layerName = configs("name").toString
    val params = weightGroups.findGroup(layerName)
    val weightNames = params.findAttribute("weight_names")
    val weight = params.findVariable(weightNames.getStringValue(0))
    val dims = weight.getDimensions
    if(dims.size != 2){
      throw new IllegalArgumentException("invalid dimension for Embedding class")
    }
    val e = new Embedding(dims.get(0).getLength, dims.get(1).getLength)
    e.h5load(weight)
    e
  }
}
