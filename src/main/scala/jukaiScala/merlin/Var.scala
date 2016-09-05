package jukaiScala.merlin

import breeze.linalg.DenseVector

/**
  * Created by kenta-yoshinaga on 2016/08/30.
  */
class Var[T](data: Any, args: DenseVector[Var[T]], f: Any, df: Any, grad: Any) {
  type Var

}
