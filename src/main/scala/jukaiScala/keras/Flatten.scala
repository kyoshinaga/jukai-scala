package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/27.
  */
import breeze.linalg.DenseMatrix

object Flatten extends Functor{

  override def functorName = "Flatten"

  override final def convert(data: DenseMatrix[Float]) = data.reshape(data.rows * data.cols, 1)

  override def toString: String = functorName

}
