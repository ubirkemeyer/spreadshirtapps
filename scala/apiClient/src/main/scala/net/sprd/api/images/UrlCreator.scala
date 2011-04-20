package net.sprd.api.images

import scala.collection._
import net.sprd.api.access.HttpAccess._
import net.sprd.api.ApiClient

object UrlCreator {
  
  def getDesignUrl(designId: String, width: Int, height: Int, colors: Seq[String] = Seq.empty) = {
    var params = mutable.Map.empty[String,String]
    if (width>0) params += ("width" -> width.toString)
    if (height>0) params += ("height" -> height.toString)
    0 until colors.size foreach (a => params += ("config:colors["+a.toString+"]" -> colors(a)))
    ApiClient.imageBaseUrl+"/designs/"+designId+"?"+urlParamsString(params)
  }
}
