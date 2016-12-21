package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, argmax}
import jukaiScala.hdflib.H5Util
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
class TokenizerSpec extends FlatSpec with Matchers {


  "Tokenizer" should "annotated string" in {

    val testString = "上海ガニをばくばく食べる。"
    val testString2 = "そのタイムワーナーを通信大手ＡＴ＆Ｔが買収することで、米国で業種を超えたメディア再編がまた動き出した。"
    val goldStringList = List("上海","ガニ","を","ばくばく","食べる","。")

    val filePath = "./target/test-classes/data/tokenizer_test.h5"
    val filePath2 = "./target/test-classes/data/tokenizer_result.h5"


    val t = Tokenizer(filePath)
    val t2 = Tokenizer(filePath2)

    val testStringList = t.tokenize(testString).split("\n").toList

    val testStringList2 = t2.tokenize(testString2).split("\n").toList

    val testStringList2WithRange = t2.tokenizeWithRanges(testString2)

    println(testStringList)
    println(testStringList2WithRange)

    (testStringList zip goldStringList).forall(x => x._1 == x._2) should be (true)
  }

}
