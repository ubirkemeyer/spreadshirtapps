/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

import java.util.StringTokenizer

object TextSplitter {

  def splitToWords(text : String, withSpaces : Boolean = false) = {
    text.split(' ')
  }

  def splitSubWords(text : String, chars : String = " ,.-:;") = {
    val toks = new StringTokenizer(text, chars, true)
    val parts = new scala.collection.mutable.ArrayBuffer[String]()
    var nextPart = toks.nextToken
    while (toks.hasMoreTokens()) {
      val next = toks.nextToken
      if (chars.contains(next)) {
        nextPart = nextPart + next
      }
      else {
        parts + nextPart
        nextPart = next
      }
    }
    parts + nextPart
    parts
  }
}
