package org.snapimpact.snippet

import _root_.net.liftweb._
import http._
import mapper._
import S._
import SHtml._

import common._
import util._
import Helpers._

import _root_.java.util.Locale
import _root_.java.io.ByteArrayInputStream
import org.snapimpact.etl.model.dto.FootprintFeed
import xml.{XML, NodeSeq, Text, Group}
import org.snapimpact.model.MemoryFeedStore

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Mar 16, 2010
 * Time: 7:36:23 PM
 * To change this template use File | Settings | File Templates.
 */

class XmlUploadSnippet {
/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


  // the request-local variable that hold the file parameter
  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  /**
   * Bind the appropriate XHTML to the form
   */
  def upload(xhtml: NodeSeq): NodeSeq =
    if (S.get_?) bind("ul", chooseTemplate("choose", "get", xhtml),
                    "file_upload" -> fileUpload(ul => theUpload(Full(ul))))
    else {
      var itemCount = 0
      val db = new MemoryFeedStore
      theUpload.is.map(v => {
        val theXml = XML.load(new ByteArrayInputStream(v.file))
        val ff = FootprintFeed.fromXML(theXml)
        db.create(ff)
        itemCount = itemCount + ff.opportunities.opps.length
      })
      bind("ul", chooseTemplate("choose", "post", xhtml),
            "results" -> Text(itemCount.toString)
      )
    }


  def lang(xhtml: NodeSeq): NodeSeq =
    bind("showLoc", xhtml,
       "lang" -> locale.getDisplayLanguage(locale),
       "select" -> selectObj(locales.map(lo => (lo, lo.getDisplayName)),
                             Full(definedLocale.is), setLocale))

  private def locales =
    Locale.getAvailableLocales.toList.sortWith(_.getDisplayName < _.getDisplayName)

  private def setLocale(loc: Locale) = definedLocale.set(loc)
}

object definedLocale extends SessionVar(S.locale)