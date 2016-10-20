package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, argmax}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
class TokenizerSpec extends FlatSpec with Matchers {


  "Tokenizer" should "annotated string" in {

    def functionRecur(input: DenseMatrix[Double], unprocessed:List[Functor]): DenseMatrix[Double] = unprocessed match {
      case functor :: tail => {
        val interOutput = functor.convert(input)
        functionRecur(interOutput, tail)
      }
      case Nil => input
    }

    val embed = Embedding(1000, 10)
    val conv = Conv((10, 9), (1, 10), (1, 1), (0, 4))
    val ls = Linear(10, 2)

    val SampleData = DenseMatrix(Array[Double](3, 1, 2, 10, 20 ,50))

    val z1 = embed.convert(SampleData)

    println(SampleData)
    println("Embedding")
    println(z1)

    val z2 = conv.convert(z1)

    println("Convolution")
    println(z2)

    val z3 = Relu(z2)

    println("Relu")
    println(z3)

    val z4 = ls.convert(z3.t)

    println("Linear")
    println(z4)

    val y = argmax(z4(::,0))

    println("Output")
    println(y)

    val ar = List[Functor](embed, conv, Relu, Transpose, ls)

    val yy = functionRecur(SampleData, ar)

    println("Recursive operation")
    println(yy)

    1 should be (1)
  }

}
