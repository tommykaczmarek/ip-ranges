package com.losmotylos.ip.ranges

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class IpRangesTest extends FlatSpec with Matchers {

  it should "return mutually excluded ip addresses" in {
    val input = Seq(
      IpRange2("197.203.0.0", "197.206.9.255"),
      IpRange2("197.204.0.0", "197.204.0.24"),
      IpRange2("201.233.7.160", "201.233.7.168"),
      IpRange2("201.233.7.164", "201.233.7.168"),
      IpRange2("201.233.7.167", "201.233.7.167"),
      IpRange2("203.133.0.0", "203.133.255.255")

    )

    IpRanges.symmetricDifference(input) should contain theSameElementsAs(
      Seq(
        IpRange2("197.203.0.0", "197.203.255.255"),
        IpRange2("197.204.0.25", "197.206.9.255"),
        IpRange2("201.233.7.160", "201.233.7.163"),
        IpRange2("203.133.0.0", "203.133.255.255")
      )
    )

  }

}
