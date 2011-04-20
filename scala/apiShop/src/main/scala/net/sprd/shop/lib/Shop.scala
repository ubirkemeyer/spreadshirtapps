/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import net.liftweb.http.S
import net.sprd.api.ApiClient

object Shop {
  
  def getShopId() = {
    
    val servletSession = S.containerSession.get
    var shopId = servletSession.attribute("shopId").asInstanceOf[String]
    if (shopId == null || shopId == "" || (S.param("shopId").isDefined && S.param("shopId").get!=shopId)) {
      shopId = S.param("shopId").getOrElse("205909")
      if (S.param("shopId").isDefined) servletSession.setAttribute("shopId", shopId)
    }
    shopId
  }

  def getShop() = {
    ApiClient.shops(getShopId())
  }

  def getArticleId() = {
    S.param("article")
  }

  def getArticle() = {
    var articleId = getArticleId
    if (articleId.isDefined)
      getShop().articles(getArticleId().get)
    else null
  }

}
