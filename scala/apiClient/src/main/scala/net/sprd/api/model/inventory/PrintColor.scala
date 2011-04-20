package net.sprd.api.model.inventory


import net.sprd.api._
import net.sprd.api.model._

class PrintColor(root :RootResource, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {
  lazy val name = (data \ "name").text
  lazy val price = (data \ "price" \ "vatIncluded").text.toDouble
  lazy val currencyId = (data \"price"\"currency"\"@id").text
  lazy val fill = (data \ "fill").text

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price)
    
}
