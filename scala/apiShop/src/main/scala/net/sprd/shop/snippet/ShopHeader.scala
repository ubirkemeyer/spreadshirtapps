/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.snippet

import net.liftweb.http.S
import net.sprd.api.ApiClient
import net.sprd.shop.lib.Shop
import scala.xml.Text

class ShopHeader {
  
  def title = {
    val shop = ApiClient.shops(Shop.getShopId())

    <span>Shop: {shop.id}</span>
  }

  def init = {
    val shop = ApiClient.shops(Shop.getShopId())
    
    Text("")
  }

}
