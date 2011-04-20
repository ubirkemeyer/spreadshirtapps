/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern
import net.sprd.api.creator.text.TextDeEncoder
import scala.collection.mutable.ArrayBuffer

object Losung {

  val source = "http://www.losungen.de"

  val encoding = "utf-8"

  val pattern = Pattern.compile("""<strong><a[^>]*>([^<]*)<[^>]*/a><[^>]*/strong>: ([^<]*)<[^>]*/div>""")

  val datePattern = "yyyy/MMdd"

  def getUrl(date:Date) = {
    val dateFormat = new SimpleDateFormat(datePattern)
    val dateString = dateFormat.format(date)
    source+"/"+dateString+".html"
  }

  def getLosung(date: Date): ArrayBuffer[Tuple2[String,String]] = {
    ArrayBuffer.empty
  }

}
