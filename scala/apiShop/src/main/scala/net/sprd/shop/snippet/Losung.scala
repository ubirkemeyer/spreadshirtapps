package net.sprd.shop.snippet

import java.text.SimpleDateFormat
import java.util.Date
import net.liftweb.http.LiftRules
import net.liftweb.http.RequestVar
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE
import net.liftweb.http.js.JE.Str
import net.liftweb.http.js.JsCmds
import net.sprd.api._
import net.sprd.shop.lib.ImageUrl
import net.sprd.shop.lib.ProductCreator
import net.sprd.shop.lib.Shop
import scala.collection.mutable.ArrayBuffer
import scala.xml.Elem
import scala.xml.NodeSeq
import net.sprd.api.model.inventory.Size
import net.sprd.api.model.product._

import _root_.net.liftweb.util.Helpers._
import scala.xml.Text
import scala.collection._

class Losung {

  val productSize = 400
  val appearanceSize = 50

  val shopId = "539719"

  val products = Array("18385368", "18385376", "18385386", "18385356",
    null, "18385328", "18385340", "18385322", "18385332")

  lazy val sizes = getAllSizes()

  val datePattern = "d. MMMM yyyy"

  object losung extends RequestVar[ArrayBuffer[Tuple2[String, String]]](ArrayBuffer.empty)

  object losungDate extends RequestVar[Date](new Date())

