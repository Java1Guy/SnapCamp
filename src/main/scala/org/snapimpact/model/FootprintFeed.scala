package org.snapimpact.model

import net.liftweb.http.{IntField, StringField}
import com.eltimn.scamongo.field.{ObjectIdField, DBRefField}
import com.eltimn.scamongo.{MongoMetaRecord, MongoId, MongoRecord}

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Mar 30, 2010
 * Time: 8:28:15 PM
 * To change this template use File | Settings | File Templates.
 */

class FootprintFeed extends MongoRecord[FootprintFeed] with MongoId[FootprintFeed] {
  def meta = FootprintFeed
  object name extends StringField() // this, 12
  object cnt extends IntField(this)
  object refdoc extends DBRefField[FootprintFeed, RefDoc](this, RefDoc)
  object refdocId extends ObjectIdField(this) {
    def fetch = RefDoc.find(value)
  }
}
object FootprintFeed extends FootprintFeed with MongoMetaRecord[FootprintFeed]
class RefDoc extends MongoRecord[RefDoc] with MongoId[RefDoc] {
	def meta = RefDoc
}
object RefDoc extends RefDoc with MongoMetaRecord[RefDoc]