package net.sprd.api.model

import net.sprd.api._
import net.sprd.api.model.product._

class User(url:String, data: => scala.xml.Node) extends RootResource(url, data)  {
  
  lazy val productUrl = (data \ "products" \"@{http://www.w3.org/1999/xlink}href").text
  
  lazy val products = new XmlEntityListCache[Product](productUrl,"product") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new Product(User.this, data, full)
  }
}