  def getAllSizes() = {
    var sizeList = SortedSet.empty[Int];
    var sizeMap = mutable.Map.empty[Int, Size];
    products.foreach(pid => if (pid != null) {
      val product = ApiClient.shops(shopId).products(pid)
      product.productType.sizes.foreach(size => if (!sizeList.contains(size.id.toInt)) {
        sizeList = sizeList + size.id.toInt
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
      <script type="text/javascript" src={ "/" + LiftRules.resourceServerPath + "/jlift.js" }/>
    </head>
  }

  def getDate() = {
    if (losungDate == null) {
      val day = S.param("day").get.toInt
      val month = S.param("month").get.toInt
      val year = S.param("year").get.toInt
      losungDate(new Date(year, month, day))
    }
    losungDate.get
  }

  def getLosung(losungIndex: Int) = {
    if (losung.isEmpty || losung.get.isEmpty) {
      val date = getDate()
      val losung1 = net.sprd.shop.lib.Losung.getLosung(date)
      losung(losung1)
    }
    losung.get(losungIndex)
  }

  def navi(xHtml: NodeSeq) = {
    <a href={ "/zitate" }>{ ">>" }</a>;
  }

  def formlist(xHtml: NodeSeq) = {
    val shop = ApiClient.shops(Shop.getShopId())
     <form action="/zitate" id="myform" method="get">
       { product(xHtml, 0) }
       { product(xHtml, 1) }
     </form>
  }

  def getDateString() = {
    val dateFormat = new SimpleDateFormat(datePattern)
    val dateString = dateFormat.format(getDate)
    "Herrenhuter Losung vom " + dateString
  }

  def product(xHtml: NodeSeq, losungIndex: Int): NodeSeq = {
    val (vers, position) = getLosung(losungIndex)
    val texts = Array(getDateString(), vers, position)
    val p = ProductCreator.product(shopId, products(0), texts)
    val pidUrl = S.contextPath + ProductCreator.productImageUrl(shopId, "PID", "1", texts, productSize)
    val pidBuyUrl = S.contextPath + ProductCreator.productBuyUrl(shopId, "PID", texts)
    val buyCmd = JsCmds.SetElemById("myform",
      JE.JsRaw("'" + pidBuyUrl.replaceAll("'", "\'") + "'.replace('PID'," +
        JE.ValById("variance").toJsCmd + ")"), "action")
    val zitateUrl = S.contextPath + "/zitate"
    val nextCmd = new JE.JsRaw("location.href='" + zitateUrl + "'")
    bind("losung", xHtml, ("name" -> <span>{ position + ": " + vers }</span>), ("id" -> <input type="hidden" id="article" name="article" value={ "" + losungIndex }/>), ("price" -> <span style="font-size: 25px; font-weight: bold;">{ p.formattedPrice }<br/></span>), ("image" -> productImage(products(0), "1", texts)), ("varianceHidden" -> (<input type="hidden" id="variance" value={ products(0) }/>)), ("variances" -> variances(chooseTemplate("losung", "variances", xHtml), texts)), ("sizes" -> sizes(chooseTemplate("zitate", "sizes", xHtml))), ("buy" -> <input type="submit" value="Kaufen" onclick={ buyCmd.toJsCmd }/>), ("next" -> <input type="button" value="Nächstes Zitat" onclick={ nextCmd.toJsCmd }/>)
      )
  }

  def productImage(productId: String, view: String, texts: Array[String]) = {
    val url = ProductCreator.productImageUrl(shopId, productId, view, texts, productSize)
     <img class="articlesImage" id={ "zitatImage" } width={ "" + productSize } height={ "" + productSize } src={ url }/>;
  }

  def setRadioActiveStateJs(id: String, active: Boolean) = {
    if (active) JsCmds.SetElemById(id, JE.Num(0), "disabled")
    else JsCmds.SetElemById(id, JE.Num(1), "disabled") & JsCmds.SetElemById(id, JE.Num(0), "checked")
  }

  def variances(xHtml: NodeSeq, texts: Array[String]) = {
    val viewId = 1
    val pidUrl = S.contextPath + ProductCreator.productImageUrl(shopId, "PID", "1", texts, productSize)
    NodeSeq.Empty ++ products.flatMap(pid => if (pid == null) {
      <br/>
    } else {
      val product = ApiClient.shops(shopId).products(pid)
      val url = ProductCreator.productImageUrl(shopId, pid, "1", texts, productSize)
      val productTypeImageUrl = ImageUrl.productTypeUrl(product.productTypeId, "1", product.appearanceId, appearanceSize);
      val setOutImage = JsCmds.SetElemById("zitatImage",
        JE.JsRaw("'" + pidUrl.replaceAll("'", "\'") + "'.replace('PID'," +
          JE.ValById("variance").toJsCmd + ")"), "src")
      val setImage = JsCmds.SetElemById("zitatImage", Str(S.contextPath + url), "src")
      val setVarianceImage = JsCmds.SetElemById("variance" + pid, Str(S.contextPath + url), "src")
      val setAppearance = JsCmds.SetElemById("variance", Str(pid), "value")
      val posSizes = product.productType.possibleSizes(product.appearanceId);
      //sizes foreach (a=> {println(a + "-"+ posSizes.find(_.id == a) + "-" +posSizes.find(_.id == a).isDefined)})
      val setSizes = sizes map (a => setRadioActiveStateJs("size" + a.id, posSizes.find(_.id == a.id).isDefined)) reduceLeft (_ & _)

      bind("zitate", xHtml, ("variance" -> SHtml.a(<img id={ "variance" + pid } style="border: 2px solid black;" width={ "" + appearanceSize } height={ "" + appearanceSize } src={ productTypeImageUrl }/>, setImage & setAppearance & setSizes, ("onmouseover" -> (setImage & setVarianceImage).toJsCmd), ("onmouseout" -> setOutImage.toJsCmd)
        ))
        )
    }
      )
  }

  def title(xHtml: NodeSeq): NodeSeq = {
    Text("Zitate-Online.de")
  }

  def sizes(xHtml: NodeSeq): NodeSeq = {
    sizes(xHtml, ApiClient.shops(shopId).products(products(0)), "")
  }

  def sizes(xHtml: NodeSeq, product: ProductInterface, id: String): NodeSeq = {
    val productType = product.productType;
    val activeSizes = productType.possibleSizes(product.appearanceId)
    NodeSeq.Empty ++ sizes.map(size => {
      <span style="font-size: 15px; font-weight: bold;">
        {
          var xml = <input type="radio" name="size" value={ size.id } id={ "size" + size.id }/>
          if (activeSizes.find(as => as.id == size.id).isDefined) {
            if (activeSizes(0).id == size.id) {
              xml % ("checked" -> "checked")
            } else xml
          } else xml % ("disabled" -> "disabled")
        }
        { size.name }
        &nbsp;
      </span>
    })
  }

}

