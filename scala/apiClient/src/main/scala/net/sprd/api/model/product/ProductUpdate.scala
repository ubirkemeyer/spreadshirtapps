package net.sprd.api.model.product

import scala.collection._
import net.sprd.api.model._
import net.sprd.api.model.inventory._
import net.sprd.api._

class ProductUpdate(
  var root: RootResource,
  var name :String,
  var freeColorSelection: Boolean,
  var productTypeId :String,
  var appearanceId :String,
  var configurations :Set[ConfigurationUpdate],
  var id : String = ""
  ) extends ProductInterface  {
  type B = ConfigurationUpdate

  def this(product: ProductInterface) {
    this(product.root, product.name, product.freeColorSelection, product.productTypeId,
         product.appearanceId, null)

    this.configurations = product.configurations.map(ConfigurationUpdate(_,ProductUpdate.this))
  }
    
  
	  
	def createXml = <product xmlns="http://api.spreadshirt.net">
  	  <productType id={productTypeId}/>
  	  <appearance id={appearanceId}/>
  	  <configurations>{configurations map (a => a.createXml)}</configurations>
     </product>
	  
}
