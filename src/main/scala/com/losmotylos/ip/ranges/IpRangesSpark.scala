package com.losmotylos.ip.ranges

import org.apache.log4j.Logger
import org.apache.spark.sql.{Dataset, SparkSession}

case class IpRangesMessage(uri: String, ranges: Seq[IpRange2])
// todo add IpRangesError containing uri

object IpRangesSpark {
  val logger: Logger = Logger.getLogger(this.getClass)

  def processIpRanges(input: Dataset[IpRangesMessage])(implicit spark: SparkSession): Dataset[(Option[IpRange2ValidationError], Option[IpRangesMessage])] = {
    import spark.implicits._

    input
      .map { inputRanges =>
        logger.info(s"Processing input for URI [${inputRanges.uri}]")
        IpRanges.symmetricDifference(inputRanges.ranges) match {
          case Left(err) =>
            logger.warn(s"Failed to process input for URI [${inputRanges.uri}], got error [${err.error}]")
            logger.debug(s"Got error [${err.error}] for URI [${inputRanges.uri}], input message [${err.improperRanges}]")
            (Some(err), None)
          case Right(symmetricDiffRanges) =>
            logger.info(s"Input processed successfully for URI [${inputRanges.uri}]")
            logger.debug(s"Processed input successfully for URI [${inputRanges.uri}], got [${inputRanges.ranges}] returning [$symmetricDiffRanges]")
            (None, Some(inputRanges.copy(ranges = symmetricDiffRanges)))
        }
      }
  }
}
