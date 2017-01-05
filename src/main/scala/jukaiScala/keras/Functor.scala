package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import breeze.linalg.{DenseMatrix, DenseVector}
import ucar.nc2.{Variable, Group}

trait Functor {

  def functorName:String

  def convert(data: DenseMatrix[Double]): DenseMatrix[Double]

  override def toString: String = functorName
}
