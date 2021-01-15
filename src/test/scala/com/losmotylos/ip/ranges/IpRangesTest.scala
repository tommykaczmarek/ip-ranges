package com.losmotylos.ip.ranges

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class IpRangesTest extends FlatSpec with Matchers with TestData {

  it should "return mutually excluded ip addresses" in {
    IpRanges.symmetricDifference(contractScenario._1).right.get should contain theSameElementsAs contractScenario._2
  }

  it should "return error records for improper ip address" in {
    IpRanges.symmetricDifference(improperIpScenario._1).left.get shouldBe improperIpScenario._2
  }


}
