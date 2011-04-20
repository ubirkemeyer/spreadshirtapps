package net.sprd.groovy.amazonas

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import de.kabtain.groovy.sprd.inventory.Size
import de.kabtain.groovy.sprd.inventory.Appearance
import de.kabtain.groovy.sprd.common.MediaType

/**
 * Created by IntelliJ IDEA.
 * User: kab
 * Date: 23.09.2010
 * Time: 15:05:26
 * To change this template use File | Settings | File Templates.
 */
class AmazonProduct extends AmazonParentProduct {
  AmazonParentProduct parentProduct
  AmazonColorMapping colorMapping
  Appearance apperance
  Size size


  def getSku() {
    "${parentProduct.sku}-app${apperance.id}-size${size.id}"
  }

  def getProductId() {""}

  def getProductIdType() {""}

  def getProductName() {
    "${parentProduct.productName}, ${apperance.name}, ${size.name}"
  }

  def getRelationshipType() {
    "Variation"
  }

  def getParentChild() {
    "Child"
  }

  def getParentSku() {
    parentProduct.sku
  }

  def getColor() {
    apperance.name
  }

  def getColorMap() {
    colorMapping.amazonName
  }

  def getSize() {
    size.name
  }

  def getSizeMap() {
    size.name
  }

  def getProductDescription() {""}

  def getSwatchImageURL() {
    primaryImageURL.getURL(MediaType.PNG, 50)
  }


  def getSeason() {
    ""
  }

  def getQuantity() {
    inStock ? 99999 : 0
  }

  def getItemPrice() {
    article.price
  }

  def getCurrency() {
    article.currency.isoCode
  }
}
