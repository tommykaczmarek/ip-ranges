package com.losmotylos.ip.ranges

import org.apache.spark.sql.SparkSession
import org.scalatest.{FlatSpec, Matchers}

trait SparkTest extends FlatSpec with Matchers {
  lazy val spark: SparkSession = SparkSession.builder()
    .appName("spark3-hot-check")
    .master("local[*]")
    .getOrCreate()
}
