package org.snapimpact.etl.model

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: May 7, 2010
 * Time: 10:35:30 PM
 * from http://www.hars.de/2009/01/html-as-xml-in-scala.html
 */

import _root_.org.xml.sax.{XMLReader,InputSource}
import _root_.scala.xml.{Node,TopScope}

import _root_.scala.xml.parsing.FactoryAdapter
import _root_.scala.xml.factory.NodeFactory
import _root_.scala.xml.{Elem,MetaData,NamespaceBinding,
			 Node,Text,TopScope}

trait NonBindingFactoryAdapter extends FactoryAdapter
                               with NodeFactory[Elem] {

  def nodeContainsText(localName: String) = true

  // methods for NodeFactory[Elem]
  /** constructs an instance of scala.xml.Elem */
  protected def create(pre: String, label: String,
                       attrs: MetaData, scpe: NamespaceBinding,
		       children: Seq[Node]): Elem =
    Elem( pre, label, attrs, scpe, children:_* )


  // -- methods for FactoryAdapter
  def createNode(pre: String, label: String,
                 attrs: MetaData, scpe: NamespaceBinding,
                 children: List[Node] ): Elem =
    Elem( pre, label, attrs, scpe, children:_* )

  def createText(text: String) = Text(text)

  def createProcInstr(target: String, data: String) =
    makeProcInstr(target, data)
}

trait SAXFactoryAdapter extends NonBindingFactoryAdapter {

  /** The method [getReader] has to implemented by
      concrete subclasses */
  def getReader() : XMLReader;

  override def loadXML(source : InputSource) : Node = {
    val reader = getReader()
    reader.setContentHandler(this)
    scopeStack.push(TopScope)
    reader.parse(source)
    scopeStack.pop
    return rootElem
  }
}

trait HTMLFactoryAdapter extends FactoryAdapter {

  val emptyElements = Set("area", "base", "br", "col", "hr", "img",
                          "input", "link", "meta", "param")

  def nodeContainsText(localName: String) =
    !(emptyElements contains localName)
}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl

class TagSoupFactoryAdapter extends SAXFactoryAdapter
                            with HTMLFactoryAdapter {
  private val parserFactory = new SAXFactoryImpl
  parserFactory.setNamespaceAware(true)

  def getReader() = parserFactory.newSAXParser().getXMLReader()
}

object XmlPreprocessor {
  def preProcess(url:String) {
    val theDoc = new TagSoupFactoryAdapter load url
    println("Result of '"+url+"' = "+theDoc.toString)
    theDoc
  }
}