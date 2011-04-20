package net.sprd.groovy.amazonas

class AmazonProductType {

  String spreadshirtProductTypeId
  String name
  String clothingType
  String department1
  String department2
  String department3
  String browseNode
  String outerMaterial
  String innerMaterial
  String materialComposition
  String careInstructions
  String sizeModifier
  String sleeveType
  String collarType
  String platinumKeywords1
  String platinumKeywords2
  String platinumKeywords3
  String platinumKeywords4
  String platinumKeywords5
  String styleKeyword
  String sizeChart


  static constraints = {
    spreadshirtProductTypeId blank: false, unique: true
    browseNode blank: false
    sizeChart(nullable: true)
  }

  public def getSizeChartURL() {

  }
}
