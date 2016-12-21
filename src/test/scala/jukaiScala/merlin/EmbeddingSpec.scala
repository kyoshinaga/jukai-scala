package jukaiScala.merlin


/**
  * Created by kenta-yoshinaga on 2016/10/19.
  */

import org.scalatest._
import breeze.linalg.{DenseMatrix, DenseVector}
import jukaiScala.hdflib.H5Util

class EmbeddingSpec extends FlatSpec with Matchers{

  "Embedding.convert" should "match gold matrix of model file" in {
    val filePath = "./target/test-classes/data/tokenizer_test_embedding.h5"
    val model = H5Util.loadData(filePath).child.head
    val embedding = Lookup(model)
    val inputData = DenseMatrix((0 until 20).toArray.map(_.toFloat))

    val convertedData = embedding.convert(inputData)
    val goldMatrix = DenseMatrix.zeros[Float](2,20)
    for (y <- 0 until 2)
      for (x <- 0 until 20)
        goldMatrix(y, x) = model.child(1)(x, y).asInstanceOf[Float]

    convertedData should be (goldMatrix)
  }

}
