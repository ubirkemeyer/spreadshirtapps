package net.sprd.api.model.basket

import net.sprd.api.ApiClient
import net.sprd.api.Entity
import net.sprd.api.ImageServer
import scala.xml.Elem
import scala.xml.NodeSeq

class BasketItem ( override val id: String,
                  var shopId :String,
                  var element: BasketItemElement,
                  var description: String,
                  var price :Double,
                  var currencyId :String,
                  var quantity :Int = 1) extends Entity{
  
  def this(data: => scala.xml.Node) {
    this((data \ "@id").text, (data \"shop"\ "@id").text, new BasketItemElement((data \"element")(0)),(data \"description").text,  (data \"price"\"vatIncluded").text.toDouble, (data \"price"\"currency"\"@id").text, (data \"quantity").text.toInt)
  }

  lazy val shop = ApiClient.shops(shopId)
  lazy val product =
    element.itemType match {
      case BasketItemElement.productItemType => shop.products(element.id)
      case BasketItemElement.articleItemType => shop.articles(element.id).product
      case _ => null
    }

  lazy val article =
    element.itemType match {
      case BasketItemElement.articleItemType => Some(shop.articles(element.id))
      case _ => None
    }

  lazy val appearanceId =
    if (element.properties.contains(BasketItemElement.propAppearance))
      element.properties(BasketItemElement.propAppearance)
  else product.appearanceId

  lazy val sizeId = element.properties(BasketItemElement.propSize)

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price*quantity)
  lazy val formattedSinglePrice = ApiClient.currencies(currencyId).format(price)

  

  def toXml : NodeSeq = {
    val xml = <shop id={shopId} />
        <description>{description}</description>
        <quantity>{quantity}</quantity>;
        
    if (id!=null && id !="") {
      <basketItem id ={id}>
        {xml}
        {element.toXml}
      </basketItem>
    } else <basketItem>
        {xml}
        {element.toXml}
      </basketItem>
  }
}
