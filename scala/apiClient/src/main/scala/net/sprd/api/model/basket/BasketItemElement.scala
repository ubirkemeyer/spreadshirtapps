package net.sprd.api.model.basket

import net.sprd.api.Entity
import scala.collection._
import net.sprd.api.model.product._

class BasketItemElement ( override val id: String,
                         val itemType :String,
                         val link :String,
                         val properties: mutable.Map[String,String]) extends Entity{

  def this(data: => scala.xml.Node) {
    this((data \ "@id").text, (data \"@type").text,
         (data \"@{http://www.w3.org/1999/xlink}href").text,
         mutable.Map.empty ++ (data \"properties"\"property").map(a => ((a\"@key").text -> a.text)))
  }

  override def equals(other: Any) = 
    other match {
      case e: BasketItemElement => 
        (e.id == id) && (e.itemType ==itemType) && (properties.map(p=> e.properties.contains(p._1) && e.properties(p._1)==p._2).foldLeft(true)(_&&_))
      case _ => false
    }
  

  def toXml = <element id={id} type={itemType} xlink:href={link}>
    <properties>
      {properties map (a=> <property key={a._1}>{a._2}</property>)}
    </properties>
              </element>

}

object BasketItemElement {

  val productItemType = "sprd:product"
  val articleItemType = "sprd:article"

  val propAppearance = "appearance"
  val propSize = "size"

  def apply(product:Product, sizeId:String, appearanceId:String ) = {
    new BasketItemElement(product.id,productItemType,  product.root.getUrl(product), mutable.Map((propSize->sizeId) , (propAppearance -> appearanceId)))
  }

  def apply(article:Article, sizeId:String, appearanceId:String ) = {
    new BasketItemElement(article.id,articleItemType,  article.shop.getUrl(article), mutable.Map((propSize->sizeId) , (propAppearance -> appearanceId)))
  }
}


