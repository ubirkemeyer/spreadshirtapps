package net.sprd.shop.snippet

import net.liftweb.common.Box
import net.liftweb.common.Full
import net.liftweb.common.Empty
import net.liftweb.http.JsonHandler
import net.liftweb.http.LiftRules
import net.liftweb.http.RequestVar
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE
import net.liftweb.http.js.JE.JsVal
import net.liftweb.http.js.JE.Str
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.jquery.JqJE
import net.liftweb.util.JsonCmd
import net.liftweb.util.LoanWrapper
import net.sprd.api.ImageServer
import net.sprd.api._
import net.sprd.shop.lib.Basket
import net.sprd.shop.lib.ImageUrl
import net.sprd.shop.lib.ProductCreator
import net.sprd.shop.lib.Shop
import net.sprd.shop.lib.TextLoader
import scala.xml.Elem
import scala.xml.NodeSeq
import net.sprd.api.model.inventory.Size
import net.sprd.api.model.product._

import _root_.net.liftweb.util.Helpers._
import scala.xml.Text
import scala.collection._

class Zitate {

  val productSize = 400
  val appearanceSize = 50

  val shopId = "539719"

  val products = Array("18165045","18164058","18189781","18165166","18185494","18165070","18165156",
                       null,"18164063","18189797","18165190","18185476","18165174","18165181")

  lazy val sizes = getAllSizes()

  object zitat extends RequestVar("")

  object author extends RequestVar("")

  def getAllSizes() = {
    var sizeList = SortedSet.empty[Int];
    var sizeMap = mutable.Map.empty[Int, Size];
    products.foreach(pid => if (pid!= null) {
        val product = ApiClient.shops(shopId).products(pid)
        product.productType.sizes.foreach(size =>
          if (!sizeList.contains(size.id.toInt)) {
            sizeList= sizeList + size.id.toInt
            sizeMap + (size.id.toInt -> size)
          }
        )
      }
    )
    println(sizeList)
    sizeList.toArray.map(sizeMap(_))
  }

  def head(xHtml: NodeSeq) = {
    <head>
      <script type="text/javascript"
        src={"/" + LiftRules.resourceServerPath + "/jlift.js"} />
    </head>
  }

  def getZitat()={
    if (zitat.isEmpty || zitat.get == "") {
      if (S.param("zitat").isDefined) {
        zitat(S.param("zitat").get)
        if (S.param("author").isDefined) {
          author(S.param("author").get)
        } else {
          author(" ")
        }
      } else {
        val (nextZitat, nextAuthor) = net.sprd.shop.lib.Zitate.nextZitat()
        zitat(nextZitat)
        author(nextAuthor)
      }
    }
    (zitat.get, author.get)
  }

  def navi(xHtml: NodeSeq) = {
    <a href={"/zitate"}>{">>"}</a>;
  }
 
  def formlist(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
    <form action="/zitate" id="myform" method="get">
                   {product(xHtml)}
    </form>
  }

  def product(xHtml: NodeSeq):NodeSeq = {
    val (zitat, author) = getZitat
    val texts = Array(zitat, author)
    val p =ProductCreator.product(shopId, products(0), texts)
    val pidUrl  = S.contextPath+ProductCreator.productImageUrl(shopId, "PID", "1", texts,productSize)
    val pidBuyUrl  = S.contextPath+ProductCreator.productBuyUrl(shopId, "PID", texts)
    val buyCmd = JsCmds.SetElemById("myform",
                                             JE.JsRaw("'"+pidBuyUrl.replaceAll("'","\'")+"'.replace('PID',"+
                                                      JE.ValById("variance").toJsCmd+")") ,"action")
    val zitateUrl = S.contextPath+"/zitate"
    val nextCmd = new JE.JsRaw("location.href='"+zitateUrl+"'")
    bind("zitate", xHtml,
         ("name" -> <span>{author+": "+zitat}</span>),
         ("id" -> <input type="hidden" id="article" name ="article" value={zitat+"/"+author} />),
         ("price" -> <span style="font-size: 25px; font-weight: bold;">{p.formattedPrice}<br/></span>),
         ("image" -> productImage(products(0), "1", Array(zitat, author))) ,
         ("varianceHidden" -> (<input type="hidden" id="variance" value={products(0)} />)),
         ("variances" -> variances(chooseTemplate("zitate","variances",xHtml), Array(zitat, author))),
         ("sizes" -> sizes(chooseTemplate("zitate","sizes",xHtml))),
         ("buy"-> <input type="submit" value="Kaufen" onclick={buyCmd.toJsCmd} />),
         ("next"-> <input type="button" value="Nächstes Zitat" onclick={nextCmd.toJsCmd} />)
    )
  }

