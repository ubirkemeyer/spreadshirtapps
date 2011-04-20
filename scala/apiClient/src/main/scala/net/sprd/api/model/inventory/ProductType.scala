package net.sprd.api.model.inventory

import net.sprd.api._
import net.sprd.api.model._
import net.sprd.api.model.product._
import scala.collection._
import scala.runtime._

class ProductType(root :RootResource, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full)  {
  
  lazy val name = (data \ "name").text
  lazy val description = (fullData \ "description").text
  lazy val brand = (fullData \ "brand").text
  lazy val price = (data \ "price" \ "vatIncluded").text.toDouble
  lazy val currencyId = (data \"price"\"currency"\"@id").text
  lazy val imageUrl = data \ "resources" \ "resource"\"@{http://www.w3.org/1999/xlink}href"
  
  lazy val printAreas = immutable.Map.empty ++ 
    ((fullData \ "printAreas"\ "printArea") map (a => (a\"@id").text -> new PrintArea(this, a)))
  
  lazy val views = (fullData \ "views"\ "view") map (a => new View(this, a))

  lazy val viewMap = immutable.Map.empty ++ (views map (v => v.id -> v))

  lazy val defaultViewId = (fullData \ "defaultValues"\ "defaultView"\ "@id").text

  lazy val defaultAppearanceId = (fullData \ "defaultValues"\ "defaultAppearance"\ "@id").text
  
  
  lazy val appearances = immutable.Map.empty ++ 
    ((fullData \ "appearances"\ "appearance") map (a => (a\"@id").text -> Appearance(a))) 
  
  lazy val sizes = immutable.Seq.empty ++
    ((fullData \ "sizes"\ "size") map (a =>new Size(a)))

  lazy val sizeNames = immutable.Map.empty ++ sizes.map(s => (s.id -> s.name))
  
  lazy val stockStates = immutable.Map.empty ++ 
    ((fullData \ "stockStates" \ "stockState") map (a => (((a\"appearance"\"@id").text,(a\"size"\"@id").text) -> (a\"available").text.toBoolean)))
    
  def inStock(appearance:String, size:String) = stockStates.contains(appearance,size) && stockStates(appearance,size)

  def possibleSizes(appearance:String) = sizes.filter(a => inStock(appearance, a.id))

  lazy val formattedPrice = ApiClient.currencies(currencyId).format(price)


}

