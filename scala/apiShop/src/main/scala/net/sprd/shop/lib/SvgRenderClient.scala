/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib


import net.sprd.svg.SvgRenderer
import scala.xml.NodeSeq

object SvgRenderClient {

  def performRenderRequest(svgContent:NodeSeq) : Array[Byte] =  {
    SvgRenderer.pngRender(svgContent)
  }
}
