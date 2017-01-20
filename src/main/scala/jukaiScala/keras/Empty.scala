package jukaiScala.keras
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2017/01/17.
  */
object Empty extends Functor{

  override def functorName = "Empty"

  override final def convert(data: DenseMatrix[Double]) = data

}
