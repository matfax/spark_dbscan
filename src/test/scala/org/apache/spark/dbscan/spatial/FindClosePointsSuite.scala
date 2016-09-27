package org.apache.spark.dbscan.spatial

import org.apache.spark.dbscan.spatial.rdd.PointsPartitionedByBoxesRDD
import org.apache.spark.dbscan.{SuiteBase, DbscanSettings}


class FindClosePointsSuite extends SuiteBase {
  val dataset1 = sc.parallelize(Array (
    Point(0.0, 0.0), Point(0.5, 4.0), Point(4.0, 2.5),
    Point(0.5, 0.5), Point(0.5, 0.9), Point(0.9, 0.9),
    Point(0.5, 1.3),
    Point(1.1, 0.9),
    Point(1.1, 1.1), Point(1.3, 1.3),
    Point(2.5, 1.5), Point(2.9, 1.5), Point(3.3, 1.5)
  ))

  val dataset1_1 = sc.parallelize(Array ( Point(0.0, 0.0), Point(1.0, 0.0),
      Point(0.5, 1.0), Point(0.5, 0.5), Point(0.5, 5.0) ))

  val settings = new DbscanSettings ().withEpsilon (0.4)

  val partitionedData = PointsPartitionedByBoxesRDD (dataset1, dbscanSettings = settings)
  val boxes = partitionedData.boxes
  val boundingBox = partitionedData.boundingBox

  test ("DistanceAnalyzer should find close points within and across boxes") {


    val distanceAnalyzer = new DistanceAnalyzer (settings)

    println ("All boxes:")
    boxes.foreach ( println )
    println ("----------\n")

    println ("All points:")
    partitionedData.collect ().foreach ( x => println (x._2) )
    println ("----------\n")

    println ("Indexed points:")
    PointsPartitionedByBoxesRDD.extractPointIdsAndCoordinates(partitionedData).collect ().foreach ( println )
    println ("----------\n")

    val closePointsWithinBoxes = distanceAnalyzer.countClosePointsWithinEachBox(partitionedData)
    println ("Pairs of close points within boxes:")
    closePointsWithinBoxes.collect ().foreach( println )
    println ("----------\n")

    val pointsCloseToBounds = distanceAnalyzer.findPointsCloseToBoxBounds(partitionedData, partitionedData.boxes, settings.epsilon)
    println ("Points close to box boundaries:")
    pointsCloseToBounds.collect ().foreach( println )
    println ("----------\n")

    val closePointsAcrossBoxes = distanceAnalyzer.countClosePointsInDifferentBoxes(pointsCloseToBounds, partitionedData.boxes, settings.epsilon)
    println ("Close points which reside in different boxes:")
    closePointsAcrossBoxes.collect ().foreach( println )
    println ("----------\n")

    // TODO: add assertions
  }

  test("DistanceAnalyzer should find close points properly") {
    val settings = new DbscanSettings ().withEpsilon(0.8)
    val partitionedAndSortedData = PointsPartitionedByBoxesRDD (dataset1_1, dbscanSettings = settings)

    val distanceAnalyzer = new DistanceAnalyzer(settings)

    val closePointTuples = distanceAnalyzer.countClosePoints(partitionedAndSortedData)

    closePointTuples.foreach( println )

    // TODO: add assertions
  }

  test("DistanceAnalyzer should Count neighbors of points properly") {

    val settings = new DbscanSettings ().withEpsilon(0.8)
    val partitionedAndSortedData = PointsPartitionedByBoxesRDD (dataset1_1, dbscanSettings = settings)

    val distanceAnalyzer = new DistanceAnalyzer(settings)

    val pointsWithCounts = distanceAnalyzer.countNeighborsForEachPoint(partitionedAndSortedData)

    pointsWithCounts.foreach( println )

    // TODO: add assertions

  }
}
