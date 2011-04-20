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
import net.sprd.api.model.product._

import _root_.net.liftweb.util.Helpers._
import scala.xml.Text

class ShopArticles {

  val productSize = 250
  val appearanceSize = 40

  def template =
    <articles:name /><br />
  <articles:price /><br />
  <articles:appearanceHidden />
  <articles:appearances >
    <appearances:appearance />
  </articles:appearances >
  <br />
  <articles:sizes >
    <sizes:size />
  </articles:sizes >
  <br />
  <articles:cart />;

  object selectedSize extends RequestVar("")

  def count = <span>{ApiClient.shops(Shop.getShopId()).articles.size}</span>

  def addToBasket(json: String) :JsCmd = {
    JsCmds.jsExpToJsCmd(JE.JsRaw("alert(’Button2 clicked’)"))
  }

  def head(xHtml: NodeSeq) = {
    <head>
      <script type="text/javascript"
        src={"/" + LiftRules.resourceServerPath + "/jlift.js"} />
      {JsCmds.Script(json.jsCmd)}
    </head>
  }

  def getStart() = S.param("start").getOrElse("0").toInt

  def getEnd() = S.param("end").getOrElse("10").toInt

  def navi(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
    val start = getStart
    val end = getEnd
    val size = end-start+1
    val articleSize = shop.articles.size

    val lastStart = Math.max(start-size, 0)
    val last =
      if(start>0)
        <a href={"/shop/"+Shop.getShopId()+"/articles/"+(lastStart)+"/"+(lastStart+size-1)}>{"<<"}</a>
    else Text("<<");
    val nextEnd = Math.min(end+size, articleSize-1)
    val next =
      if(end+1<articleSize)
        <a href={"/shop/"+Shop.getShopId()+"/articles/"+(nextEnd-size+1)+"/"+nextEnd}>{">>"}</a>
    else Text(">>");
    <table width="100%" ><tr><td style="font-size: 20px; font-weight: bold;text-align: center;">{last}</td><td style="font-size: 20px; font-weight: bold;text-align: center;">{next}</td></tr></table>
  }

  def articleDetails(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
    SHtml.jsonForm(json,
                   xHtml ++ <input type="hidden" id="shop" name ="shop" value={shop.id} /><input type="hidden" id="article" name ="article" value="" />)
  }
 
  def formlist(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
    SHtml.jsonForm(json,
                   list(xHtml) ++
                   <input type="hidden" id="shop" name ="shop" value={shop.id} /><input type="hidden" id="article" name ="article" />
    )
  }

  def newsDetails(xHtml: NodeSeq) = {
    xHtml
//    val shop = ApiClient.shops(Shop.getShopId())
//    SHtml.jsonForm(newsJson,
//                   xHtml ++ <input type="hidden" id="shop" name ="shop" value={shop.id} /><input type="hidden" id="article" name ="article" value="" />)
  }

  def newsAjax(config: String) = {
    println("newsAjax: "+config)
    val attrs= config.split("/")
    JsCmds.SetHtml("divarticle",newsProductHtml(config,attrs(0),attrs(1),urlDecode(attrs(2))))
  }

  def newsProductHtml(id:String, productId:String, viewId:String,text: String) = {
    val p =ProductCreator.product(Shop.getShopId(), productId, Array(text))
    println(p.createXml.toString)
    val res = newsProduct(p, Shop.getShopId(), productId, viewId, text)
    println(res.toString)
    res
  }

  def newsProduct(p: ProductInterface,shopId:String, productId:String, viewId:String, text:String):NodeSeq = {
    <form action={"/shop/"+shopId+"/buy/"+productId+"/"+text} method="get">
      <span>{text}</span><br/>
      <span style="font-size: 25px; font-weight: bold;">{p.formattedPrice}<br/></span>
      {sizes(<sizes:size />, p, "")} <br/>
      <input type="submit" value="Buy"/>
    </form>
  }

  def articleAjax(articleId: String) = {
    JsCmds.SetHtml("divarticle",getArticleHtml(articleId))
  }

  def getArticleHtml(articleId: String) = {
    val shop = ApiClient.shops(Shop.getShopId())
    shop.fetchArticles(true)
    val articleSize = shop.articles.size

    val a = shop.articles(articleId);
    article(template, a)
  }

  def article(xHtml: NodeSeq, a: Article):NodeSeq = {
    bind("articles", xHtml,
         ("name" -> <span>{a.name}</span>),
         ("price" -> <span style="font-size: 25px; font-weight: bold;">{a.formattedPrice}<br/></span>),
         ("image" -> productImage(a,a.product.defaultViewId, a.product.appearanceId)) ,
         ("appearanceHidden" -> <input type="hidden" id={"appearance"+a.id} value={a.product.appearanceId} name={"appearance"+a.id} />),
         ("appearances" -> appearances(chooseTemplate("articles","appearances",xHtml), a)),
         ("sizes" -> sizes(chooseTemplate("articles","sizes",xHtml), a)),
         //("previews" -> previews(chooseTemplate("articles","previews",xHtml), a)),
         ("cart"-> <input type="submit" value="Add to Cart" onclick={JsCmds.SetValById("article",JE.Str(a.id)).toJsCmd} />)
    )
  }

  def texts(xHtml: NodeSeq) = {
    val shopId = Shop.getShopId()
    val source = S.param("news") openOr "lvz"
    
    //println(source)
    val texts = TextLoader.texts(source)
    val productId = S.param("product") openOr TextLoader.products(source)
    val viewId = S.param("view") openOr "1"
    //println(xHtml)

    NodeSeq.Empty ++ texts.flatMap(text => {
        //println(text);
        val ajaxJs = SHtml.ajaxCall(Str(productId+"/"+viewId+"/"+urlEncode(text.replaceAll("/", "-"))), newsAjax(_))
        val imageUrl = ProductCreator.productImageUrl(Shop.getShopId(), productId, viewId, Array(text), productSize)
        bind("articles", xHtml,
             ("image" -> <img class="articlesImage" onclick={ajaxJs._2} width={400.toString} height={400.toString} src={imageUrl} />)
        )
      })
  }
  
