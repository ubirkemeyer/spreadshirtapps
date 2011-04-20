package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.common.Full
import net.liftweb.http.provider.HTTPRequest
import net.sprd.api.ApiClient
import net.sprd.shop.lib.ProductCreator
import net.sprd.shop.lib.SvgRenderClient

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {

  def boot {

    ApiClient.apiKey = ""
    ApiClient.secret = ""
    ApiClient.baseUrl = "http://api.spreadshirt.net/api/v1"
    
    // where to search snippet
    LiftRules.addToPackages("net.sprd.shop")

    LiftRules.statelessRewrite.append {      
      case RewriteRequest(
          ParsePath(List("shop",shopId,"articles"),_,_,_),_,_) =>
        RewriteResponse("articles" :: Nil, Map("shopId" -> shopId))
      case RewriteRequest(
          ParsePath(List("shop",shopId,"articles",start,end),_,_,_),_,_) =>
        RewriteResponse("articles" :: Nil, Map(("shopId" -> shopId),("start" -> start),("end" -> end)))
      case RewriteRequest(
          ParsePath(List("shop",shopId,"articlesCarousel",start,end),_,_,_),_,_) =>
        RewriteResponse("articlesCarousel" :: Nil, Map(("shopId" -> shopId),("start" -> start),("end" -> end)))
      case RewriteRequest(
          ParsePath(List("shop",shopId,"basket"),_,_,_),_,_) =>
        RewriteResponse("basket" :: Nil, Map("shopId" -> shopId))
      case RewriteRequest(
          ParsePath(List("t-news",source),_,_,_),_,_) =>
        RewriteResponse("t-news" :: Nil, Map(("news" -> source),("shopId" -> "533633")))
    }

    LiftRules.dispatch.prepend {
      case Req(List("shop",shopId,"images",productId,viewId,text), "", GetRequest) =>
        () => {
          val svg = ProductCreator.productImage(shopId,productId,viewId,Array(text))
          val bytes = SvgRenderClient.performRenderRequest(svg)
          Full(InMemoryResponse(bytes,
            ("Content-Type" -> "image/png") :: Nil,
            Nil,
            200))
          //Full(XmlResponse(svg))
        }
      case Req(List("shop",shopId,"images",productId,viewId,text0, text1), "", GetRequest) =>
        () => {
          val size = S.param("size").getOrElse("400").toInt
          val svg = ProductCreator.productImage(shopId,productId,viewId,Array(text0, text1), size)
          val bytes = SvgRenderClient.performRenderRequest(svg)
          Full(InMemoryResponse(bytes,
            ("Content-Type" -> "image/png") :: Nil,
            Nil,
            200))
          //Full(XmlResponse(svg))
        }
      case Req(List("shop",shopId,"buy",productId,text), "", GetRequest) =>
        () => {
          val product = ProductCreator.product(shopId,productId,Array(text))
          val appearanceId = product.appearanceId
          val sizeId = urlDecode(S.param("size").get)
          println(shopId+"/"+appearanceId+"/"+sizeId)
          println(product.createXml)
          val checkoutUrl = ProductCreator.buyProduct(shopId, product, appearanceId, sizeId)
          S.redirectTo(checkoutUrl)
        }
        case Req(List("shop",shopId,"buy",productId,text0, text1), "", GetRequest) =>
        () => {
          val product = ProductCreator.product(shopId,productId,Array(text0, text1))
          val appearanceId = product.appearanceId
          val sizeId = urlDecode(S.param("size").get)
          println(shopId+"/"+appearanceId+"/"+sizeId)
          println(product.createXml)
          val checkoutUrl = ProductCreator.buyProduct(shopId, product, appearanceId, sizeId)
          S.redirectTo(checkoutUrl)
        }
        
    }


    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

}




