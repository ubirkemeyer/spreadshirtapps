package net.sprd.api

import scala.xml._
import access._
/** Defines a list entity which can be fetched also as list event with reduced params
*/
class ListEntity(data: => scala.xml.Node, full:Boolean) extends ReadOnlyEntity(data) {
  //fetch fullData of not already fetched
  lazy val fullData = 
    if (!full) 
      scala.xml.XML.load(HttpAccess.get((data\"@{http://www.w3.org/1999/xlink}href").text)) 
    else data

}
