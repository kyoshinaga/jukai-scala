package jukaiScala.merlin
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
object Transpose extends Functor{

  override val functorName: String = "Transpose"

  override def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = data.t

  def apply(m: DenseMatrix[Double]) = this.convert(m)

}
