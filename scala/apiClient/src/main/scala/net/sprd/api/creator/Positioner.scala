package net.sprd.api.creator

import net.sprd.api.model.design._
import net.sprd.api.model.inventory._
import net.sprd.api.model.common._

object Positioner {
  
  def centralizeDesign(printType: PrintType, printArea: PrintArea, design: Design) = {
    val (minWidth, maxWidth) = getPossibleWidth(printType, printArea, design)
    var resultWidth = printArea.size._1 * 0.667;
    if (resultWidth < minWidth) resultWidth = minWidth
    if (resultWidth > maxWidth) resultWidth = maxWidth
    val resultHeight = resultWidth /design.size._1 * design.size._2
    val posX = (printArea.size._1 - resultWidth) * 0.5
    val posY = (printArea.size._2 - resultHeight) * 0.5
    (posX, posY, resultWidth, resultHeight)
  }
  
  def getPossibleWidth(printType: PrintType, printArea: PrintArea, design: Design) :(Double,Double) = {
    val paSize = printArea.size;
    val designSize = design.size;
    if (!design.printTypeIds.contains(printType.id)) {
      error("printType is not possible for design")
    }
    if (printType.scaleability  == PrintType.SHRINKABLE) {
      //pixel printtype
      if (design.unit == Units.unitMm) {
        //vector image
        if ((designSize._1/designSize._2) > (paSize._1/paSize._2)) {
          (paSize._1, 1.0)
        } else {
          (paSize._2 * (designSize._1/designSize._2) , 0)
        }
      } else {
        //pixel image
        val designSizeMM = (toMM(printType.dpi,designSize._1), toMM(printType.dpi,designSize._1));
        if (designSizeMM._1 < paSize._1 && designSizeMM._2 < paSize._2  ) {
          (designSizeMM._1, 1.0)
        } else if ((designSize._1/designSize._2) > (paSize._1/paSize._2)) {
          (paSize._1, 1.0)
        } else {
          (paSize._2 * (designSize._1/designSize._2) , 0)
        }
      }
    } else {
      //vector printtype
      if (design.unit == Units.unitMm) {
        //vector image
        if (designSize._1 * design.minScale > paSize._1 || designSize._2 * design.minScale > paSize._2) {
          (-1,0)
        } else
        if ((designSize._1/designSize._2) > (paSize._1/paSize._2)) {
          (paSize._1, designSize._1 * design.minScale)
        } else {
          (paSize._2 * (designSize._1/designSize._2), designSize._1 * design.minScale)
        }
      } else {
        (-1,0)
      }
    }
  }
  
  def toMM(dpi:Double,pixel:Double) = pixel/dpi*25.4
  def toPixel(dpi:Double,mm:Double) = mm/25.4*dpi

}