  def list(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
    val start = getStart
    val end = getEnd
    val size = end-start+1
    shop.fetchArticles(true)
    val articleSize = shop.articles.size
    val articles = shop.articles.slice(start, end + 1, true);
    articles.filter(a => a.product!=null).flatMap(a => article(xHtml, a))
  }

  import JsCmds._
  object json extends JsonHandler {
    def apply(in: Any): JsCmd = SetHtml("json_result", in match {
        case JsonCmd("processForm", _, p: Map[String, String], _) => {
            val shopId = urlDecode(p("shop"))
            val articleId = urlDecode(p("article"))
            val appearanceId = if(p.contains("appearance"+articleId)) urlDecode(p("appearance"+articleId)) else ""
            val sizeId = urlDecode(p("size"+articleId))
            S.set("shopId",shopId)
            Basket.addArticle(shopId,articleId, appearanceId, sizeId);
            val basket = Basket(shopId);
            Text("")
//            <b>{p}</b><br />
//            <b>articleId:{articleId}</b><br />
//            <b>sizeId:{sizeId}</b><br />
//            <b>appearanceId:{appearanceId}</b><br />
//            <b>BasketId:{basket.id}</b><br />
//            <b>BasketSize: {basket.basketItems.size}</b>
          }
        case x => <b>Problem... didn’t handle JSON message {x}</b>
      })
  }


  def productImage(article: Article, view:String, appearance: String) = {
    val ajaxJs = SHtml.ajaxCall(Str(article.id), articleAjax(_))
    <img  class="articlesImage" onclick={ajaxJs._2} id={"Product"+article.id} width={""+productSize} height={""+productSize} src={ImageUrl.productUrl(article.product,view, appearance,productSize)} />
  }
            
  
  def setRadioActiveStateJs(id: String, active:Boolean) = {
    if (active) JsCmds.SetElemById(id,JE.Num(0), "disabled")
    else JsCmds.SetElemById(id,JE.Num(1), "disabled") & JsCmds.SetElemById(id,JE.Num(0), "checked")
  }

  def getJQueryTest() : JsCmd = {
    JqJE.JqDoc.>>(JqJE.JqAttr("ready",JE.AnonFunc(JsCmds.Alert("TESTTTT!")))).cmd
  }


  def appearances(xHtml:NodeSeq, article:Article) = {
    val product = article.product
    val viewId = product.defaultViewId
    product.possibleAppearances.filter(ap  => ap!= null && !product.productType.possibleSizes(ap.id).isEmpty).
    flatMap(ap => {
        val setOutImage = JsCmds.SetElemById("Product"+article.id,
                                             JE.JsRaw("\""+ImageUrl.productUrl(product, viewId,"APID", productSize)+"\".replace('APID',"+
                                                      JE.ValById("appearance"+article.id).toJsCmd+")") ,"src")
        val setImage = JsCmds.SetElemById("Product"+article.id, Str(ImageUrl.productUrl(product, viewId, ap.id, productSize)),"src")
        val setAppearance = JsCmds.SetElemById("appearance"+article.id,Str(ap.id),"value")
        val posSizes = product.productType.possibleSizes(ap.id);
        val setSizes = product.productType.sizes map(a=> setRadioActiveStateJs("size"+article.id+a.id, posSizes.contains(a))) reduceLeft (_ & _)

        bind("appearances", xHtml,
             ("appearance" -> SHtml.a(<img style="border: 2px solid black;" width={""+appearanceSize} height={""+appearanceSize}
                                      src={ImageUrl.productUrl(product, viewId, ap.id, productSize)} />, setImage & setAppearance & setSizes,
                                      ("onmouseover"->setImage.toJsCmd) , ("onmouseout"->setOutImage.toJsCmd)
            )
          )
        )
      }
    )
  }

  def previews(xHtml:NodeSeq, article:Article) = {
    val product = article.product
    product.productType.views.
    flatMap(view => {
        bind("previews", xHtml,
             ("image" -> Text(" "))
        )
        
      }
    )
  }


  def sizes(xHtml:NodeSeq, article:Article):NodeSeq = {
    sizes(xHtml, article.product, article.id)
  }

  def sizes(xHtml:NodeSeq, product:ProductInterface, id:String):NodeSeq = {
    val productType = product.productType;
    val activeSizes = productType.possibleSizes(product.appearanceId)
    val radios = SHtml.radio(productType.sizes.map(_.id), if (activeSizes.size>0) Full(activeSizes.head.id) else Empty, selectedSize(_), ("name"->("size"+id)))
    0 until productType.sizes.size flatMap( a =>
      bind("sizes", xHtml,
           ("size" -> //radios(a).asInstanceOf[Elem] % ("id" -> ("sizes"+article.id+productType.sizes(a).id)))
            <span style="font-size: 15px; font-weight: bold;">
            {if (activeSizes.contains(productType.sizes(a))) {
                (radios(a).asInstanceOf[Elem] % ("id" -> ("size"+id+productType.sizes(a).id)))
              } else {
                (radios(a).asInstanceOf[Elem] % ("id" -> ("size"+id+productType.sizes(a).id)) % ("disabled" -> "disabled"))
              }
            }
            {productType.sizes(a).name}
            &nbsp;</span>)
      )
    )
  }


}

