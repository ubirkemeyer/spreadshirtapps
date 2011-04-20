/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import net.liftweb.http.S
import net.liftweb.http.provider.HTTPCookie
import net.sprd.api.ApiClient
import net.sprd.api.model.basket.BasketItemElement

object Basket {
  def apply(shopId:String) = {
    val shop = ApiClient.shops(shopId)
    val servletSession = S.containerSession.get
    var basketId = servletSession.attribute("basketId").asInstanceOf[String]
    if (basketId == null || basketId == "") {
      val basketIdCookie = S.findCookie("basketId");
      if (basketIdCookie.isEmpty) {
        val basket = ApiClient.createBasket(shop, S.session.get.uniqueId)
        val cookie = HTTPCookie("basketId", basket.id)
        cookie.setPath("/")
        cookie.setMaxAge(3600*24);
        S.addCookie(cookie)
        basketId = basket.id
      } else {
        basketId = basketIdCookie.get.value.get
      }
      servletSession.setAttribute("basketId", basketId)
    }
    ApiClient.getBasket(basketId);
  }

  def getBasket(basketId: String) = ApiClient.getBasket(basketId)


  def addArticle(shopId:String,articleId:String, appearanceId:String, sizeId:String) = {
    val shop = ApiClient.shops(shopId)
    val article = shop.articles(articleId)
    val element = BasketItemElement(article, sizeId, appearanceId)
    val basket = this(shopId)
    basket.add(shop.id, element, if (article.name.isEmpty) article.product.productType.name else article.name, 1, article.price, article.currencyId)
    println(basket.toXml.toString)
    ApiClient.updateBasket(basket)
    CometBasketMaster ! new CometBasketMaster.Updated(basket.id)
    basket
  }

  def addProduct(shopId:String,productId:String, appearanceId:String, sizeId:String) = {
    val shop = ApiClient.shops(shopId)
    val product = shop.products(productId)
    val element = BasketItemElement(product, sizeId, appearanceId)
    val basket = this(shopId)
    basket.add(shop.id, element, product.productType.name, 1)
    ApiClient.updateBasket(basket)
    CometBasketMaster ! new CometBasketMaster.Updated(basket.id)
    basket
  }

  def removeItem(basket:net.sprd.api.model.basket.Basket, basketItemId: String) {
    val item = basket.basketItems.find(_.id == basketItemId)
    if (item.isDefined) {
      basket.basketItems.remove(item.get)
      ApiClient.updateBasket(basket)
    }
    CometBasketMaster ! new CometBasketMaster.Updated(basket.id)
  }

  def getCheckoutUrl(basketId: String) = ApiClient.getBasketCheckoutUrl(basketId)
}
