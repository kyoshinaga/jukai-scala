package jukaiScala.merlin

import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */
class Embedding(intdim: Int, outdim: Int) {

  private val w = DenseMatrix.zeros[Double](intdim, outdim)

  // def h5load

}
