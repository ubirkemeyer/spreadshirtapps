/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import net.sprd.api.converter.DpiConverter
import net.sprd.api.model.inventory.Font
import net.sprd.api.rect.Rect

object TextSize {

  def size(font: Font, fontSize: Double)(text:String) = {

    val dpi = 100;
    
    val style = (if (font.isBold) java.awt.Font.BOLD else java.awt.Font.PLAIN) |
    (if (font.isItalic) java.awt.Font.ITALIC  else java.awt.Font.PLAIN)
    val internalFontSize = Math.round(DpiConverter.convertFromMm(fontSize, dpi)).toInt
    val fontInternal = new java.awt.Font(font.name, style, internalFontSize);
    //println(font.name+" = "+fontInternal.getFontName)
    val vector =
      fontInternal.createGlyphVector(new FontRenderContext(new AffineTransform(), false, false), text);
    val logicalBound = vector.getLogicalBounds();
    val visualBound = vector.getVisualBounds();
    val fullRect = Rect.union((logicalBound.getX(),logicalBound.getY(), logicalBound.getWidth(), logicalBound.getHeight()),
               (visualBound.getX(),visualBound.getY(), visualBound.getWidth(), visualBound.getHeight()))
    //println(Rect.transform(fullRect, DpiConverter.convertToMm(_, dpi)))
    Rect.transform(fullRect, DpiConverter.convertToMm(_, dpi))
  }

  def lineOffset(font: Font, fontSize: Double) = {
    val dpi = 100;

    val style = (if (font.isBold) java.awt.Font.BOLD else java.awt.Font.PLAIN) |
    (if (font.isItalic) java.awt.Font.ITALIC  else java.awt.Font.PLAIN)
    val internalFontSize = Math.round(DpiConverter.convertFromMm(fontSize, dpi)).toInt
    val fontInternal = new java.awt.Font(font.name, style, internalFontSize);
    val lineMetrics =
      fontInternal.getLineMetrics("p", new FontRenderContext(new AffineTransform(), false, false));
    DpiConverter.convertToMm(lineMetrics.getLeading, dpi)
  }

}
