/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api

abstract class XmlEntityListCache[B<:Entity](baseUrl:String, tagName:String) extends EntityListCache[B](baseUrl, tagName, null) {

  entityCache = new XmlEntityCache[B](baseUrl, tagName){
    def loadXml(data: => scala.xml.Node, full:Boolean) = XmlEntityListCache.this.loadXml(data, full)
  }
  
  def loadXml(data: => scala.xml.Node, full:Boolean):B;

}

