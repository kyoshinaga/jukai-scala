package jukaiScala.merlin

import breeze.linalg.{DenseMatrix, argmax}
import jukaiScala.hdflib.H5Util
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */
class TokenizerSpec extends FlatSpec with Matchers {


  "Tokenizer" should "annotated string" in {

    //val testString = "村山富市首相は年頭にあたり首相官邸で内閣記者会と二十八日会見し、社会党の新民主連合所属議員の離党問題について「政権に影響を及ぼすことにはならない。離党者がいても、その範囲にとどまると思う」と述べ、大量離党には至らないとの見通しを示した。また、一九九五年中の衆院解散・総選挙の可能性に否定的な見解を表明、二十日召集予定の通常国会前の内閣改造を明確に否定した。"
    val testString = "否定した。"
//    val testString = "\n"

    val filePath = "./target/test-classes/data/tokenizer_test.h5"
    val model = H5Util.loadData(filePath)

    val t = Tokenizer(model)

    val m = t.decode(testString)

    println(testString)

    println("Decoded")
    println(m)

    val emb = t.embed.convert(m)

    println("Embed")
    println(emb)

    val cv = t.conv.convert(emb)

    println("Conv")
    println(cv)
    println(cv(::,9))

    val trcv = Transpose(cv)

    println("Transpose")
    println(trcv)

    val rel = Relu(trcv)

    println("relu")
    println(rel)

    val output = t.ls.convert(rel)

    println("output")
    println(output)

//    println(t.embed.w(2))

    1 should be (1)
  }

}
