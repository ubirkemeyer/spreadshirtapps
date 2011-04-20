package net.sprd.groovy.amazonas

class AmazonPrintType {
  String spreadshirtPrintTypeId
  String additionalDescription

  static constraints = {
    spreadshirtPrintTypeId blank: false, unique: true
    additionalDescription blank: false
  }

}
