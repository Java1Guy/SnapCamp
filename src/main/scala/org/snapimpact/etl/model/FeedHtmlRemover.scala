package org.snapimpact.etl.model

import dto.{VolunteerOpportunity, FootprintFeed}
import org.snapimpact.etl.XmlPreprocessor

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: May 15, 2010
 * Time: 7:43:23 PM
 */

object FeedHtmlRemover {
  // Business logic to remote HTML from certain fields in the opportunities
  // description and ??

  def htmlRemove(ff:FootprintFeed): List[Option[String]]  = {
    ff.opportunities.opps.map( (vo:VolunteerOpportunity) =>
      htmlRemove(vo))
  }

  def htmlRemove(vo:VolunteerOpportunity): Option[String] = {
    vo.description.map( (desc:String) => htmlRemove(desc))
  }

  def htmlRemove(desc:String):String = {
    XmlPreprocessor.postProcess(desc)
  }
}