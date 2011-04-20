package net.sprd.api.model

import net.sprd.api.model.product._
import net.sprd.api.model.basket._
import net.sprd.api._
import net.sprd.api.access._

class Shop(url:String, data: => scala.xml.Node) extends RootResource(url, data)  {

  lazy val userId = (data \ "user" \ "@id").text
  
  lazy val articleUrl = (data \ "articles" \"@{http://www.w3.org/1999/xlink}href").text
  lazy val productUrl = url+"/"+id+"/products";//(data \ "articles" \"@{http://www.w3.org/1999/xlink}href").text

  private var articleFetched = false;
  
  
  def user():User = ApiClient.users(userId)
  
  lazy val articles = new XmlEntityListCache[Article](articleUrl,"article") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new Article(Shop.this, data, full)
  }
  
  lazy val products = new XmlEntityCache[Product](productUrl,"product") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new Product(Shop.this, data, full)
  }

  def fetchArticles(full: Boolean = false, maxCount : Int = 1000) {
    if (!articleFetched) {
      getAllArticles(full, maxCount);
      articleFetched = true;
    }
  }
  
  def getAllArticles(full: Boolean = false, maxCount : Int = 1000) = {
    val articleCount = articles.size;
    articles.slice(0, Math.min(articles.size, maxCount) ,full)
  }
  
  def createBasket() = {
    val result = HttpAccess.createLocation(ApiClient.baseUrl+"/baskets", new Basket("", token="xy",shopId = id).createXml.toString, true);
    if (result != null)
      new Basket(result)
    else 
      null
  }

  def getUrl(article:Article) = url+"/"+id+"/articles/"+article.id
  
}
