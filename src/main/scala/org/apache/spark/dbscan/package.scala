package org.apache.spark

import org.apache.spark.dbscan.spatial.Point
import org.apache.spark.rdd.RDD

import scala.collection.mutable

/** Contains implementation of distributed DBSCAN algorithm as well as tools for exploratory analysis.
  *
  *
  */
package object dbscan {

  /** Represents one record in a dataset
    *
    */
  type PointCoordinates = mutable.WrappedArray.ofDouble

  /** Represents a dataset which needs to be clustered
    *
    */
  type RawDataSet = RDD[Point]

  /** Unique point ID in a data set
   *
   */
  private [dbscan] type PointId = Long

  private [dbscan] type TempPointId = Int

  /** Unique id of a box-shaped region in a data set
   *
   */
  private [dbscan] type BoxId = Int

  /** Cluster ID
   *
   */
  type ClusterId = Long

  /** A pair of IDs of density-based partitions adjacent to each other
   *
   */
  private [dbscan] type PairOfAdjacentBoxIds = (BoxId, BoxId)
}
