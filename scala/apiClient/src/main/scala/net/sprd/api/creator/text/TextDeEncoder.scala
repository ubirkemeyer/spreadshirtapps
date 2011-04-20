/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

object TextDeEncoder {
  
  def encode(text:String) = {
    text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
  }
  
  def decode(text:String) = {
    text.replaceAll("&lt;","<").replaceAll("&gt;",">").
    replaceAll("&amp;","&").replaceAll("&ndash;","&").
    replaceAll("&quot;","\"").
    replaceAll("&agrave;","à").replaceAll("&Agrave;","À").
    replaceAll("&acirc;","â").replaceAll("&auml;","ä").
    replaceAll("&Auml;","Ä").replaceAll("&Acirc;","Â").
    replaceAll("&aring;","å").replaceAll("&Aring;","Å").
    replaceAll("&aelig;","æ").replaceAll("&AElig;","Æ" ).
    replaceAll("&ccedil;","ç").replaceAll("&Ccedil;","Ç").
    replaceAll("&eacute;","é").replaceAll("&Eacute;","É" ).
    replaceAll("&egrave;","è").replaceAll("&Egrave;","È").
    replaceAll("&ecirc;","ê").replaceAll("&Ecirc;","Ê").
    replaceAll("&euml;","ë").replaceAll("&Euml;","Ë").
    replaceAll("&iuml;","ï").replaceAll("&Iuml;","Ï").
    replaceAll("&ocirc;","ô").replaceAll("&Ocirc;","Ô").
    replaceAll("&ouml;","ö").replaceAll("&Ouml;","Ö").
    replaceAll("&ograve;", "ò").replaceAll("&Ograve;", "Ò").
    replaceAll("&oslash;","ø").replaceAll("&Oslash;","Ø").
    replaceAll("&szlig;","ß").replaceAll("&ugrave;","ù").
    replaceAll("&Ugrave;","Ù").replaceAll("&ucirc;","û").
    replaceAll("&Ucirc;","Û").replaceAll("&uuml;","ü").
    replaceAll("&Uuml;","Ü").replaceAll("&nbsp;"," ").
    replaceAll("&copy;","\u00a9").
    replaceAll("&reg;","\u00ae").
    replaceAll("&euro;","\u20a0").
    replaceAll("&acute;","´").
    replaceAll("„", "\"").replaceAll("“", "\"").
    replaceAll("&#8230;", "-").replaceAll("&#8220;", "\"").replaceAll("&#8221;", "\"").
    replaceAll("&bdquo;", "\"").replaceAll("&ldquo;", "\"")


//    text.replaceAll("&uuml;", "ü").replaceAll("&auml;", "ä").replaceAll("&ouml;", "ö").
//    replaceAll("&Uuml;", "Ü").replaceAll("&Auml;", "Ä").replaceAll("&Ouml;", "Ö").
//    replaceAll("&quot;", "\"").replaceAll("&szlig;", "ß").replaceAll("&ndash;", "-").
//    replaceAll("&amp;", "&").replaceAll("&bdquo;", "\"").replaceAll("&ldquo;", "\"").
//    replaceAll("&ograve;", "ò").replaceAll("&ccedil;", "ç").replaceAll("&eacute;", "é").
//
//    replaceAll("„", "\"").replaceAll("“", "\"").
//    replaceAll("&#8230;", "-").replaceAll("&#8220;", "\"").replaceAll("&#8221;", "\"")

  }


}
