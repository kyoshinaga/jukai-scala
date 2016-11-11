package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, argmax}
import jukaiScala.hdflib.H5Util
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
class TokenizerSpec extends FlatSpec with Matchers {


  "Tokenizer" should "annotated string" in {

    val testString = "否定した。"
    val goldStringList = List("否定","した","。")

    val filePath = "./target/test-classes/data/tokenizer_test.h5"
    val model = H5Util.loadData(filePath)

    val t = Tokenizer(model)

    val testStringList = t.tokenize(testString).split(" ").toList

    (testStringList zip goldStringList).forall(x => x._1 == x._2) should be (true)
  }

}
