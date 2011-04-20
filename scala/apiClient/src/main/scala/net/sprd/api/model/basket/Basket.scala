package net.sprd.api.model.basket

import net.sprd.api._
import scala.collection._
class Basket(override val id: String, 
             val token :String = "",
             val shopId :String,
             var userId :String = "",
             var basketItems:mutable.Set[BasketItem] = mutable.Set.empty
) extends Entity{
    
  def this(data: => scala.xml.Node) {
    this((data \ "@id").text,  (data \ "token").text,
         (data \"shop"\ "@id").text, (data \"user"\ "@id").text,
         mutable.Set.empty ++ ((data \ "basketItems"\ "basketItem") map ( new BasketItem(_))))
  }

  lazy val price = basketItems.foldLeft(0d)( (sum, b) => sum + b.price*b.quantity )
  lazy val currencyId= if (basketItems.isEmpty) "1" else basketItems.head.currencyId

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price)
    
  def toXml = <basket xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://api.spreadshirt.net"  id={id}>
    <token>{token}</token>
    <shop id={shopId} />
    <user id={userId} />
    <basketItems>
    {basketItems.map(_.toXml)}
    </basketItems>
              </basket>
        
  def createXml = <basket xmlns="http://api.spreadshirt.net"><token>{token}</token><shop id={shopId}/></basket>
  
  def add(shopId: String, element:BasketItemElement, description:String, quantity: Int =1, price:Double = 0.0, currencyId:String ="1") {
    val item = basketItems.find(b => b.element == element)
    item match {
      case Some(it) => it.quantity += quantity
        it
      case _ => val it =new BasketItem("",shopId,element, description, price, currencyId, quantity)
        basketItems+= it
        it
    }
  }


}
