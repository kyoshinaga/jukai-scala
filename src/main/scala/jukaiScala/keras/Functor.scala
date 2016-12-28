package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import breeze.linalg.{DenseMatrix, DenseVector}
trait Functor {

  def functorName:String

  def convert(data: DenseMatrix[Float]): DenseMatrix[Float]

}
