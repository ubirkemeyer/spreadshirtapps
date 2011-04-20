package net.sprd.api

import net.sprd.api.model._
import net.sprd.api.model.basket._
import net.sprd.api.access._
import net.sprd.api.model.common.Currency
import net.sprd.api.model.product._
import net.sprd.api.model.design._
import scala.xml.Xhtml
/** Defines the Spreadshirt API-Client
 */
object ApiClient{
  
  var baseUrl = "http://api.spreadshirt.net/api/v1";
  var imageBaseUrl = "http://image.spreadshirt.net/image-server/v1";
  var maxTime = 60*60*1000; //1h in ms
  var apiKey = ""
  var secret = ""
   
  lazy val shops = new XmlEntityListCache[Shop](baseUrl+"/shops", "shop") {
      def loadXml(data: => scala.xml.Node, isListData:Boolean) = new Shop(baseUrl+"/shops",data)
    }
  
  lazy val users = new XmlEntityListCache[User](baseUrl+"/users", "user") {
      def loadXml(data: => scala.xml.Node, isListData:Boolean) = new User(baseUrl+"/users",data)
    }

  lazy val currencies = new XmlEntityListCache[Currency](baseUrl+"/currencies", "currency") {
      def loadXml(data: => scala.xml.Node, isListData:Boolean) = new Currency(data, isListData)
    }

  def getBasket(basketId : String) = {
    val result = HttpAccess.loadXml(baseUrl+"/baskets/"+basketId, apiKey=true)
    if (result != null) 
      new Basket(result)
    else
      null
  }

  def createProduct(shop: Shop, product: ProductUpdate) = {
    println(Xhtml.toXhtml(product.createXml))
    println(baseUrl+"/shops/"+shop.id+"/products")
    val result = HttpAccess.createLocation(baseUrl+"/shops/"+shop.id+"/products", Xhtml.toXhtml(product.createXml), true, false);
    if (result != null)
      new Product(shop, result, true)
    else
      null
  }


  def createDesign(shop: Shop, design: DesignUpdate) = {
    println(Xhtml.toXhtml(design.createXml))
    println(baseUrl+"/shops/"+shop.id+"/designs")
    val result = HttpAccess.createLocation(baseUrl+"/shops/"+shop.id+"/designs", Xhtml.toXhtml(design.createXml), true, false);
    if (result != null)
      new Design(shop, result, true)
    else
      null
  }
  
  def createBasket(shop: Shop, token:String = "123") = {
    val result = HttpAccess.createLocation(baseUrl+"/baskets", Xhtml.toXhtml(new Basket("", token=token,shopId = shop.id).createXml), true, true);
    if (result != null)
      new Basket(result)
    else 
      null
  }

  /**
   * returns the HTTP returncode of the api query
   */
  def updateBasket(basket: Basket) = {
    HttpAccess.put(baseUrl+"/baskets/"+basket.id, basket.toXml.toString, true)._2;
  }

  def getBasketCheckoutUrl(basketId: String) = {
     val result = HttpAccess.loadXml(baseUrl+"/baskets/"+basketId+"/checkout", apiKey=true)
    if (result != null)
      (result \ "@{http://www.w3.org/1999/xlink}href").text.replaceFirst("http://", "https://")
    else
      null
  }

}
