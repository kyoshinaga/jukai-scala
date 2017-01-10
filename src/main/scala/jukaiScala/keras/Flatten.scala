package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/27.
  */
import breeze.linalg.DenseMatrix

object Flatten extends Functor{

  override def functorName = "Flatten"

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = data.t.toDenseVector.toDenseMatrix

  override def toString: String = functorName

}
