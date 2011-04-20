/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern
import net.sprd.api.model.product.ConfigurationUpdate
import net.sprd.api.model.product.ProductInterface
import net.sprd.api.model.product.ProductUpdate
import net.sprd.api.model.product.TextConfigurationUpdate
import scala.xml._
import scala.Math

object TextConfigurationReplacer {

  val textPattern = Pattern.compile("""!TEXT(\d*)[^!]*!""")
  val datePattern = Pattern.compile("""!D([^!]*)\|[^!]*!""")


  def handleTextConfiguration(config: TextConfigurationUpdate, texts: Array[String]) = {
    val tspans = Tspan.extract(config)
    var addOffset = 0.0
    val newTspans = tspans.flatMap(tspan => {
        val y = tspan.pos._2
        val resultTspans = handleTspan(tspan, addOffset, config, texts)
        addOffset += (resultTspans.last.pos._2 - y)
        resultTspans
     })
    config.svg= Tspan.svg(newTspans)
    config
  }

  def handleTspan(tspan:Tspan, addOffset: Double, config:TextConfigurationUpdate, texts: Array[String]) = {
    val content = tspan.text
    var newContent = handleText(content, texts)
    newContent = handleDate(newContent)
    val font = tspan.font
    val fontSize = Math.max(font.minimalSize,tspan.fontSize)
    val width = Math.min(Math.min((config.textTag \ "@width").text.toDouble, config.printType.size._1),TextSize.size(font, fontSize)(content)._3)
    //println("WIDTH: "+width)
    val (lines, lineWidth, lineHeight) = LineBreaker.breakWidth(font, fontSize, width, TextSplitter.splitToWords(newContent))
    //println(lineWidth)
    val x = tspan.pos._1
    var y = tspan.pos._2 + addOffset
    val leading = 0;//TextSize.lineOffset(font, fontSize)

    val newTspans = lines.map(a => {        
        val newTspan = Tspan(tspan);
        newTspan.text = a;
        newTspan.pos = (x, y);
        y+=lineHeight+leading;
        newTspan
      })
        
    newTspans
  }

  def handleText(content:String, texts: Array[String]) = {
    val matcher = textPattern.matcher(content)
    var newContent = content
    while(matcher.find()) {
      val patternString = matcher.group
      val textId = matcher.group(1).toInt
      val replace = texts(textId)
      newContent = matcher.replaceFirst(replace)
    }
    newContent
  }

  def handleDate(content:String) = {
    val matcher = datePattern.matcher(content)
    var newContent = content
    while(matcher.find()) {
      val patternString = matcher.group
      val datePattern = matcher.group(1)
      val dateFormat = new SimpleDateFormat(datePattern)
      val replace = dateFormat.format(new Date())
      newContent = matcher.replaceFirst(replace)
    }
    newContent
  }

  def replace(product:ProductInterface, configurationId : String, text: String) = {
    val newProduct = new ProductUpdate(product)
    val configOption = newProduct.findConfiguration(configurationId)
    if (!configOption.isEmpty) {
      replaceTextSvg(configOption.get, text)
    }
    newProduct.configurations.foreach(c => (checkConfiguration(c)))
    newProduct
  }

  def replaceTextSvg(config: ConfigurationUpdate, text: String)  {
    val textTag = (config.svg \ "text")(0)
    val width = (textTag\"@width").text.toDouble
    val fontFamilyId = (textTag\"@fontFamilyId").text
    val fontId = (textTag\"@fontId").text

    val font = config.product.root.fontFamilies(fontFamilyId).fonts(fontId)

    val fontSize = Math.max(font.minimalSize,(textTag\"@font-size").text.toDouble)
    val textAnchor = (textTag\"@text-anchor").text
    val (lines, lineWidth, lineHeight) = LineBreaker.breakWidth(font, fontSize, width, TextSplitter.splitToWords(text))
    var y = (textTag\"@y").text.toDouble
    var x = (textTag\"@x").text.toDouble
//    println("Width: "+width+" - "+lineWidth)
    var xDiff = (width - lineWidth) / 2
//    println("X: "+x+" - "+(x - xDiff))
    config.offset = (config.offset._1 + xDiff, config.offset._2)
    x = x - xDiff
    val newTag = (<text>{
          lines.map(a => {y+=lineHeight; (<tspan text-anchor={textAnchor} x={x.toString} y={(y-lineHeight).toString} >{a}</tspan>)})
        }</text>) %
    textTag.attributes %
    new UnprefixedAttribute("font-size" ,fontSize.toString, scala.xml.Null) %
    new UnprefixedAttribute("x", x.toString, scala.xml.Null)
    config.svg = <svg>{newTag}</svg>
  }

  def checkConfiguration(config: ConfigurationUpdate) {
    config.configurationType match {
      case "text" => checkTextConfigurtion(config)
      case _ =>
    }
  }

  def checkTextConfigurtion(config: ConfigurationUpdate) {
    val textTag = (config.svg \ "text")(0).asInstanceOf[Elem]
    val fontFamilyId = (textTag\"@fontFamilyId").text
    val fontId = (textTag\"@fontId").text
    val font = config.product.root.fontFamilies(fontFamilyId).fonts(fontId)
    val fontSize = (textTag\"@font-size").text.toDouble
    if (fontSize < font.minimalSize) {
      config.svg = <svg>{textTag % new UnprefixedAttribute("font-size" ,font.minimalSize.toString, scala.xml.Null)}</svg>
    }
  }

  def getAttributes(text : Node, tspan: Node) = {
    text.attributes.append(tspan.attributes)
  }

}
