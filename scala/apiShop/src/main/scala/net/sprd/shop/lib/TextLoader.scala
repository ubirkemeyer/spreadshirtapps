/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLDecoder
import java.util.regex.Pattern
import scala.collection._
import net.sprd.api.creator.text.TextDeEncoder

object TextLoader {

  val sources = Map.empty + 
  ("lvz" -> "http://www.lvz-online.de") +
  ("spiegel" -> "http://www.spiegel-online.de") +
  ("sprd" -> "http://feeds.feedburner.com/Spreadblog-de?format=xml") +
  ("welt" -> "http://www.welt.de/") +
  ("sz" -> "http://www.sueddeutsche.de/")+
  ("bild" -> "http://rss.bild.de/bild-home.xml")+
  ("faz" -> "http://www.faz.net/s/RubF3CE08B362D244869BE7984590CB6AC1/Tpl~Epartner~SRss_.xml")
  val regexs = Map.empty + 
  ("lvz" -> Pattern.compile("""<h1[^>]*><a[^>]*>([^<]*)</a></h1>""")) +
  ("spiegel" -> Pattern.compile("""<h3><a[^>]*>([^<]*)</a></h3>""")) +
  ("welt" -> Pattern.compile("""</h3>[^<]*<h2[^>]*>[^<]*<a[^>]*>([^<]*)</a>[^<]*</h2>""")) +
  ("sz" -> Pattern.compile("""<h3 class="Titel"><a[^>]*>([^<]*)</a>[^<]*</h3>"""))
  val encodings = Map.empty +
  ("lvz" -> "utf-8") +
  ("spiegel" -> "iso-8859-1") +
  ("sprd" -> "rss") +
  ("welt" -> "iso-8859-1") +
  ("sz" -> "iso-8859-1") +
  ("bild" -> "rss") +
  ("faz" -> "rss")
  val products = Map.empty +
  ("lvz" -> "18377965") +
  ("spiegel" -> "18377969") +
  ("sprd" -> "18377962") +
  ("welt" -> "18377979") +
  ("sz" -> "18377956") +
  ("bild" -> "18377950") +
  ("faz" -> "18377987")


//  val products = Map.empty +
//  ("lvz" -> "18086828") +
//  ("spiegel" -> "18086825") +
//  ("sprd" -> "18086820") +
//  ("welt" -> "18086822") +
//  ("sz" -> "18086816") +
//  ("bild" -> "18084304")


  
  def texts(source:String) = {
    val sourceUrl=sources(source)
    val encoding = encodings(source)
    if (encoding == "rss") {
      rssTexts(sourceUrl)
    } else {
      val pattern = regexs(source)

      val content = getContent(sourceUrl,encoding).replaceAll("<br />", " ").replaceAll("<br/>", " ").replaceAll("<br>", " ")
      println(content)
      val matcher = pattern.matcher(content)
      var textSeq = mutable.ArrayBuffer.empty[String]
      while(matcher.find()) {
        textSeq += TextDeEncoder.decode(matcher.group(1)).replaceAll("  ", " ").replaceAll("  ", " ")
      }
      textSeq.slice(0, 9)
    }
  }

  def rssTexts(url: String) = {
    val xml = scala.xml.XML.load(new URL(url).openStream)
    val titles = xml \ "channel"\"item"\"title"
    mutable.ArrayBuffer.empty ++ titles.map(_.text) .slice(0,9)
  }

  //&#8230;

  

  def getContent(url:String, encoding:String) = {
    val reader = new BufferedReader(new InputStreamReader(new URL(url).openStream, encoding))
    val builder = new StringBuilder
    var line = reader.readLine
    while (line!=null) {
      builder.append(line)
      line = reader.readLine
    }
    reader.close
    builder.toString
  }


}
