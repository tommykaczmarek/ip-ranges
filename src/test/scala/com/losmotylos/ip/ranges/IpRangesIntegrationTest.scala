package com.losmotylos.ip.ranges

import org.apache.spark.sql.SparkSession
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IpRangesIntegrationTest extends SparkTest with TestData {
  implicit val s: SparkSession = spark

  val uri: String = uriGen.sample.get

  it should "run proper ipRanges in spark" in {
    import spark.implicits._
    val input = Seq(IpRangesMessage(uri, contractScenario._1)).toDS()
    val result = IpRangesSpark.processIpRanges(input).collect()(0)

    result shouldBe (None, Some(IpRangesMessage(uri, contractScenario._2)))
  }

  it should "run improper ipRanges in spark and return error" in {
    import spark.implicits._
    val input = Seq(IpRangesMessage(uri, improperIpScenario._1)).toDS()
    val result = IpRangesSpark.processIpRanges(input).collect()(0)

    result shouldBe (Some(improperIpScenario._2), None)
  }
}
