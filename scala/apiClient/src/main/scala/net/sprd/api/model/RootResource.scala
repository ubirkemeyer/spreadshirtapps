package net.sprd.api.model

import net.sprd.api._
import net.sprd.api.access._
import net.sprd.api.model.inventory._
import net.sprd.api.model.product._
import net.sprd.api.model.design._
import scala.collection._

abstract class RootResource(url:String, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {

  lazy val countryId = (data \ "country" \ "@id").text
  lazy val currencyId = (data \ "currency" \ "@id").text
  lazy val languageId = (data \ "language" \ "@id").text
  
  lazy val productTypeUrl = (data \ "productTypes" \"@{http://www.w3.org/1999/xlink}href").text
  lazy val printTypeUrl = (data \ "printTypes" \"@{http://www.w3.org/1999/xlink}href").text
  lazy val fontFamilyUrl = (data \ "fontFamilies" \"@{http://www.w3.org/1999/xlink}href").text
  lazy val designCategoriesUrl = (data \ "designCategories" \"@{http://www.w3.org/1999/xlink}href").text
  
  lazy val designUrl = url+"/"+id+"/designs";//(data \ "printTypes" \"@{http://www.w3.org/1999/xlink}href").text
  
  lazy val productTypes = new XmlEntityListCache[ProductType](productTypeUrl,"productType") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new ProductType(RootResource.this,data, full)
  }
  
  lazy val printTypes = new XmlEntityListCache[PrintType](printTypeUrl,"printType") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new PrintType(RootResource.this,data, full)
  }

  lazy val fontFamilies = new XmlEntityListCache[FontFamily](fontFamilyUrl,"printType") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new FontFamily(RootResource.this,data, full)
  }

  lazy val designCategories = immutable.Map.empty ++ (( HttpAccess.loadXml(designCategoriesUrl)\ "designCategories"\ "designCategory") map (a => (a\"@id").text -> new DesignCategory(this,a)))
  
  lazy val designs = new XmlEntityListCache[Design](designUrl,"design") {
    def loadXml(data: => scala.xml.Node, full:Boolean) = new Design(RootResource.this,data, full)
  }
  
  def createProduct(newProduct: ProductUpdate){
    val result = HttpAccess.createLocation(ApiClient.baseUrl+"/baskets", newProduct.createXml.toString, true);
    if (result != null)
      new Product(this,result, true)
    else 
      null
  }

  def getUrl(product: Product) = url+"/"+id+"/products/"+product.id
 

}