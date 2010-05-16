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

class PostprocessingSpecTest extends Runner(new PostprocessingSpecs) with JUnit with Console

class PostprocessingSpecs extends Specification
{
  "PostprocessingSpec " should
    {
      "handle empty string" in
        {
          XmlPreprocessor.postProcess("") mustEqual ""
        }
      "leave basic text alone" in
        {
          XmlPreprocessor.postProcess("The quick brown fox") mustEqual "The quick brown fox"
        }
      "replace <br...> with line feed" in
        {
          XmlPreprocessor.postProcess("The quick<br>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<br/>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<br style='test'>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<brbrbr>brown fox") mustEqual "The quick\nbrown fox"
        }
      "remove </br ...>" in
        {
          XmlPreprocessor.postProcess("The quick</br  > brown fox") mustEqual "The quick brown fox"
          XmlPreprocessor.postProcess("The quick </br/>brown fox") mustEqual "The quick brown fox"
        }
      "replace <p> with line feed" in
        {
          XmlPreprocessor.postProcess("The quick<p>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<p/>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<p style='test'>brown fox") mustEqual "The quick\nbrown fox"
          XmlPreprocessor.postProcess("The quick<p>brown fox") mustEqual "The quick\nbrown fox"
        }
      "remove </p ...>" in
        {
          XmlPreprocessor.postProcess("The quick</p  > brown fox") mustEqual "The quick brown fox"
          XmlPreprocessor.postProcess("The quick </p/>brown fox") mustEqual "The quick brown fox"
        }
      "remove <...>" in
        {
          XmlPreprocessor.postProcess("The quick<> brown fox") mustEqual "The quick brown fox"
          XmlPreprocessor.postProcess("The quick <abc>brown fox") mustEqual "The quick brown fox"
          XmlPreprocessor.postProcess("The quick <a b c>brown fox") mustEqual "The quick brown fox"
        }
      "remove multipl tags" in
        {
          XmlPreprocessor.postProcess("The quick <p>brown fox<br/></p>") mustEqual "The quick \nbrown fox\n"
        }

    }
}