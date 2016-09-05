package jukaiScala.hdflib

/**
  * Created by kenta-yoshinaga on 2016/09/02.
  */
trait HDF5Const {
  // iteration order constants
  val H5_ITER_UNKNOWN = -1
  val H5_ITER_INC     = 0
  val H5_ITER_DEC     = 1
  val H5_ITER_NATIVE  = 2
  val H5_ITER_N       = 3
  // indexing type constants
  val H5_INDEX_UNKNOWN   = -1
  val H5_INDEX_NAME      = 0
  val H5_INDEX_CRT_ORDER = 1
  // dataset constants
  val H5D_COMPACT      = 0
  val H5D_CONTIGUOUS   = 1
  val H5D_CHUNKED      = 2
  // error-related constants
  val H5E_DEFAULT      = 0
  // file access modes
  val H5F_ACC_RDONLY   = 0x00
  val H5F_ACC_RDWR     = 0x01
  val H5F_ACC_TRUNC    = 0x02
  val H5F_ACC_EXCL     = 0x04
  val H5F_ACC_DEBUG    = 0x08
  val H5F_ACC_CREAT    = 0x10
  // object types
  val H5F_OBJ_FILE     = 0x0001
  val H5F_OBJ_DATASET  = 0x0002
  val H5F_OBJ_GROUP    = 0x0004
  val H5F_OBJ_DATATYPE = 0x0008
  val H5F_OBJ_ATTR     = 0x0010
  val H5F_OBJ_ALL      = (H5F_OBJ_FILE|H5F_OBJ_DATASET|H5F_OBJ_GROUP|H5F_OBJ_DATATYPE|H5F_OBJ_ATTR)
  val H5F_OBJ_LOCAL    = 0x0020
  // other file constants
  val H5F_SCOPE_LOCAL   = 0
  val H5F_SCOPE_GLOBAL  = 1
  val H5F_CLOSE_DEFAULT = 0
  val H5F_CLOSE_WEAK    = 1
  val H5F_CLOSE_SEMI    = 2
  val H5F_CLOSE_STRONG  = 3
  // object types (C enum H5Itype_t)
  val H5I_FILE         = 1
  val H5I_GROUP        = 2
  val H5I_DATATYPE     = 3
  val H5I_DATASPACE    = 4
  val H5I_DATASET      = 5
  val H5I_ATTR         = 6
  val H5I_REFERENCE    = 7
  // Link constants
  val H5L_TYPE_HARD    = 0
  val H5L_TYPE_SOFT    = 1
  val H5L_TYPE_EXTERNAL= 2
  // Object constants
  val H5O_TYPE_GROUP   = 0
  val H5O_TYPE_DATASET = 1
  val H5O_TYPE_NAMED_DATATYPE = 2
  // Property constants
  val H5P_DEFAULT = 0
}
