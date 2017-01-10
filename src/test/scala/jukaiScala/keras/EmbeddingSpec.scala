package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2017/01/10.
  */

import org.scalatest.{FlatSpec, Matchers}
import breeze.linalg.{csvread, DenseMatrix}
import breeze.numerics.abs
import java.io._
class EmbeddingSpec extends FlatSpec with Matchers{

  "Embedding" should "load model and convert input matrix" in {

    val model = KerasModel("./target/test-classes/data/embedding/embedding_model.h5")
    val inputData = csvread(new File("./target/test-classes/data/embedding/embedding_input.csv"),separator = ',')
    val goldData = csvread(new File("./target/test-classes/data/embedding/embedding_gold.csv"),separator = ',')

    val output = model.convert(inputData)

    val diff = abs(output - goldData).forall(x => x < 1e-6)

    diff should be (true)
  }

}
