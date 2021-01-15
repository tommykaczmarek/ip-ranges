package com.losmotylos.ip.ranges

import com.risksense.ipaddr.{IpAddress, IpRange, IpSet}

import scala.util.{Failure, Success, Try}


case class IpRange2(from: String, to: String)
case class IpRange2ValidationError(error: String, improperRanges: Seq[IpRange2])

object IpRanges {
  /**
   * Returns mutually exclusive ranges basing on input ranges in IPv4.
   *
   * @return
   * - Error record for improper IP ranges
   * - ranges that are mutually exclusive
   **/
  def symmetricDifference(input: Seq[IpRange2]): Either[IpRange2ValidationError, Seq[IpRange2]] = {
    validateRanges(input) match {
      case Left(v) => Left(v)
      case Right(symmetricDiffRanges) =>
        Right(symmetricDifferenceOnContiguousIpRanges(symmetricDiffRanges)
          .map(r => IpRange2(IpAddress(r.first).toString, IpAddress(r.last).toString)
        ))
    }
  }

  private def symmetricDifferenceOnContiguousIpRanges(input: Seq[IpRange]): Seq[IpRange] = {
    val inputRanges =
      input
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

    symmetricDiff
      .networkSeq
      .sorted
      .map(n => IpRange(IpAddress(n.first).toString, IpAddress(n.last).toString))
      .map(Seq(_))
      .reduceLeft((a, b) => join(a, b))
  }

  private def validateRanges(addresses: Seq[IpRange2]): Either[IpRange2ValidationError, Seq[IpRange]] = {
    Try(addresses.map(range => IpRange(range.from, range.to))) match {
      case Success(value) => Right(value) // todo add validateContiguous
      case Failure(_) => Left(IpRange2ValidationError("Failed to parse IpAddress", addresses))
    }
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
