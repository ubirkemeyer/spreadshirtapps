/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib
import net.sprd.shop.comet.CometBasket
import scala.actors._
import net.sprd.shop.comet.CometBasket
import scala.actors.Actor._
import scala.collection._

object CometBasketMaster extends Actor {

  case class SubscribeBasket(cbasket : CometBasket)
  case class UnsubBasket(cbasket : CometBasket)
  case class Updated(basketId : String)

  var baskets : mutable.Map[String,List[CometBasket]] = mutable.Map.empty
  override def act() {
    loop {
      react {
        case CometBasketMaster.SubscribeBasket(basket) => {
          println("Subscribe "+basket.basketId);
          baskets += (basket.basketId -> (basket :: baskets.getOrElse(basket.basketId, Nil)))
        }
        case CometBasketMaster.UnsubBasket(basket) =>
          baskets += (basket.basketId -> (baskets.getOrElse(basket.basketId, Nil) - basket))
        case CometBasketMaster.Updated(basketId) => {
          println("MasterAdded "+basketId)
          baskets.getOrElse(basketId, Nil).foreach(_ ! CometBasket.Updated)
        }
      }
    }
  }

  start
}

