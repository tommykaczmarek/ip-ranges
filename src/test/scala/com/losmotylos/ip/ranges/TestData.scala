package com.losmotylos.ip.ranges

import org.scalacheck.Gen

trait TestData {

  def uriGen: Gen[String] = Gen.alphaNumStr

  val contractScenario: (Seq[IpRange2], Seq[IpRange2]) = (
    Seq(
      IpRange2("197.203.0.0", "197.206.9.255"),
      IpRange2("197.204.0.0", "197.204.0.24"),
      IpRange2("201.233.7.160", "201.233.7.168"),
      IpRange2("201.233.7.164", "201.233.7.168"),
      IpRange2("201.233.7.167", "201.233.7.167"),
      IpRange2("203.133.0.0", "203.133.255.255")

    ),
    Seq(
      IpRange2("197.203.0.0", "197.203.255.255"),
      IpRange2("197.204.0.25", "197.206.9.255"),
      IpRange2("201.233.7.160", "201.233.7.163"),
      IpRange2("203.133.0.0", "203.133.255.255")
    )
  )
  val improperIpInput = Seq(
    IpRange2("197.203.0.0", "197.206.9.255"),
    IpRange2("a.a.a.a", "197.204.0.24")
  )
  val improperIpScenario: (Seq[IpRange2], IpRange2ValidationError) = (
    improperIpInput,
    IpRange2ValidationError("Failed to parse IpAddress", improperIpInput)
  )
}
