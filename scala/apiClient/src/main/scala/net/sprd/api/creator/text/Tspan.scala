/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

import net.sprd.api.model.inventory.Font
import net.sprd.api.model.product.ProductUpdate
import net.sprd.api.model.product.TextConfigurationInterface
import net.sprd.api.model.product.TextConfigurationUpdate
import scala.xml.Node
import scala.xml.NodeSeq

class Tspan(
  var fontSize:Double,
  var font:Font,
  var textAnchor:String,
  var pos:(Double, Double),
  var offset:(Double, Double),
  var printColorId : String,
  var fill:String,
  var text:String) {

}

object Tspan{
  def apply(configuration:TextConfigurationInterface, text:Node, tspan:Node) = {
    val fontFamiliyId = (if (tspan.attribute("fontFamilyId").isDefined) tspan.attribute("fontFamilyId").get else text.attribute("fontFamilyId").get)(0).text
    val fontId = (if (tspan.attribute("fontId").isDefined) tspan.attribute("fontId").get else text.attribute("fontId").get)(0).text
    val font = configuration.root.fontFamilies(fontFamiliyId).fonts(fontId)
    val fontSize = Math.max( font.minimalSize  ,(if (tspan.attribute("font-size").isDefined) tspan.attribute("font-size").get else text.attribute("font-size").get)(0).text.toDouble)
    val x = (if (tspan.attribute("x").isDefined) tspan.attribute("x").get else text.attribute("x").get)(0).text.toDouble
    val y = (if (tspan.attribute("y").isDefined) tspan.attribute("y").get else text.attribute("y").get)(0).text.toDouble
    val textAnchor = (if (tspan.attribute("text-anchor").isDefined) tspan.attribute("text-anchor").get else text.attribute("text-anchor").get)(0).text
    val printColorId = (if (tspan.attribute("printColorId").isDefined) tspan.attribute("printColorId").get else text.attribute("printColorId").get)(0).text
    val fill = (if (tspan.attribute("fill").isDefined) tspan.attribute("fill").get else text.attribute("fill").get)(0).text
    new Tspan(fontSize, font, textAnchor, (x,y), configuration.offset,printColorId,fill,tspan.text)
  }

  def apply(tspan: Tspan) = {
    new Tspan(tspan.fontSize, tspan.font, tspan.textAnchor, tspan.pos, tspan.offset,tspan.printColorId,tspan.fill,tspan.text)
  }

  def extract(configuration:TextConfigurationInterface) = {
    val svg = configuration.svg
    val text = (svg \ "text")(0)
    if ((text\"tspan").length == 0) {
      Array(Tspan(configuration, text, text))
    } else {
      (text\"tspan").map(Tspan(configuration, text, _)).toArray
    }
  }

  def merge(product: ProductUpdate, printAreaId: String, printTypeId:String ,tspans: Array[Tspan]) = {
    val offset = tspans.foldLeft((Double.MaxValue, Double.MaxValue))((offset,tspan) => (Math.min(offset._1,tspan.offset._1),Math.min(offset._2,tspan.offset._2)))
    val tspanXml = tspans.map(tspan => <tspan 
        font-size={tspan.fontSize.toString}
        font-family={tspan.font.name}
        fontId={tspan.font.id}
        fontFamilyId={tspan.font.fontFamily.id}
        font-weight={if (tspan.font.isBold) "bold" else "normal"}
        font-style={if (tspan.font.isItalic) "italic" else "normal"}
        x={(tspan.pos._1+tspan.offset._1-offset._1).toString}
        y={(tspan.pos._2+tspan.offset._2-offset._2).toString}
        text-anchor={tspan.textAnchor}
        printColorId={tspan.printColorId}
        fill={tspan.fill}
        >{tspan.text}</tspan>)
    val svg = <svg>{(<text>{tspanXml}</text>)%tspanXml(0).attributes}</svg>

    lazy val printColorIdSet = scala.collection.immutable.Set.empty ++ (tspans map (_.printColorId))
    lazy val printColorRGBSet = scala.collection.immutable.Set.empty ++ (tspans map (_.fill))

    new TextConfigurationUpdate(product.root, product,offset,svg, printAreaId, printTypeId, printColorIdSet, printColorRGBSet)
  }

  def svg(tspans: Array[Tspan]) = {
    val tspanXml = NodeSeq.Empty ++ tspans.flatMap(tspan => <tspan
        font-size={tspan.fontSize.toString}
        font-family={tspan.font.name}
        fontId={tspan.font.id}
        fontFamilyId={tspan.font.fontFamily.id}
        font-weight={if (tspan.font.isBold) "bold" else "normal"}
        font-style={if (tspan.font.isItalic) "italic" else "normal"}
        x={tspan.pos._1.toString}
        y={tspan.pos._2.toString}
        text-anchor={tspan.textAnchor}
        printColorId={tspan.printColorId}
        fill={tspan.fill}
        >{tspan.text}</tspan>)
    
    <svg>{(<text>{tspanXml}</text>)%tspanXml(0).attributes}</svg>
  }
}
