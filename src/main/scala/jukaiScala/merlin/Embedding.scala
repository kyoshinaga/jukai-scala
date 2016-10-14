package jukaiScala.merlin

import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */
class Embedding(intdim: Int, outdim: Int) extends Functor{

  val functorName = "Embedding"

  val w = new Array[DenseVector[Double]](intdim).map(r =>
    DenseVector.zeros[Double](outdim))

  override def h5load(data: String): Unit = println("hogehoge")

  override def convert(x: DenseMatrix[Double]): Unit = println("hoge")
}
