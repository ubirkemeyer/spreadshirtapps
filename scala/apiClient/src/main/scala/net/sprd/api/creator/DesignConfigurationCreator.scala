package net.sprd.api.creator

import net.sprd.api.model.design._
import net.sprd.api.model.inventory._
import net.sprd.api.images._

object DesignConfigurationCreator {
  
  def getPrintColorDesignConfiguration (printType: PrintType, printArea: PrintArea, design: Design) = {
    getCentralDesignConfigurationSvg(printType, printArea, design, Positioner.centralizeDesign(printType, printArea, design)._3,0.0, null, Colorizer.bestColors(printType, Seq.empty ++ design.colors.values))
  }
  
  def getCentralDesignConfigurationSvg(printType: PrintType, printArea: PrintArea, design: Design, width: Double, angle: Double = 0.0, colorRGBs: Seq[String], colorIds: Seq[String]) {
    val height = design.size ._2/design.size._1 * width
    val (imgWidth, imgHeight) = imageSize(printType.dpi, width, height)
    val fillColors = if (colorRGBs==null || (colorRGBs.isEmpty && !colorIds.isEmpty)) colorRGBs else colorRGBs
    <svg><design designId={design.id} width={width.toString} height={height.toString} colorRGBs={seqParamsString(colorRGBs)} colorIds={seqParamsString(colorIds)} xlink:href={UrlCreator.getDesignUrl(design.id, imgWidth, imgHeight, colorRGBs)}/></svg>
  }
  
  def seqParamsString(params:Seq[String]) = params match {
    case null => ""
    case _ => params reduceLeft (_+","+_)
  }
  
  def imageSize(dpi: Double, width: Double, height: Double) = {
    val widthPx = Positioner.toPixel(dpi, width)
    val heightPx = Positioner.toPixel(dpi, height)
    val imageWidth = Math.ceil(width / 50)*50
    (Math.round(imageWidth).asInstanceOf[Int], Math.round(heightPx *(imageWidth/widthPx)).asInstanceOf[Int])
  }
    
 

}
