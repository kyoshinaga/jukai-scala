package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/10.
  */
import org.scalatest.{FlatSpec, Matchers}
import breeze.linalg.{csvread, DenseMatrix}
import breeze.numerics.abs
import java.io._
class FlattenSpec extends FlatSpec with Matchers{

  "Flatten" should "load model and convert input matrix" in {
    val model = KerasModel("./target/test-classes/data/flatten/flatten_model.h5")
    val inputData = csvread(new File("./target/test-classes/data/flatten/flatten_input.csv"),separator = ',')
    val goldData = csvread(new File("./target/test-classes/data/flatten/flatten_gold.csv"))

    val output = model.convert(inputData)

    val diff = abs(output- goldData).forall(x => x < 1e-6)

    diff should be (true)
  }

}
