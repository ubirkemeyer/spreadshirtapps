package net.sprd.api

import scala.xml.XML

abstract class XmlEntityCache [B<:Entity](baseUrl:String, tagName:String) extends TimeEntityCache[B]{

  protected def load(id:java.lang.String):B = {
    loadXml(XML.load(baseUrl+"/"+id), true)
  }
  
  /** Load Entity from XML data
   * 
  */
  def loadXml(data: => scala.xml.Node, full: Boolean):B
}
