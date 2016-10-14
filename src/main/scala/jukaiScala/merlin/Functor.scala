package jukaiScala.merlin

import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/10/04.
  */
trait Functor {

  val functorName:String

  def h5load(data:String)

  def convert(x:DenseMatrix[Double])
}
