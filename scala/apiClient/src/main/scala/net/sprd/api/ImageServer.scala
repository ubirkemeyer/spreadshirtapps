/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api
case class Format(val extension:String)
case object PNG extends Format("png")
case object JPG extends Format("jpg")
case object GIF extends Format("gif")

object ImageServer {

  var host = "image.spreadshirt.net"
  val rootPath = "/image-server/v1"

  def getDesignUrl(designId:String, width:Option[Int] = new Some(300), height:Option[Int] = None, backgroundColor:Option[String] = None, format:Format = PNG) = {
    "http://"+host+rootPath+"/designs/"+designId+attr("width",width)+attr("height",height)+attr("backgroundColor",backgroundColor)+"."+format.extension
  }

  def getConfigurationUrl(configurationId:String, width:Option[Int] = new Some(300), height:Option[Int] = None, backgroundColor:Option[String] = None, appearanceId:Option[String] = None, format:Format = PNG) = {
    "http://"+host+rootPath+"/configurations/"+configurationId+attr("width",width)+attr("height",height)+attr("backgroundColor",backgroundColor)+attr("appearanceId",appearanceId)+"."+format.extension
  }

  def getProductUrl(productId:String, viewId:String = "1", width:Option[Int] = new Some(300), height:Option[Int] = None, backgroundColor:Option[String] = None, appearanceId:Option[String] = None, format:Format = PNG) = {
    "http://"+host+rootPath+"/products/"+productId+"/views/"+viewId+attr("width",width)+attr("height",height)+attr("backgroundColor",backgroundColor)+attr("appearanceId",appearanceId)+"."+format.extension
  }

  def getCompositionUrl(productId:String, viewId:String = "1", width:Option[Int] = new Some(300), height:Option[Int] = None, backgroundColor:Option[String] = None, appearanceId:Option[String] = None, format:Format = PNG) = {
    "http://"+host+rootPath+"/compositions/"+productId+"/views/"+viewId+attr("width",width)+attr("height",height)+attr("backgroundColor",backgroundColor)+attr("appearanceId",appearanceId)+"."+format.extension
  }
  
  def getProductTypeUrl(productTypeId:String, viewId:String = "1", appearanceId:String = "1", width:Option[Int] = new Some(300), height:Option[Int] = None, backgroundColor:Option[String] = None, format:Format = PNG) = {
    "http://"+host+rootPath+"/productTypes/"+productTypeId+"/views/"+viewId+"/appearances/"+appearanceId+attr("width",width)+attr("height",height)+attr("backgroundColor",backgroundColor)+"."+format.extension
  }

  def getAppearanceUrl(appearanceId: String, width:Option[Int] = None, height:Option[Int] = None ) =
    "http://"+host+rootPath+"/appearances/"+appearanceId+attr("width",width)+attr("height",height)

  private def attr(attribute:String, value: Option[Any]) = value match {
    case Some(n) => ","+attribute+"="+n.toString
    case _ => ""
  }

}
