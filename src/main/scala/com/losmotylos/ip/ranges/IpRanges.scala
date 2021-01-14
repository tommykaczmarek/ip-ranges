package com.losmotylos.ip.ranges

import com.risksense.ipaddr.{IpAddress, IpRange, IpSet}

case class IpRange2(from: String, to: String)

object IpRanges {

  /**
   * Returns mutually exclusive ranges basing on input ranges in IPv4.
   * @throws RuntimeException when any of the range is not contiguous
   **/
  def symmetricDifference(input: Seq[IpRange2]): Seq[IpRange2] = {
    // todo add checking if ips are correct and contiguous

    val inputRanges =
      input
        .map(range => IpRange(range.from, range.to))
        .map(IpSet(_))

    val symmetricDiffInput = inputRanges.reduceLeft((a, b) => a ^ b)

    val symmetricDiffIntersections = (3 until inputRanges.size).map { i =>
      val slidingIntersections = (0 to inputRanges.size - i).map { j =>
        inputRanges
          .slice(j, j + i)
          .reduceLeft((a, b) => a & b)
      }

      slidingIntersections.reduceLeft((a, b) => a ^ b)
    }.reduceLeft((a, b) => a ^ b)

    val symmetricDiff = symmetricDiffInput ^ symmetricDiffIntersections

    val symmetricDiffRanges =
      symmetricDiff
        .networkSeq
        .sorted
        .map(n => IpRange(IpAddress(n.first).toString, IpAddress(n.last).toString))
        .map(Seq(_))
        .reduceLeft((a, b) => join(a, b))

    symmetricDiffRanges.map(r =>
      IpRange2(IpAddress(r.first).toString, IpAddress(r.last).toString)
    )
  }

  /**
   * Joins sorted network to one range if union is contiguous
   * Like:
   * 192.168.0.10 - 192.168.0.12
   * 192.168.0.13 - 192.168.0.15
   *
   * Result will be:
   * 192.168.0.10 - 192.168.0.15
   *
   * Assumptions:
   * - all elements in previous are sorted and contiguous
   * - next is bigger than previous
   **/
  private def join(previous: Seq[IpRange], next: Seq[IpRange]): Seq[IpRange] = {
    val previousLastPlusNext = IpSet(previous.last) | IpSet(next.last)
    if (previousLastPlusNext.isContiguous) {
      val allButLast: Seq[IpRange] = previous.slice(0, previous.size - 1)
      allButLast :+ previousLastPlusNext.ipRange
    } else {
      previous ++ next
    }
  }

}
