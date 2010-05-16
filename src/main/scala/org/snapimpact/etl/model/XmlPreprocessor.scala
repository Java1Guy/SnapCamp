package org.snapimpact.etl

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: May 7, 2010
 * Time: 10:35:30 PM
 * from http://www.hars.de/2009/01/html-as-xml-in-scala.html
 */

import model.dto.VolunteerOpportunity
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.ccil.cowan.tagsoup.{XMLWriter, HTMLSchema, Parser}
import org.xml.sax.{InputSource, ContentHandler, XMLReader}

import _root_.java.io.ByteArrayOutputStream
import _root_.java.io.ByteArrayInputStream
import _root_.java.io.OutputStreamWriter
import xml.XML
import net.liftweb.common.{Empty, Full}

object XmlPreprocessor {
  def preProcess(url:String) = {
//    val theDoc = new TagSoupFactoryAdapter load url
//    println("Result of '"+url+"' = "+theDoc.toString)
//    theDoc
  }
  def preProcessString(str:String) = {
    val r: XMLReader = new Parser
    val theSchema = new HTMLSchema
    r.setProperty(Parser.schemaProperty, theSchema);
    val os = new ByteArrayOutputStream(str.length)
    val w = new OutputStreamWriter(os)
    val x = new XMLWriter(w)

    x.setPrefix(theSchema.getURI(), "")
    r.setContentHandler(x);
    val s = new InputSource
    
    s.setByteStream(new ByteArrayInputStream(str.getBytes))
    r.parse(s);
//    println("Result of '"+str+"' = "+os.toString)
    val theXML = XML.loadString(os.toString)
  }

  def postProcess(vo:Option[String]) =
  // replace <br ...> with line feed
  // remove </br ...>
  // replace <p> with line feed
  // remove </p ...>
  // remove <...>
    if (!vo.isEmpty) {
        Option(vo.get.replaceAll("<br[^>]*>", "\n")
          .replaceAll("</br[^>]*>", "")
          .replaceAll("<p[^>]*>", "\n")
          .replaceAll("</p[^>]*>", "")
          .replaceAll("<[^>]*>", ""))
    } else {
      vo
    }

}