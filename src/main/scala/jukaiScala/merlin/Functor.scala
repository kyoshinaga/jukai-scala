package jukaiScala.merlin

import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/10/04.
  */
trait Functor {

  val functorName:String

  def getFunctorName() = functorName
}
