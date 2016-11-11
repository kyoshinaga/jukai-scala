package jukaiScala.merlin

/**
  * Created by ubuntu on 10/17/16.
  */

import breeze.linalg.{DenseMatrix, DenseVector, argmax}
import jukaiScala.hdflib._

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}

class Tokenizer (val model: H5Node){

  if(model == null)
    throw new IllegalArgumentException("cannot construct " + getClass.getSimpleName + " with null")

  val functors = Graph.constructGraph(model)

  class iddict(val dict: H5Node){
    val id2count = dict.child(1).data
    val id2key = dict.child(2).data
    val key2id:mutable.Map[String,Int] = {
      val tempMap = mutable.Map[String, Int]()
      for (c <- dict.child(3).child){
        tempMap += (c.label -> c.data.head.asInstanceOf[Int])
      }
      tempMap
    }
  }

  val dict = new this.iddict(model.child.head.child(1))

  val tagset: mutable.Map[Int, String] = {
    val mappingId = mutable.Map[Int,String]()
    for(c <- model.child.head.child(3).child.filterNot(_.label == "#TYPE")){
      mappingId(c.data.head.asInstanceOf[Int] - 1) = c.label
    }
    mappingId
  }

  def tokenize(str: String) : String = {
    val outputData = callFunctors(decode(str), functors)
    val tags = classification(outputData)
    val decodedString = decode(tags)
    decodedString.map(x => str.substring(x._1, x._2)).mkString(" ")
  }

  def decode(str: String) : DenseMatrix[Float] = {
    val strArray = str.toCharArray.map(x => dict.key2id.getOrElse(x.toString, 1) - 1.0.toFloat)
    val inputData = DenseMatrix(strArray)
    inputData
  }

  def decode(tags: List[Int]): List[(Int, Int)] = {
    var bpos = 0
    val ranges = ListBuffer[(Int,Int)]()
    for (i <- tags.indices){
      tagset(tags(i)) match {
        case "I" => bpos = i
        case "E" =>
          ranges += ((bpos,i+1))
          bpos = i + 1
        case "O" => ranges += ((i, i+1))
      }
    }
    ranges.toList
  }

  def classification(x: DenseMatrix[Float]): List[Int] = {
    for {
      i <- 0 until x.cols
      maxI = argmax(x(::,i))
    }yield maxI
  } toList

  private def callFunctors(input: DenseMatrix[Float], unprocessed:List[Functor]): DenseMatrix[Float] = unprocessed match {
    case functor :: tail =>
      val interOutput = functor.convert(input)
      callFunctors(interOutput, tail)
    case Nil => input
  }
}

object Tokenizer {
  def apply(model: H5Node): Tokenizer = new Tokenizer(model)
}
