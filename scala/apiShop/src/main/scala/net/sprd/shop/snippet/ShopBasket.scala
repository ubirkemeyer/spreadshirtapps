/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.snippet

import net.liftweb.http.S
import net.sprd.api.ApiClient
import net.sprd.shop.lib.ImageUrl
import net.sprd.shop.lib.Shop
import scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers._

class ShopBasket {

  val imageSize=280

  def list(xHtml: NodeSeq) : NodeSeq = {
    val shop = ApiClient.shops(Shop.getShopId())
    var basketId = S.param("basketId").getOrElse("")
    var basket = net.sprd.shop.lib.Basket(shop.id)
    NodeSeq.Empty ++ basket.basketItems.flatMap(a => {
      a.shopId = shop.id;
      bind("basketitem", xHtml,
           ("price" -> <span style="font-size: 25px; font-weight: bold;">{"%1.2f €".format(a.price*a.quantity)}<br/></span>),
           ("image" -> (<img width={""+imageSize} height={""+imageSize} src={ImageUrl.productUrl(a.product, a.product.defaultViewId, a.appearanceId,imageSize)} />)),
           ("size" -> <span>{a.product.productType.sizeNames(a.sizeId)}</span>)
      )
    }
    )
  }
}

object ShopBasket {
  def comet = {
    <lift:comet type="CometBasket" name="Other">
         Basket: <basket:content>Empty Basket</basket:content>
   </lift:comet>

  }
}
