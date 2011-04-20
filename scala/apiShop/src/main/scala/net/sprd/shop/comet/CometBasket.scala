/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.comet

import net.liftweb.common.Full
import net.liftweb.http.CometActor
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.SetHtml
import net.sprd.api.model.basket.BasketItem
import net.sprd.shop.lib.Basket
import net.sprd.shop.lib.CometBasketMaster
import net.sprd.shop.lib.ImageUrl
import net.sprd.shop.lib.Shop
import scala.xml.NodeSeq
import scala.xml.Text

class CometBasket extends CometActor {
  
  override def defaultPrefix = Full("basket")

  var basketId = ""

  var checkoutUrl = ""
  
  override def localSetup {
    basketId = Basket(Shop.getShopId()).id
    checkoutUrl = Basket.getCheckoutUrl(basketId)
    CometBasketMaster ! new CometBasketMaster.SubscribeBasket(this)
    super.localSetup()
  }

  override def localShutdown {
    CometBasketMaster ! CometBasketMaster.UnsubBasket(this)
    super.localShutdown()
  }

//  def render = {
//  def linkView(item: BasketItem): NodeSeq = {
//    <li>
//      {item.quantity+"x "+item.formattedPrice}
//    </li>
//  }
//
//    bind("content" -> <ol>{Basket.getBasket(basketId).basketItems.flatMap(linkView _)}</ol>)
//}

  override def render() = {
    bind("content" -> basketSpan)
  }

  def basketSpan = <span id="cometBasket">{content}</span>

  val imageSize=50
  val imgSize=25

  def content() = {
    val basket = Basket.getBasket(basketId);
    //val xml2 = items(basket)
    //++ <img width={""+imgSize} height={""+imgSize} src={ImageUrl.productUrl(item.product, item.appearanceId,imageSize)} />
    //println("Basket:   " + basket.createXml.toString)
    val xml2 = basket.basketItems.foldLeft(NodeSeq.Empty)((a:NodeSeq,item) => {
        //println("BasketItem:   " + item.toXml.toString)
        val span = <span style="color: #FF0000;">{item.quantity.toString}</span>;
        a ++
        SHtml.a(() => {Basket.removeItem(Basket.getBasket(basketId), item.id) ; JsCmds.Noop}, span) ++
        Text(" "+item.description+": "+item.formattedPrice+" ") ++
        <br/>;
      });
    val sum = basket.basketItems.foldLeft(0)(_+_.quantity);
    val xml = Text("Gesamt: "+ basket.formattedPrice);
    val checkout = <a href={checkoutUrl}>{xml}</a>;
    <span>{xml2}{checkout}</span>
  }

//  val imageSize=50
//  val imgSize=30
//
//  def items(basket:net.sprd.api.model.basket.Basket) : NodeSeq = {
//    NodeSeq.Empty ++ basket.basketItems.map(item => {
//        <tr><td><img width={""+imgSize} height={""+imgSize} src={ImageUrl.productUrl(item.product, item.appearanceId,imageSize)} /></td>
//      <td></td>
//      <span>{item.description}</span><br />
//      <span style="font-size: 25px; font-weight: bold;">{"%1.2f €".format(item.price*item.quantity)}<br/></span>
//    </tr>
//      })
//  }

  override def lowPriority : PartialFunction[Any, Unit] = {
    case CometBasket.Updated => {
        println("LocalAdded "+basketId+" "+ Basket.getBasket(basketId).formattedPrice)
        this.reRender(false)
        //       val newContent = content();
//        val t =3;
        //     partialUpdate(SetHtml("cometBasket", newContent));
      }
  }
}

object CometBasket {
  case object Updated
}

