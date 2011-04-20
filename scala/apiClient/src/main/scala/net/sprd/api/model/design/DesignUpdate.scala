package net.sprd.api.model.design

import net.sprd.api.model._
class DesignUpdate (
  var root: RootResource,
  var name: String,
  var description: String,
  var price: Double,
  var id : String = ""
  ) extends DesignInterface {

  def createXml =
    <design xmlns="http://api.spreadshirt.net">
  	  <name>{name}</name>
      <description>{description}</description>
  	  <price><vatIncluded>{price}</vatIncluded></price>
     </design>
}