package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/06.
  */

import org.scalatest.{FlatSpec, Matchers}
import breeze.linalg.{csvread, DenseMatrix}
import breeze.numerics.abs
import java.io._

class Convolution1DSpec extends FlatSpec with Matchers{

  "Convolution1D" should "load model and convert input matrix" in {

    val model = KerasModel("./target/test-classes/data/convolution1d/convolution1d_model.h5")
    val inputData = csvread(new File("./target/test-classes/data/convolution1d/convolution1d_input.csv"),separator = ',')
    val goldData = csvread(new File("./target/test-classes/data/convolution1d/convolution1d_gold.csv"),separator = ',')

    val output = model.graph(0).convert(inputData)

    val diff = abs(output - goldData).forall(x => x < 1e-6)

    diff should be (true)
  }
}
