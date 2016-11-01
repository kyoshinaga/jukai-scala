package jukaiScala.merlin
import breeze.linalg.DenseMatrix
import breeze.stats.distributions.Rand
import jukaiScala.hdflib.H5Node

/**
  * Created by ubuntu on 10/17/16.
  */
class Conv(filterRow: Int, filterCol:Int,
           inCh: Int, outCh: Int,
           strideRow: Int, strideCol: Int,
           paddingRow: Int, paddingCol: Int) extends Functor{
  override val functorName: String = "Conv"

  val w = Array.ofDim[DenseMatrix[Float]](inCh, outCh).map(
    _.map(
      //_ => {DenseMatrix.rand[Float](filterRow, filterCol) :+= -0.5}
      _ => {DenseMatrix.zeros[Float](filterRow, filterCol)}
    )
  )

  private val filterDim = (filterRow, filterCol)
  private val stride = (strideRow, strideCol)
  private val padding = (paddingRow, paddingCol)

  def h5load(data: H5Node): Unit = {
    for(outc <- 0 until data.dims(0).asInstanceOf[Long].toInt)
      for(inc <- 0 until data.dims(1).asInstanceOf[Long].toInt)
        for(y <- 0 until data.dims(3).asInstanceOf[Long].toInt)
          for(x <- 0 until data.dims(2).asInstanceOf[Long].toInt)
            (w(inc)(outc))(y,x) = data(outc, inc, x, y).asInstanceOf[Float]
  }

  override final def convert(data: DenseMatrix[Float]): DenseMatrix[Float] = {
    val filterSize = filterRow * filterCol
    val chSize = inCh * outCh
    val outdims = outsize(data)
    val ws2col = DenseMatrix.zeros[Float](filterSize, chSize)

    for(j <- 0 until inCh){
      for(i <- 0 until outCh){
        val index = j * outCh + i
        ws2col(::,index) := w(j)(i).toDenseVector
      }
    }

    val work = im2col(data, outdims)

    work * ws2col
  }

  def im2col(x: DenseMatrix[Float], outdims: Array[Int]): DenseMatrix[Float] = {
    val filterSize = filterRow * filterCol
    val work = DenseMatrix.zeros[Float](outdims.fold(1)((z,n) => z * n), filterSize)

    val x1 = x.rows
    val x2 = x.cols
    val w1 = filterRow
    val w2 = filterCol
    val s1 = strideRow
    val s2 = strideCol
    val p1 = paddingRow
    val p2 = paddingCol
    val n1 = (x1 + 2 * p1 - w1) / s1 + 1
    val n2 = (x2 + 2 * p2 - w2) / s2 + 1

    for (d2 <- 0 until w2){
      for (d1 <- 0 until w1){
        for(k2 <- 0 until n2){
          for(k1 <- 0 until n1){
            val i1 = k1 * s1 - p1 + d1
            val i2 = k2 * s2 - p2 + d2
            val j1 = d1 + d2 * w1
            val j2 = k2
            if(i1 >= 0 & i1 < x1 & i2 >= 0 & i2 < x2){
              work(j2, j1) = x(i1, i2)
            }
            else{
              work(j2, j1) = 0.0.toFloat
            }
          }
        }
      }
    }
    work
  }

  def outsize(x: DenseMatrix[Float]): Array[Int] = {
    val N = 2
    val dims = Array.ofDim[Int](N)
    dims(0) = (x.rows +  2 * paddingRow - filterRow) + strideRow
    dims(1) = (x.cols +  2 * paddingCol - filterCol) + strideCol
    dims
  }
}

object Conv {
  def apply(filter: Tuple2[Int,Int], ch: Tuple2[Int,Int],
            stride: Tuple2[Int,Int], padding: Tuple2[Int, Int]):Conv =
    new Conv(filter._1, filter._2, ch._1, ch._2, stride._1, stride._2,
      padding._1, padding._2)
}