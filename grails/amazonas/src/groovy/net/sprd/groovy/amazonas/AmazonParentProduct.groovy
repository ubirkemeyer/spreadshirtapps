package net.sprd.groovy.amazonas

import de.kabtain.groovy.sprd.shop.Shop
import de.kabtain.groovy.sprd.design.Design
import de.kabtain.groovy.sprd.shop.Article
import de.kabtain.groovy.sprd.common.ImageURL
import de.kabtain.groovy.sprd.common.User
import de.kabtain.groovy.sprd.common.MediaType
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Created by IntelliJ IDEA.
 * User: kab
 * Date: 23.09.2010
 * Time: 15:09:27
 * To change this template use File | Settings | File Templates.
 */
class AmazonParentProduct {
  Shop shop
  User user
  Design primaryDesign
  Article article
  ImageURL primaryImageURL
  def secondaryImageURLs = [:]
  def brand
  Boolean inStock = true

  AmazonProductType amazonProductType

  def getSku() {
    "S${shop.id}-A${article.id}"
  }

  def getProductName() {
    "${brand}, ${primaryDesign?.name}, ${article.product.productType.name}"
  }

  def getStyleKeyword() {
    amazonProductType.styleKeyword
  }

  def getClothingType() {
    amazonProductType.clothingType
  }

  def getCurrency() {
    ""
  }

  def getDepartment1() {
    amazonProductType.department1
  }

  def getDepartment2() {
    amazonProductType.department2
  }

  def getDepartment3() {
    amazonProductType.department3
  }

  def getDepartment4() {
    ''
  }

  def getDepartment5() {
    ''
  }

  def getRecommendedBrowseNode() {
    amazonProductType.browseNode
  }

  def getRelationshipType() {
    ""
  }

  def getVariationTheme() {
    "SizeColor"
  }

  def getParentChild() {
    "Parent"
  }

  def getParentSku() {
    ""
  }

  def getColor() {""}

  def getColorMap() {""}

  def getSize() {""}

  def getSizeMap() {""}

  def getMaterialComposition() {
    amazonProductType.materialComposition
  }

  def getCareInstructions() {
    amazonProductType.careInstructions
  }

  def getOuterMaterial() {
    amazonProductType.outerMaterial
  }

  def getInnerMaterial() {
    amazonProductType.innerMaterial
  }

  def getProductDescription() {
    article.product.productType.description
  }

  def getMainImageURL() {
    primaryImageURL.getURL(MediaType.PNG, 1200)
  }

  def getSwatchImageURL() {""}

  def getOtherImageURL1() {
    getSecondaryImageURL(0)
  }

  def getOtherImageURL2() {
    getSecondaryImageURL(1)
  }

  def getOtherImageURL3() {
    getSecondaryImageURL(2)
  }

  def getOtherImageURL4() {
    getSecondaryImageURL(3)
  }

  def getOtherImageURL5() {
    getSecondaryImageURL(4)
  }
  def getOtherImageURL6() {
    getSecondaryImageURL(5)
  }
  def getOtherImageURL7() {
    getSecondaryImageURL(6)
  }
  def getOtherImageURL8() {
    getSecondaryImageURL(7)
  }

  def getSearchTerms1() {
    primaryDesign?.name
  }

  def getSearchTerms2() {
    primaryDesign?.getTagBundle(0, 4)?.join(",")
  }

  def getSearchTerms3() {
    primaryDesign?.getTagBundle(1, 4)?.join(",")
  }

  def getSearchTerms4() {
    primaryDesign?.getTagBundle(2, 4)?.join(",")
  }

  def getSearchTerms5() {
    primaryDesign?.getTagBundle(3, 4)?.join(",")
  }

  def getSeason() {
    "NOS"
  }

  def getSizeModifier() {
    amazonProductType.getSizeModifier()
  }

  def getSleeveType() {
    amazonProductType.getSleeveType()
  }

  def getCollarType() {
    amazonProductType.getCollarType()
  }

  def getItemPrice() {
    ""
  }

  def getQuantity() {
    ""
  }

  def getCountryOfOrigin() {
    "DE"
  }

  def getPlatinumKeywords1() {
    amazonProductType.getPlatinumKeywords1()
  }

  def getPlatinumKeywords2() {
    amazonProductType.getPlatinumKeywords2()
  }

  def getPlatinumKeywords3() {
    amazonProductType.getPlatinumKeywords3()
  }

  def getPlatinumKeywords4() {
    amazonProductType.getPlatinumKeywords4()
  }

  def getPlatinumKeywords5() {
    amazonProductType.getPlatinumKeywords5()
  }

  def getRegisteredParameter() {
    "PrivateLabel"
  }
  private def getSecondaryImageURL(int i) {
    def result = ""
    def allSecondaryImages = []

    secondaryImageURLs.each {
      allSecondaryImages << it.getURL(MediaType.PNG, 1200)
    }
    if (amazonProductType.sizeChart && new File(amazonProductType.sizeChart).canRead()) {
      allSecondaryImages << new ApplicationTagLib().createLink(
              controller: 'export',
              action: 'sizeChart',
              id: amazonProductType.id,
              absolute: true
      )
    }

    if (allSecondaryImages[i]) {
      result = allSecondaryImages[i]
    }

    return result
  }

  
}
