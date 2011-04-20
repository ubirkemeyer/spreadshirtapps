package net.sprd.groovy.amazonas

class AmazonColorMapping {
  String spreadshirtColorId
  String spreadshirtName
  String amazonName

  static constraints = {
    spreadshirtColorId blank: false, unique: true
    spreadshirtName blank: false
    amazonName blank: false
  }
}
