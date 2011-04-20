package examples

import java.net.URI
import net.sprd.api.ApiClient
import net.sprd.api.model.basket.BasketItemElement

object BasketExample extends Application{

  //configure APIClient
  ApiClient.baseUrl = "http://api.spreadshirt.net/api/v1"
  //set apiKey and secret for create Basket in API
  ApiClient.apiKey = "123456789"
  ApiClient.secret = "987654321"

  //define your shopId
  val shopId = "282267";
  val shop = ApiClient.shops(shopId)

  //create a Basket for the shop
  //if a Basket with the Token already exists, it is loaded
  var basket = ApiClient.createBasket(shop, "My Session Token")
  println("BasketId: "+basket.id)

  //select article of your shop to buy
  val article = shop.articles("6207616")
  //create basketItem  with appearance and size
  val element = BasketItemElement(article, "3", "2")
  //add it to the basket
  basket.add(shopId, element, article.name, 1, article.price, article.currencyId)
  println("FullBasket= "+basket.toXml.toString)

  //update Basket on API
  val responseCode = ApiClient.updateBasket(basket)
  println("ResponseCode= "+responseCode)

  //reload basket from API
  basket = ApiClient.getBasket(basket.id)
  println("StoredBasket= "+basket.toXml.toString)
  println("Price= "+basket.formattedPrice)

  //get Checkout Url
  val checkoutUrl = ApiClient.getBasketCheckoutUrl(basket.id)
  println("CheckoutUrl:"+checkoutUrl)

  //if Desktop Mode: open Browser with Basket in Checkout
  if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported( java.awt.Desktop.Action.BROWSE ))
    java.awt.Desktop.getDesktop().browse(new URI(checkoutUrl))
  
}
