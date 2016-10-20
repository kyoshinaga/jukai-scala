package jukaiScala.merlin
import breeze.linalg.DenseMatrix
import breeze.stats.distributions.Rand

/**
  * Created by ubuntu on 10/17/16.
  */
class Conv(filterRow: Int, filterCol:Int,
           inCh: Int, outCh: Int,
           strideRow: Int, strideCol: Int,
           paddingRow: Int, paddingCol: Int) extends Functor{
  override val functorName: String = "Conv"

  val w = Array.ofDim[DenseMatrix[Double]](inCh, outCh).map(
    _.map(
      _ => {DenseMatrix.rand[Double](filterRow, filterCol) :+= -0.5}
    )
  )
  private val filterDim = (filterRow, filterCol)
  private val stride = (strideRow, strideCol)
  private val padding = (paddingRow, paddingCol)

  def h5load(data: String): Unit = println("hogehoge")

  override final def convert(data: DenseMatrix[Double]): DenseMatrix[Double] = {
    val filterSize = filterRow * filterCol
    val chSize = inCh * outCh
    val outdims = outsize(data)
    val ws2col = DenseMatrix.zeros[Double](filterSize, chSize)

    for(j <- 0 until inCh){
      for(i <- 0 until outCh){
        val index = j * outCh + i
        ws2col(::,index) := w(j)(i).toDenseVector
      }
    }

    val work = im2col(data, outdims)

    work * ws2col
  }

  def im2col(x: DenseMatrix[Double], outdims: Array[Int]): DenseMatrix[Double] = {
    val filterSize = filterRow * filterCol
    val work = DenseMatrix.zeros[Double](outdims.fold(1)((z,n) => z * n), filterSize)

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
              work(j2, j1) = 0
            }
          }
        }
      }
    }
    work
  }

  def outsize(x: DenseMatrix[Double]): Array[Int] = {
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