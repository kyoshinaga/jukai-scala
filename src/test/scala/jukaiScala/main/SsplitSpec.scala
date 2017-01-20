package jukaiScala.main

/**
  * Created by kenta-yoshinaga on 2017/01/17.
  */
import org.scalatest.{FlatSpec, Matchers}

class SsplitSpec extends FlatSpec with Matchers{

  "ssplit" should "get result" in {

    val ssplit = KerasParser("./target/test-classes/data/ssplit_model_bio_bunsetsu.h5", "./target/test-classes/data/jpnLookup.json")
    //val ssplit = KerasParser("./target/test-classes/data/ssplit_model_bio.h5", "./target/test-classes/data/jpnLookup.json")

    //val str = "詰め将棋の本を買ってきました。駒と盤は持っています。フリーで使えるソフトウェアってありませんか？やっぱりないのでしょうかね・・・・↓これなんかどうですか？"
    val str = "やっぱりないのでしょうかね・・・・↓これなんかどうですか？"

    val listOfSplit = ssplit.parsing(str)

    println(listOfSplit.mkString("\n"))

    1 should be (1)
  }

}
