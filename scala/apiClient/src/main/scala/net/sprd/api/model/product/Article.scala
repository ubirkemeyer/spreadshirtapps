package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._
import scala.xml.XML

class Article (val shop: Shop, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full)  {

  lazy val name = (data \ "name").text
  lazy val description = (fullData \ "description").text
  lazy val price = (data \ "price" \ "vatIncluded").text.toDouble
  lazy val currencyId = (data \ "price" \ "currency" \ "@id").text
  lazy val imageUrl = data \ "resources" \ "resource"\"@{http://www.w3.org/1999/xlink}href"
  lazy val productId = (fullData \ "product"\"@id").text
  
  lazy val product = shop.products(productId);

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price)
}
