package net.sprd.api.model.product

import scala.collection._
import net.sprd.api.model._
import net.sprd.api.model.inventory._
import net.sprd.api._

trait ProductInterface extends Entity {
  type B <:ConfigurationInterface
  def root: RootResource
  def name: String
  def freeColorSelection: Boolean
  def productTypeId :String
  def appearanceId :String
  def configurations : Set[B]
  
  def productType = {root.productTypes(productTypeId)
  }
  def appearance = {
    if (productType != null && productType.appearances != null && productType.appearances.contains(appearanceId))
      productType.appearances(appearanceId)
    else null
  }
  def usedPrintTypes = immutable.Set.empty ++ configurations map (a => a.printTypeId)
  
  def possibleAppearances = {
    if (freeColorSelection) immutable.Seq.empty ++ productType.appearances.valuesIterator filter
    (a => (usedPrintTypes map (p => a.printTypes.contains(p))).foldLeft(true)(_ && _))
    else if (appearance != null) immutable.Seq(appearance)
    else immutable.Seq.empty[Appearance]
  }

  def findConfiguration(id : String) = {
    configurations.find(_.id==id)
  }
                                 
  def price = configurations.foldLeft(productType.price)(_+_.price)
  def formattedPrice = ApiClient.currencies(productType.currencyId).format(price)
}
