package jukaiScala.keras

/**
  * Created by kenta-yoshinaga on 2016/12/07.
  */

import org.scalatest.{FlatSpec, Matchers}
import breeze.linalg.{csvread, DenseMatrix}
import breeze.numerics.abs
import java.io._

class KerasModelSpec extends FlatSpec with Matchers {
  "kerasModel" should "load linear-network" in {

    val model = KerasModel("./target/test-classes/data/ssplitModel/ssplit_model.h5")
    val inputData = csvread(new File("./target/test-classes/data/ssplitModel/ssplit_input.csv"),separator = ',')
    val goldData = csvread(new File("./target/test-classes/data/ssplitModel/ssplit_gold.csv"), separator = ',')

    val output = model.convert(inputData)

    val diff = abs(output - goldData).forall(x => x < 1e-6)

    diff should be (true)
  }
}