  def productImage(productId: String, view:String, texts:Array[String]) = {
    val url = ProductCreator.productImageUrl(shopId, productId, view, texts, productSize)
    <img  class="articlesImage" id={"zitatImage"} width={""+productSize} height={""+productSize} src={url} />;
  }
            
  
  def setRadioActiveStateJs(id: String, active:Boolean) = {
    if (active) JsCmds.SetElemById(id,JE.Num(0), "disabled")
    else JsCmds.SetElemById(id,JE.Num(1), "disabled") & JsCmds.SetElemById(id,JE.Num(0), "checked")
  }



  def variances(xHtml:NodeSeq, texts:Array[String]) = {
    val viewId = 1
    val pidUrl  = S.contextPath+ProductCreator.productImageUrl(shopId, "PID", "1", texts,productSize)
    NodeSeq.Empty ++ products.flatMap(pid =>
        if (pid == null) {
          <br />
        } else {
        val product = ApiClient.shops(shopId).products(pid)
        val url  = ProductCreator.productImageUrl(shopId, pid, "1", texts, productSize)
        val productTypeImageUrl = ImageUrl.productTypeUrl(product.productTypeId, "1", product.appearanceId ,appearanceSize);
        val setOutImage = JsCmds.SetElemById("zitatImage",
                                             JE.JsRaw("'"+pidUrl.replaceAll("'","\'")+"'.replace('PID',"+
                                                      JE.ValById("variance").toJsCmd+")") ,"src")
        val setImage = JsCmds.SetElemById("zitatImage", Str(S.contextPath+url),"src")
        val setVarianceImage = JsCmds.SetElemById("variance"+pid,  Str(S.contextPath+url),"src")
        val setAppearance = JsCmds.SetElemById("variance",Str(pid),"value")
        val posSizes = product.productType.possibleSizes(product.appearanceId);
        //sizes foreach (a=> {println(a + "-"+ posSizes.find(_.id == a) + "-" +posSizes.find(_.id == a).isDefined)})
        val setSizes = sizes map(a=> setRadioActiveStateJs("size"+a.id, posSizes.find(_.id == a.id).isDefined)) reduceLeft (_ & _)

        bind("zitate", xHtml,
             ("variance" -> SHtml.a(<img id={"variance"+pid} style="border: 2px solid black;" width={""+appearanceSize} height={""+appearanceSize}
                                      src={productTypeImageUrl} />, setImage & setAppearance & setSizes,
                                      ("onmouseover"->(setImage & setVarianceImage).toJsCmd) , ("onmouseout"->setOutImage.toJsCmd)
            )
          )
        )
      }
    )
  }

  def title(xHtml:NodeSeq):NodeSeq = {
    Text("Zitate-Online.de")
  }

  def sizes(xHtml:NodeSeq):NodeSeq = {
    sizes(xHtml, ApiClient.shops(shopId).products(products(0)), "")
  }

  def sizes(xHtml:NodeSeq, product:ProductInterface, id:String):NodeSeq = {
    val productType = product.productType;
    val activeSizes = productType.possibleSizes(product.appearanceId)
    NodeSeq.Empty ++ sizes.map(size => {
        <span style="font-size: 15px; font-weight: bold;">
          {var xml = <input type="radio" name="size" value={size.id} id={"size"+size.id} />
           if (activeSizes.find(as => as.id == size.id).isDefined) {
              if (activeSizes(0).id == size.id) {
                xml % ("checked" -> "checked")
              }
                else xml
           } else xml % ("disabled" -> "disabled")
              
          }
          {size.name}
        &nbsp;</span>
      })
//    val radios = SHtml.radio(sizes.map(_.id), if (activeSizes.size>0) Full(activeSizes.head.id) else Empty, selectedSize(_), ("name"->("sizes"+id)))
//    0 until sizes.size flatMap( a =>
//      bind("sizes", xHtml,
//           ("size" -> //radios(a).asInstanceOf[Elem] % ("id" -> ("sizes"+article.id+productType.sizes(a).id)))
//            <span style="font-size: 15px; font-weight: bold;">
//            {if (activeSizes.find(as => as.id == sizes(a).id).isDefined) {
//                (radios(a).asInstanceOf[Elem] % ("id" -> ("sizes"+sizes(a).id)))
//              } else {
//                (radios(a).asInstanceOf[Elem] % ("id" -> ("sizes"+sizes(a).id)) % ("disabled" -> "disabled"))
//              }
//            }
//            {sizes(a).name}
//            &nbsp;</span>)
//      )
//    )
  }


}

