/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 5:52:11 PM
 * To change this template use File | Settings | File Templates.
 */

package org.snapimpact.etl

import model.dto._
import org.specs.Specification
import _root_.org.specs.runner._
import scala.xml.XML

class PreprocessingSpecTest extends Runner(new PreprocessingSpecs) with JUnit with Console

class PreprocessingSpecs extends Specification
{
  val volOppXmlGood = <VolunteerOpportunity>
            <volunteerOpportunityID>2002</volunteerOpportunityID>
            <sponsoringOrganizationIDs>
              <sponsoringOrganizationID>2</sponsoringOrganizationID>
            </sponsoringOrganizationIDs>
            <title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title>
            <volunteersNeeded>3</volunteersNeeded>
            <dateTimeDurations>
              <dateTimeDuration>
                <openEnded>No</openEnded> <startDate>2009-01-01</startDate> <endDate>2009-05-31</endDate>
                <iCalRecurrence>FREQ=WEEKLY;INTERVAL=2</iCalRecurrence>
                <commitmentHoursPerWeek>2</commitmentHoursPerWeek>
              </dateTimeDuration>
            </dateTimeDurations> <locations>
              <location>
                <city>Mercer County</city> <region>NJ</region> <postalCode>08610</postalCode>
              </location>
            </locations> <audienceTags>
              <audienceTag>Teens</audienceTag>
            </audienceTags> <categoryTags>
              <categoryTag>Community</categoryTag> <categoryTag>Children
                &amp;
                Youth</categoryTag>
            </categoryTags> <skills>Be interested in promoting youth volunteerism.Be available two Tuesday
              evenings per month.</skills>
            <detailURL>http://www.volunteermatch.org/search/opp200517.jsp</detailURL>
            <description>Quixote Quest is a volunteer club for teens who have a passion for community service.
              The teens each volunteer for their own specific cause.
              <p>Twice monthly, the club meets.At the club meetings the teens from different high schools come
              together for two hours to talk about their volunteer experiences and spend some hang-out time
              together that helps them bond as fraternity...family.</p><b>Quixote Quest</b> is seeking young adults
              roughly between 20 and 30 years of age who would be interested in being a guide and advisor
              to the teens during these two evening meetings a month.</description>
            <lastUpdated olsonTZ="America/Denver">2008-12-02T19:02:01</lastUpdated>
          </VolunteerOpportunity>
  val volOppXmlBad = "<VolunteerOpportunity>"+
            "<volunteerOpportunityID>2002</volunteerOpportunityID>"+
            "<sponsoringOrganizationIDs>"+
              "<sponsoringOrganizationID>2</sponsoringOrganizationID>"+
            "</sponsoringOrganizationIDs>"+
            "<title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title>"+
            "<volunteersNeeded>3</volunteersNeeded>"+
            "<dateTimeDurations>"+
              "<dateTimeDuration>"+
                "<openEnded>No</openEnded> <startDate>2009-01-01</startDate> <endDate>2009-05-31</endDate>"+
                "<iCalRecurrence>FREQ=WEEKLY;INTERVAL=2</iCalRecurrence>"+
                "<commitmentHoursPerWeek>2</commitmentHoursPerWeek>"+
              "</dateTimeDuration>"+
            "</dateTimeDurations> <locations>"+
              "<location>"+
                "<city>Mercer County</city> <region>NJ</region> <postalCode>08610</postalCode>"+
              "</location>"+
            "</locations> <audienceTags>"+
              "<audienceTag>Teens</audienceTag>"+
            "</audienceTags> <categoryTags>"+
              "<categoryTag>Community</categoryTag> <categoryTag>Children &amp; Youth</categoryTag>"+
            "</categoryTags> <skills>Be interested in promoting youth volunteerism.Be available two Tuesday evenings per month.</skills>"+
            "<detailURL>http://www.volunteermatch.org/search/opp200517.jsp</detailURL>"+
            "<description>Quixote Quest is a volunteer club for teens who have a passion for community service."+
              "The teens each volunteer for their own specific cause."+
              "<p>Twice monthly, the club meets.<br>At the club meetings the teens from different high schools come"+
              "together for two hours to talk about their volunteer experiences and spend some hang-out time"+
              "together that helps them bond as fraternity...family.<b>Quixote Quest</b> is seeking young adults"+
              "roughly between 20 and 30 years of age who would be interested in being a guide and advisor"+
              "to the teens during these two evening meetings a month.</description>"+
            "<lastUpdated olsonTZ=\"America/Denver\">2008-12-02T19:02:01</lastUpdated>"+
          "</VolunteerOpportunity>"

  "PreprocessingSpec " should
    {
      "handle good html in VO descriptions" in
        {
          val item = VolunteerOpportunity.fromXML(volOppXmlGood)
          item.volunteerOpportunityID mustEqual "2002"
          item.sponsoringOrganizationIDs.size mustEqual 1
          item.sponsoringOrganizationIDs.head mustEqual "2"
          item.title must find(".*MERCER")
          item.volunteersNeeded must beSome(3)
          item.skills must beSomething
          item.detailURL must beSomething
          item.description must beSome[String].which(_.startsWith("Quixote"))
          item.lastUpdated must beSomething
        }
      "handle bad html in VO descriptions" in
        {
          val theXml = XmlPreprocessor.preProcessString(volOppXmlBad)
          val item = VolunteerOpportunity.fromXML(theXml)
          item.volunteerOpportunityID mustEqual "2002"
          item.sponsoringOrganizationIDs.size mustEqual 1
          item.sponsoringOrganizationIDs.head mustEqual "2"
          item.title must find(".*MERCER")
          item.volunteersNeeded must beSome(3)
          item.skills must beSomething
          item.detailURL must beSomething
          item.description must beSome[String].which(_.startsWith("Quixote"))
          item.lastUpdated must beSomething
        }
    }
}