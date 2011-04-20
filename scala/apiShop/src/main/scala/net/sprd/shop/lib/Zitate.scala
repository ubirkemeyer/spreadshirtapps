/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.shop.lib

import java.util.regex.Pattern
import net.sprd.api.creator.text.TextDeEncoder

object Zitate {

  val source = "http://www.zitate-online.de/zufallszitat.js.php"

  val encoding = "utf-8"

  val pattern = Pattern.compile("""<strong><a[^>]*>([^<]*)<[^>]*/a><[^>]*/strong>: ([^<]*)<[^>]*/div>""")
    

  def nextZitat() = {
    var zitat = ""
    var author = ""
    while (zitat=="" || zitat.length>200) {
      val content = TextLoader.getContent(source,encoding).replaceAll("""<br \\/>""", " ").replaceAll("""\\'""", "'")
      println(content)
      val matcher = pattern.matcher(content)
      
      if (matcher.find()) {
        zitat = TextDeEncoder.decode(matcher.group(2)).replaceAll("  ", " ").replaceAll("  ", " ")
        author = TextDeEncoder.decode(matcher.group(1)).replaceAll("  ", " ").replaceAll("  ", " ")
      }
    }
    (zitat, author)
    
  }

}
