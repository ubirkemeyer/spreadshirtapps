/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import net.sprd.api.ImageServer
import net.sprd.api.model.product._

object ImageUrl {

  def productUrl(product: Product, view:String, appearance: String, size:Int) = {
    ImageServer.getProductUrl(product.id, view, width = new Some(size), height = new Some(size), appearanceId=new Some(appearance))
  }

  def productTypeUrl(productTypeId: String, view:String, appearance: String, size:Int) = {
    ImageServer.getProductTypeUrl(productTypeId, view, appearance, width = new Some(size), height = new Some(size))
  }

}
