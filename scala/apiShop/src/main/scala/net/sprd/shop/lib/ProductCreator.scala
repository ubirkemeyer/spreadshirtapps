/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import net.sprd.api.ApiClient
import net.sprd.api.creator.ProductReplacer
import net.sprd.api.model.product.ProductUpdate
import net.sprd.api.svg.SvgImageGenerator
import scala.xml.Elem
import _root_.net.liftweb.util.Helpers._

object ProductCreator {


  def productImage(shopId:String, productId:String, viewId:String, texts:Array[String], size:Int = 400):Elem = {
    val newProduct = product(shopId, productId, texts)
    val svg = SvgImageGenerator.generateProductViewImage(newProduct, viewId, size)
    svg
    //productImage(shopId, productId, viewId, text, size)
  }

  def productImageUrl(shopId:String, productId:String, viewId:String, texts:Array[String], size:Int = 400) = {
    "/shop/"+shopId+"/images/"+productId+"/"+viewId+"/"+texts.map(productUrlEncode(_)).reduceLeft(_+"/"+_)+"?size="+size
  }

  def productBuyUrl(shopId:String, productId:String, texts:Array[String], size:String) = {
    "/shop/"+shopId+"/buy/"+productId+"/"+texts.map(productUrlEncode(_)).reduceLeft(_+"/"+_)+"?sizes="+size
  }

  def productBuyUrl(shopId:String, productId:String, texts:Array[String]) = {
    "/shop/"+shopId+"/buy/"+productId+"/"+texts.map(productUrlEncode(_)).reduceLeft(_+"/"+_)
  }

  private def productUrlEncode(text:String) = urlEncode(text.replaceAll("/", "-"))

  def product(shopId:String, productId:String, texts:Array[String]) = {
    val product = ApiClient.shops(shopId).products(productId)
    ProductReplacer.replaceTemplate(product, texts)
  }

  def createProduct(shopId:String, product:ProductUpdate) = {
    ApiClient.createProduct(ApiClient.shops(shopId), product)
  }

  

  def buyProduct(shopId:String, product:ProductUpdate, appearanceId:String, sizeId:String) = {
    val newProduct = createProduct(shopId, product)
    println(newProduct.id)
    Basket.addProduct(shopId,newProduct.id, appearanceId, sizeId);
    val basket = Basket(shopId);
    println(basket.id)
    Basket.getCheckoutUrl(basket.id)
  }
  
}
