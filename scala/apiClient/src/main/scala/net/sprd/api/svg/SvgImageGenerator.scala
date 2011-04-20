package net.sprd.api.svg

import net.sprd.api.model._
import net.sprd.api.model.inventory._
import net.sprd.api.model.product._
import scala.xml._

object SvgImageGenerator {
  def generateProductViewImage(product: ProductInterface, viewId: String, size:Int = 1200) = {
    val productType = product.productType
    val view = productType.viewMap(viewId)
    <svg xml:space="preserve" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg"
      viewBox={"0 0 "+view.size._1+" "+view.size._2} width={size.toString} height={size.toString}>
      {generateProductSvg(product, view, size)}
    </svg>
  }
  
  private def generateProductSvg(product: ProductInterface, view: View, size:Int) = {
    val viewMapConfigurations = view.viewMaps.valuesIterator map (a => a -> (product.configurations filter (b => b.printAreaId == a.printAreaId))) filter (a => a._2.size > 0)
    val sizeInternal = Math.ceil(size/50).intValue * 50
    val viewUrl = "http://image.spreadshirt.net/image-server/v1/productTypes/"+view.productType.id+"/views/"+view.id+"/appearances/"+product.appearanceId+",width="+sizeInternal+".png"
    val subSvg = viewMapConfigurations map (a => generatePrintAreaConfigurationsSvg(a._1, a._2))
    <g>
      <image width={view.size._1.toString} height={view.size._2.toString} xlink:href={viewUrl}/>
      {subSvg}
    </g>
  }
  
  private def generatePrintAreaConfigurationsSvg(map: ViewMap, configurations: Iterable[ConfigurationInterface]) =
     <g transform={"translate("+map.offset._1+" "+map.offset._2+")"}>
       {configurations map (a => 
         <g transform={"translate(" + a.offset._1 + " " + a.offset._2 + ")" }>{generateConfigurationSvg(a)}</g>)}
     </g>
  
  private def generateConfigurationSvg(configuration: ConfigurationInterface) = configuration match {
    case designConfiguration: DesignConfigurationInterface => {
      val image = designConfiguration.svg \ "image";
      <image width={image\"@width"} height={image\"@height"} transform={image\"@transform"} xlink:href={setWidth((image\"@{http://www.w3.org/1999/xlink}href").text,600)}/>}
    case textConfiguration: TextConfigurationInterface => removeNameSpace(textConfiguration.svg \ "text")
  }
  
  private def removeNameSpace(xml : NodeSeq) =
    XML.loadString(xml.toString.replace("xmlns=\"http://api.spreadshirt.net\"","").replaceAll(""">\s+<""","><"))
  
  val urlWithParameterRegex = """(http://image.spreadshirt.)([^\?]+)\?(.+)""".r
  
  private def setWidth(href: String, width: Int) = href match {
    case urlWithParameterRegex(dom,link,params) => "http://origin.spreadshirt."+link+"?width="+width+"&"+params
    case _ => href+"?width="+width
  }
}
