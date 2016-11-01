package jukaiScala.merlin

/**
  * Created by ubuntu on 10/17/16.
  */

import breeze.linalg.DenseMatrix
import jukaiScala.hdflib._

import scala.collection.mutable.Map

class Tokenizer (val model: H5Node){

  if(model == null)
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with null")

  val modelData = model.child(0).child(2)

  val mappingId = Map[Int,Int]()

  var index = 0

  for ( c <- modelData.child){
    if(c.label != "#TYPE"){
      mappingId += (c.label.toInt -> index)
    }
    index += 1
  }

  def embedding(data: H5Node): Embedding = {
    val rawW = data.child(mappingId(5)).child(0).child(1)
    val tmpE = Embedding(rawW.dims(0).toInt, rawW.dims(1).toInt)
    tmpE.h5load(rawW)
    tmpE
  }

  val embed = embedding(modelData)

  def convolution(data: H5Node): Conv = {
    val rawC = data.child(mappingId(6)).child(0)
    val filterDims = rawC.child(1)
    val paddims = rawC.child(2)
    val stride = rawC.child(3)
    val w = rawC.child(4)
    val tempC = Conv(
      (filterDims.child(1).data(0).asInstanceOf[Int],
      filterDims.child(2).data(0).asInstanceOf[Int]),
      (w.child(1).dims(1).asInstanceOf[Long].toInt,
      w.child(1).dims(0).asInstanceOf[Long].toInt),
      (stride.child(1).data(0).asInstanceOf[Int],
      stride.child(2).data(0).asInstanceOf[Int]),
      (paddims.child(1).data(0).asInstanceOf[Int],
      paddims.child(2).data(0).asInstanceOf[Int])
    )
    tempC.h5load(w.child(1))
    tempC
  }

  val conv = convolution(modelData)

  def linear(data: H5Node): Linear = {
    val rawLs = data.child(mappingId(12)).child(0)
    val b = rawLs.child(1)
    val w = rawLs.child(2)
    val tmpLs = Linear(w.dims(0).toInt, w.dims(1).toInt)
    tmpLs.h5load(w, b)
    tmpLs
  }

  val ls = linear(modelData)

  class iddict(val dict: H5Node){
    val id2count = dict.child(1).data
    val id2key = dict.child(2).data
    val key2id:Map[String,Int] = {
      val tempMap = Map[String, Int]()
      for (c <- dict.child(3).child){
        tempMap += (c.label -> c.data(0).asInstanceOf[Int])
      }
      tempMap
    }
  }

  val dict = new this.iddict(model.child(0).child(1))

  private def tagset: Map[Int, String] = {
    val mappingId = Map[Int,String]()

    var index = 0
    for(c <- model.child(0).child(3)){
      mappingId += (index -> c.label)
      index += 1
    }
    mappingId
  }

  val tags = tagset

  val functorArray: List[Functor] = List[Functor](embed, conv, Relu, Transpose, ls)

  def decode(str: String) : DenseMatrix[Float] = {
    val strArray = str.toCharArray.map(x => dict.key2id(x.toString) - 1.0.toFloat)
    val inputData = DenseMatrix(strArray)
    inputData
  }

  def tokenizer(str: String) : DenseMatrix[Float] = {
    val strArray = str.toCharArray.map(x => dict.key2id(x.toString) - 1.0.toFloat)
    val inputData = DenseMatrix(strArray)
    callFunctors(inputData, functorArray)
  }


  private def callFunctors(input: DenseMatrix[Float], unprocessed:List[Functor]): DenseMatrix[Float] = unprocessed match {
    case functor :: tail => {
      val interOutput = functor.convert(input)
      callFunctors(interOutput, tail)
    }
    case Nil => input
  }
}

object Tokenizer {
  def apply(model: H5Node): Tokenizer = new Tokenizer(model)
}
