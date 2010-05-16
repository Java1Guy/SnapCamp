/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 5:52:11 PM
 * To change this template use File | Settings | File Templates.
 */

package org.snapimpact.etl

import model.dto._
import _root_.scala.xml.XML
import model.FeedHtmlRemover
import org.specs.Specification
import _root_.org.specs.runner._
import org.snapimpact.model._

import org.slf4j.LoggerFactory
import net.liftweb.util._

class FeedHtmlRemoverSpecTest extends Runner(new FeedHtmlRemoverSpecs) with JUnit with Console

class FeedHtmlRemoverSpecs extends Specification
{
  "FeedHtmlRemoverSpec " should
  {
    "pass through results from XmlPreprocessor.postProcess" in {
      FeedHtmlRemover.htmlRemove("the break<br/> in the html is mostly in the plane") must equalIgnoreCase("the break\n in the html is mostly in the plane")}

      val volunteerOpportunity =
        <VolunteerOpportunity>
          <volunteerOpportunityID>2002</volunteerOpportunityID>
          <sponsoringOrganizationIDs><sponsoringOrganizationID>2</sponsoringOrganizationID></sponsoringOrganizationIDs>
          <title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title>
          <volunteersNeeded>3</volunteersNeeded>
          <dateTimeDurations>
          <dateTimeDuration>
          <openEnded>No</openEnded>
          <startDate>2009-01-01</startDate>
          <endDate>2009-05-31</endDate>
          <iCalRecurrence>FREQ=WEEKLY;INTERVAL=2</iCalRecurrence>
          <commitmentHoursPerWeek>2</commitmentHoursPerWeek>
          </dateTimeDuration>
          </dateTimeDurations>
          <locations>
          <location>
          <city>Mercer County</city>
          <region>NJ</region>
          <postalCode>08610</postalCode>
          </location>
          </locations>
          <audienceTags>
          <audienceTag>Teens</audienceTag>
          </audienceTags>
          <categoryTags>
          <categoryTag>Community</categoryTag>
          <categoryTag>Children &amp; Youth</categoryTag>
          </categoryTags>
          <skills>Be interested in promoting youth volunteerism. Be available two Tuesday evenings per month.</skills>
          <detailURL>http://www.volunteermatch.org/search/opp200517.jsp</detailURL>
          <description>The quick <p>brown fox<br/></p></description>
          <lastUpdated olsonTZ="America/Denver">2008-12-02T19:02:01</lastUpdated>
      </VolunteerOpportunity>


    "Process a description in a Volunteer Opportunity" in
      {
        val vo = VolunteerOpportunity.fromXML(volunteerOpportunity)
        println("vo desc="+vo.description.get) // TODO the <p> and <br/> are already gone
        FeedHtmlRemover.htmlRemove(vo).get mustEqual "The quick brown fox"  // TODO where's the \n?
      }


    "Process a description in a Feed Info" in
      {
        val subject = <FootprintFeed schemaVersion="0.1">
          <FeedInfo>
            <providerID>1</providerID> <providerName>Volunteer Match</providerName>
            <createdDateTime olsonTZ="America/Denver">2008-12-30T14:30:10.5</createdDateTime>
            <providerURL>http://www.volunteermatch.org</providerURL>
          </FeedInfo>
          <VolunteerOpportunities>
            {volunteerOpportunity}
          </VolunteerOpportunities>
        </FootprintFeed>
        val ff = FootprintFeed.fromXML(subject)
        FeedHtmlRemover.htmlRemove(ff).head.get mustEqual "The quick brown fox"
      }
  }
}
