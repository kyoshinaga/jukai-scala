package jukaiScala.util

/**
  * Created by kenta-yoshinaga on 2017/01/16.
  */
import breeze.linalg.DenseMatrix
import com.ibm.icu.text.Transliterator
import org.json4s.jackson.JsonMethods
import org.json4s.{DefaultFormats, _}

class LookupTable(path: String) {

  private val jsonValue = JsonMethods.parse(IOUtil.openIn(path))

  private val translitarator = Transliterator.getInstance("Halfwidth-Fullwidth")

  implicit private val formats = DefaultFormats
  private val tables =  jsonValue.extract[Map[String, Map[String, Map[String, String]]]]

  private val key2id = tables("_lookup")("_key2id")
  private val id2key = tables("_lookup")("_id2key")

  def encode(str: String): DenseMatrix[Double] = {
    val s = translitarator.transliterate(str)
    val strArray = s.map{x =>
      key2id.getOrElse(x.toString, "0").toDouble
    }.toArray
    new DenseMatrix(1, str.length, strArray)
  }

  def decode(data: DenseMatrix[Double]): String = data.map{x => (id2key.getOrElse(x.toInt.toString, "NONE"))}.toArray.mkString("")

  def getId(key: String): Int = key2id(key).toInt
  def getId(key: Char): Int = getId(key.toString)

  def getKey(id: Int): String = id2key(id.toString)

}

object LookupTable {
  def apply(path: String) = new LookupTable(path)
}
