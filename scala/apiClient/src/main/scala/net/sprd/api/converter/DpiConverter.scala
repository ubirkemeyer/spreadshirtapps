/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.converter

object DpiConverter {
  
  val mmDpi=25.4

  def convertDpi(source:Double, sourceDpi:Double, targetDpi:Double) =
    source/sourceDpi*targetDpi

  def convertToMm(source:Double, sourceDpi:Double) =
    convertDpi(source, sourceDpi, mmDpi)

  def convertFromMm(mm: Double, targetDpi:Double) =
    convertDpi(mm, mmDpi, targetDpi)


}
