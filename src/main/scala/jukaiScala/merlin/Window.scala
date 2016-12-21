package jukaiScala.merlin

import breeze.linalg.DenseMatrix
import jukaiScala.hdflib.H5Node

/**
  * Created by kenta-yoshinaga on 2016/12/14.
  */
class Window private (filter:  (Int,Int),
                      strides: (Int,Int),
                      padding: (Int,Int)) extends Functor {

  override def functorName: String = "Window"

  override final def convert(data: DenseMatrix[Float]): DenseMatrix[Float] = {
    return data
  }

}

object Window {
  def apply(filter: (Int,Int), strides: (Int,Int), padding: (Int,Int)): Window =
    new Window(filter, strides, padding)


}
