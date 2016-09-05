package jukaiScala.hdflib

import reflect.macros.blackbox.Context

import language.experimental.macros

/**
  * Created by kenta-yoshinaga on 2016/09/02.
  */
object DataFile {

  def read(fid: Int, sym: Any): Unit = macro read_impr

  def read_impr(c: Context)(): c.Expr[Unit] = {
    c.universe.reify{ println("hello world.")}
  }
}

