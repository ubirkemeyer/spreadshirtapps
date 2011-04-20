package net.sprd.api.model.product

import scala.collection._
import net.sprd.api.model._
import net.sprd.api.model.inventory._
import net.sprd.api._

class Product(val root: RootResource, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full) with ProductInterface{

  type B = Configuration
  lazy val name = (fullData \ "name").text
  lazy val imageUrl = fullData \ "resources" \ "resource"\"@{http://www.w3.org/1999/xlink}href"
  lazy val freeColorSelection = (fullData \ "restrictions"\"freeColorSelection").text.toBoolean
  
  lazy val productTypeId = (fullData \ "productType"\"@id").text
  
  lazy val appearanceId = (fullData \ "appearance"\"@id").text
  lazy val defaultViewId = if (!(fullData \ "defaultView"\"@id").isEmpty)  (fullData \ "defaultView"\"@id").text else productType.defaultViewId
  lazy val configurations: Set[Configuration] = Set.empty ++ ((fullData \ "configurations"\ "configuration") map (a => Configuration(root, Product.this, a)))
  
  
  override lazy val productType = super.productType
  override lazy val appearance = super.appearance
  override lazy val usedPrintTypes = super.usedPrintTypes
  
  override lazy val possibleAppearances = super.possibleAppearances 
                                 
  override lazy val price = super.price
  
}
