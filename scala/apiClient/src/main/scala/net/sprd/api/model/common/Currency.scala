/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.model.common

import net.sprd.api.ListEntity
import net.sprd.api.model.RootResource

class Currency(data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full)  {
  lazy val plain = (fullData \ "plain").text
  lazy val isoCode = (fullData \ "isoCode").text
  lazy val symbol = (fullData \ "symbol").text
  lazy val decimalCount = (fullData \ "decimalCount").text.toInt
  lazy val pattern = (fullData \ "pattern").text

  protected lazy val intPattern = pattern.replace("$", symbol).replace("%", "%1."+decimalCount+"f")

  def format(value:Double) = {
    intPattern.format(value)
  }
}
