/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text

import java.util._



/*
 Silbentrennung in Java
 ----------------------

 Einfache Klasse zur Silbentrennung.

 Anwendung:
 ----------

 -Trennen eines Wortes

 Die Methode String[] spellWord(String) liefert ein String-Array mit den
 einzelnen Silben eines Wortes.

 -Formatieren einer Zeile

 Die Methode String formatLine(String paStr , int paIntWidth) bricht eine
 Zeile in der verlangten Breite um.

 -Formatieren eines Textes

 Die Methode String formatText(String paStr , int paIntWidth) bricht einen
 Text in der verlangten Breite um.

 -Grundsätzliche Vorgehensweise

 Alle beginnenden Konsonanten der zu untersuchenden Silbe werden übersprungen.
 Dann wird über alle Vokale gelaufen. Am Ende der Silbe werden alle Konsonanten
 bis auf den letzten mit einbezogen. Der letzte Konsonant bleibt für die nächste
 Silbe.

 Dann werden folgende Regeln angewendet:

 st wird nicht getrennt
 ft am Ende der Silbe bleibt zusammen
 ng wird nicht getrennt
 sch wird nicht getrennt
 str wird nicht getrennt

 ck wird in kk umgewandelt

 Problem nicht anwendbarer Regeln
 --------------------------------

 Das Wort Seitengestaltung wird in Sei-ten-ge-stal-tung getrennt. Dabei wird eine
 ng-Verbindung, die entsprechend obiger Regeln nicht getrennt werden dürfte,
 getrennt. Dafür habe ich vorgesehen, dass für solche Wörter Trennungen in einer
 HashMap hinterlegt werden.

 Methode addWord( String , String
 Methode addIgnoreCasexxx


 Alternativ wäre es möglich, Teilworte mit den dazugehörigen Trennungen zu
 hinterlegen. Dies führt aber zu einer kombinatorischen Explosion mit
 entsprechend schlechten Laufzeiten.

 Weiter besteht das Problem, dass Teilworte erkannt werden könnten:

 Problem: Auto-rennen <--> Autoren-nennungen

 Es ist sehr problematisch, ob nun die längeren oder kürzeren Worte zum Vergleich
 herangezogen werden sollten.

 Eine weitere denkbare Möglichkeit ist das definieren von Worten und
 Wortkombinationen:

 Wort A: Teilzeit
 Wort B: Mitarbeiter
 Wort C: A+B Teilzeitmitarbeiter

 Dies wird in dieser Klasse wegen der oben genannten Probleme nicht gemacht.

 Problem: ck zu kk oder ß zu ss umgewandelt. Bei nicht erfolgter Trennung muss
 der originale String in den endgültigen Text eingefügt werden.

 Formatieren eines Textes mit Silbentrennung:

 Es ist günstig für die Trennung ein Spezialzeichen zu verwenden, damit eine
 Trennung jederzeit wieder rückgängig gemacht werden kann. Ein weiteres Problem
 ist das Rückgängigmachen von Änderungen, die durch das Trennen entstanden sind (
 ck -> kk , ß -> ss )


 */


/**
 * Silbentrennung in Java
 */

object Spell {



  def main(args: Array[String]) {
    test("Lattenrost"         , "Lat-ten-rost") ;
    test("Genossenschaft"     , "Ge-nos-sen-schaft") ;
    test("Kücker"             , "Kük-ker") ;
    test("Mellouk"            , "Mel-louk") ;
    test("Hut"                , "Hut") ;
    test("Sack"               , "Sack") ;
    test("Tante"              , "Tan-te") ;
    test("Supermann"          , "Su-per-mann") ;
    test("Filzlaus"           , "Filz-laus") ;
    test("Karton"             , "Kar-ton") ;
    test("Projektmitarbeiter" , "Pro-jekt-mit-ar-bei-ter") ;
    test("Ecke"               , "Ek-ke") ;
    test("Anwendung"          , "An-wen-dung") ;
    test("Anwendungen"        , "An-wen-dungen") ;
    test("Errungenschaften"   , "Er-rungen-schaf-ten") ;
    test("Baumstruktur"       , "Baum-struk-tur") ;
    test("Datenbankverbindungen"       , "Da-ten-bank-ver-bin-dungen") ;
    test("resultierenden"       , "re-sul-tie-ren-den") ;
    test("Seitengestaltung"       , "Sei-ten-ge-stal-tung") ;
    test("Autorennen",           "Au-to-ren-nen");
    test("Autorengemeinschaft" , "Au-to-ren-ge-mein-schaft") ;
    test("Musik" , "Mu-sik") ;
    test("Aufgabe" , "Auf-ga-be") ;
    test("Aufgaben" , "Auf-ga-ben") ;
    test("Schaftränke" , "Schaf-trän-ke") ;
    test("Gesamtautorengemeinschaft" , "Ge-samt-au-to-ren-ge-mein-schaft") ;
    test("Betreten" , "Be-tre-ten") ;
    test("Beschreibung" , "Be-schrei-bung") ;
    test("Beschreibungen" , "Be-schrei-bungen") ;
    test("Entwurf" , "Ent-wurf") ;
    test("Ablaufbeschreibung", "Ab-lauf-be-schrei-bung") ;
    test("Ablaufbeschreibungen", "Ab-lauf-be-schrei-bungen") ;
    test("Artikelbezeichnung" , "Ar-ti-kel-be-zeich-nung") ;
    test("Artikelbezeichnungen" , "Ar-ti-kel-be-zeich-nungen") ;
    test("Begründung" , "Be-grün-dung") ;
    test("Lieferantenname" , "Lie-fe-ran-ten-na-me") ; // leider falsch
    test("Lieferantennummer" , "Lie-fe-ran-ten-num-mer") ; // leider falsch
    test("Anforderungstext" , "An-for-de-rungs-text") ; // leider falsch
    test("Listungsinformation" , "Lis-tungs-in-for-ma-tion") ; // leider falsch
    test("Sortiment" , "Sor-ti-ment") ;
    test("Aktion" , "Ak-tion") ;
    test("Bezugsweg" , "Be-zugs-weg") ;
    test("Preis" , "Preis") ;
    test("Auflistung" , "Auf-lis-tung") ;
    test("Benutzer" , "Be-nut-zer") ;
    test("Kontakt" , "Kon-takt") ;
    test("Zeilen" , "Zei-len") ;
    test("Zeichen" , "Zei-chen") ;
    test("Änderungstext" , "Än-de-rungs-text") ;// leider falsch
    test("Ablehnungstext" , "Ab-leh-nungs-text") ;
    test("Bearbeitung" , "Be-ar-bei-tung") ;
    test("Zurücksetzen" , "Zu-rück-set-zen") ;
    test("Freigeben","Frei-ge-ben") ;
    test("Prüfen" , "Prü-fen") ;
    test("Löschen" , "Lö-schen") ;
    test("Datum" , "Da-tum") ;
    test("Vorbelegung","Vor-be-le-gung") ;
    test("folgende","fol-gen-de");
    test("Kenntnis","Kennt-nis");
    test("extern","ex-tern");
    test("externe","ex-ter-ne");
    test("Herr","Herr");
    test("Herren","Her-ren");
    test("Geschäftsbereichsleitung","Ge-schäfts-be-reichs-lei-tung");
    test("Bereich","Be-reich");
    test("freundlich","freund-lich");
    test("freundlichen","freund-li-chen");
    test("Anwendungsentwicklung","An-wen-dungs-ent-wick-lung");
    test("Beauftragung","Be-auf-tra-gung");
    test("Geheimhaltung","Ge-heim-hal-tung");
    test("zugeteilten","zu-ge-teil-ten");
    test("Informationen","In-for-ma-tio-nen");
    test("Empfang","Emp-fang");
    test("Beratung","Be-ra-tung");
    // test( "","");
    // test( "","");
    // test( "","");

    val strText = // ...
      "Sehr geehrte Damen und Herren,\n" + // ...
    "anbei erhalten Sie die Information über meinen Antrag bezüglich meines Anliegens vom benannten Termin entsprechend unserer Absprache.\n" + // ...
    "Mit freundlichen Grüssen\n" ;

    println(formatText(strText , 25)) ;

  }// end method main

  /**
   * Testmethode für die Entwicklng
   */
  def test(paStr:String, paStrExpected:String) {
    println("=====================================================================");

    println(paStr);
    val strResult = arr2Str(spellWord(paStr) , "-") ;

    println(" -> " + strResult) ;
    if (!strResult.equals(paStrExpected)) {
      throw new RuntimeException("expected was " + paStrExpected) ;
    }
  }// end method

  /**
   *
   */
  def formatText(paStr:String , paIntWidth:Int) = {
    val retStrBuff = new StringBuffer();
    val strTok = new StringTokenizer(paStr , "\n") ;

    while (strTok.hasMoreElements()) {
      retStrBuff.append(formatLine(strTok.nextToken().trim() , paIntWidth) + "\n");
    }

    retStrBuff.toString();
  }// end method

  /**
   *
   */
  def formatLine(paStr:String , paIntWidth:Int) = {
    val retStrBuff = new StringBuffer();
    var lineStrBuff = new StringBuffer();
    val strTok = new StringTokenizer(paStr , " ") ;

    while (strTok.hasMoreElements()) {
      val strArrWord = spellWord(strTok.nextToken().trim()) ;
      var first = true
      strArrWord.foreach ( word => {

          if ((lineStrBuff.length() + word.length()) > paIntWidth) {
            // Zeile ist voll
            // in nächste Zeile gehen
            retStrBuff.append(lineStrBuff);
            if (!first) {
              retStrBuff.append("-");
            }
            retStrBuff.append("\n");
            lineStrBuff = new StringBuffer();
          }
          lineStrBuff.append(word);
          first = false
        }
      )
      lineStrBuff.append(" "); // Leerzeichen zum Trennen des nächsten Wortes
    }

    retStrBuff.append(lineStrBuff + "\n");
    retStrBuff.toString();
  }// end method

  /**
   * Liste mit bekannten Worten als Ausnahmen für die ng-Trennregel Worten
   * Achtung, alles klein schreiben
   */
  private val knownWordHashSet = newKnownWordHashSet() ;

  /**
   * Initialisierungsmethode für knownWordHashSet
   */
  private def newKnownWordHashSet() = {
    val newHashSet = new HashSet[String]();

    newHashSet.add("seiten") ;
    newHashSet.add("autoren") ;
    newHashSet.add("gesamt") ;
    newHashSet;
  }// end method

  /**
   * HashMap mit bereits getrennten Worten
   */
  private val wordHashMap = new HashMap[String,Array[String]]();

  /**
   * HashSet mit bekannten Silben
   */
  private val syllHashSet = newSyllHashSet();

  /**
   * Initialisierungsmethode für syllHashSet
   */
  private def newSyllHashSet() = {
    val newHashSet = new HashSet[String]();

    newHashSet.add("an") ;
    newHashSet.add("trag") ;
    newHashSet.add("schäfts") ;
    newHashSet.add("chen") ;
    newHashSet.add("gen") ;

    newHashSet.add("dung") ;
    newHashSet.add("nung") ;
    newHashSet.add("rung") ;
    newHashSet.add("tung") ;

    newHashSet.add("dungs") ;
    newHashSet.add("nungs") ;
    newHashSet.add("rungs") ;
    newHashSet.add("tungs") ;

    newHashSet.add("ent") ;
    newHashSet.add("auf") ;
    newHashSet.add("trag") ;

    newHashSet.add("lis") ;
    newHashSet.add("in") ;
    newHashSet.add("tion") ;
    newHashSet.add("grund") ;
    newHashSet.add("gründ") ;
    newHashSet.add("änderung") ;
    newHashSet.add("be") ;
    newHashSet.add("ver") ;
    newHashSet.add("lauf") ;
    newHashSet.add("schreib") ;
    newHashSet.add("ngen") ;
    newHashSet.add("ten") ;
    newHashSet.add("treten") ;
    newHashSet.add("mit") ;
    newHashSet.add("pro") ;
    newHashSet.add("jekt") ;
    newHashSet.add("re") ;
    newHashSet.add("agieren") ;
    newHashSet.add("arbeit") ;
    newHashSet.add("schaft") ;
    newHashSet.add("schaf") ;
    newHashSet.add("tränke") ;
    newHashSet.add("gesamt") ;
    newHashSet.add("samt") ;
    newHashSet.add("autor") ;
    newHashSet.add("rungs") ;
    newHashSet.add("text") ;
    newHashSet;

  }// end method

  /**
   * erzeugt ein String-Array mit den Silben des Wortes
   */
  def spellWord(paStrWord1: String):Array[String] = {
    var retStrArr:Array[String] = null ;

    retStrArr = wordHashMap.get(paStrWord1);
    if (retStrArr != null) {
      return retStrArr;
    }
    val arrList = new scala.collection.mutable.ArrayBuffer[String]();

    var paStrWord2 = paStrWord1.trim();

    var strPreSyllable = "" ;
    var strPreSyllables = "" ;

    while (paStrWord2.length() > 0) {
      //println("-----------------------------------------------------");
      val syllable = parseSyllable(paStrWord2,strPreSyllable,strPreSyllables) ;

      // System.out.println( "silbe:"+silbe);

      paStrWord2 = paStrWord2.substring(syllable.length()) ;
      arrList + syllable
      strPreSyllable = syllable ;
      strPreSyllables += syllable ;
    }// while

    retStrArr = arrList.toArray ;

    wordHashMap.put(paStrWord2 , retStrArr) ;

    return retStrArr;
  }// end method

  /**
   * Zurückliefern der ersten Silbe aus dem übergebenen Wort
   */
  private def parseSyllable( paStr:String, paStrPreSyllable:String, paStrPreSyllables:String):String = {
    var i:Int = 0 ;
    var bCkToKK = false ;

    var bCont = true ;

    while (bCont) {
      bCont = false;

      var bMatch = false;

      // Über beginnende Konsonanten laufen
      while (!bMatch && i < paStr.length() && isConsonant(paStr.charAt(i))) {
        bMatch = checkKnownSyllable(paStr.substring(0 , Math.min(i , paStr.length())) ,
                                    paStr.substring(Math.min(i , paStr.length()))) ;
        i+=1;
      }
      // Über Vocale laufen
      while (!bMatch && i < paStr.length() && isVocal(paStr.charAt(i)) ) {
        bMatch = checkKnownSyllable(paStr.substring(0 , Math.min(i , paStr.length())) ,
                                    paStr.substring(Math.min(i , paStr.length())));
        i+=1;
      }

      // Über endende Konsonanten laufen
      while (!bMatch && (i < (paStr.length() - 1)) && isConsonant(paStr.charAt(i + 1))) {
        bMatch = checkKnownSyllable(paStr.substring(0 , Math.min(i , paStr.length())) ,
                                    paStr.substring(Math.min(i , paStr.length())));
        i+=1;
      }

//      println("vor den Regeln: " + paStr.substring(0 , Math.min(i , paStr.length())) + "|"
//              + paStr.substring(Math.min(i , paStr.length()))) ;

      // Starter der else-if-Kaskade,  damit else if beliebig verschoben werden kann ohne Risiko Verwechslung if <--> else if
      if (bMatch) {
        i-=1;
      }

      // // bekanntes Wort wurde getrennt
      // else if (contentsKnownWord(paStrPreSyllables.toLowerCase() , // + // "?" +
      // // paStr.substring(0,Math.min(i , paStr.length())).toLowerCase())
      // paStr.substring(0,Math.min(i , paStr.length())).toLowerCase())
      // // paStr.toLowerCase() )
      // ) {
      // System.out.println("bekanntes Wort wurde getrennt " + paStr.substring(Math.min(i , paStr.length()))) ;
      // // ???
      // }

      /*
       // Spezialregel Silbe mit
       // Mi-tarbeiter wird zu Mit-arbeiter
       else if ((i > 1 && (i < paStr.length() - 2) && paStr.substring(i - 2).toLowerCase().startsWith("mi")) // ...
       && (i < paStr.length() - 1) && paStr.substring(i).startsWith("t")) {
       System.out.println("Spezialregel Silbe mit " + paStr.substring(i)) ;
       i++;
       }
       */

      /*
       // Spezialregel Silbe samt
       // Gesam-twerk wird zu Gesamt-werk
       else if ((i > 2 && (i < paStr.length() - 3) && paStr.substring(i - 3).toLowerCase().startsWith("sam")) // ...
       && (i < paStr.length() - 1) && paStr.substring(i).startsWith("t")) {
       System.out.println("Spezialregel Silbe samt " + paStr.substring(i)) ;
       i++;
       }
       */

      // Trennung zweier bekannter Silben aufgetreten
      else if (checkKnownSyllable(paStr.substring(0 , Math.min(i , paStr.length())) ,
                                  paStr.substring(Math.min(i , paStr.length())))) {
        //println("Trennung zweier bekannter Silben aufgetreten " + paStr.substring(i)) ;
      }

      // Trennung zweier bekannter Silben an Folgeposition 1 aufgetreten
      else if (checkKnownSyllable(paStr.substring(0 , Math.min(i + 1 , paStr.length())) ,
                                  paStr.substring(Math.min(i + 1, paStr.length())))) {
        //println("Trennung zweier bekannter Silben an Folgeposition 1 aufgetreten " + paStr.substring(i)) ;
        i += 1 ;
      }

      // Trennung zweier bekannter Silben an Folgeposition 2 aufgetreten
      else if (checkKnownSyllable(paStr.substring(0 , Math.min(i + 2 , paStr.length())) ,
                                  paStr.substring(Math.min(i + 2, paStr.length())))) {
        //println("Trennung zweier bekannter Silben an Folgeposition 2 aufgetreten " + paStr.substring(i)) ;
        i += 2 ;
      }

      /*
       // Trennung zweier bekannter Silben an Folgeposition 3 aufgetreten
       else if (checkKnownSyllable(paStr.substring(0 , Math.min(i + 3 , paStr.length())) ,
       paStr.substring(Math.min(i + 3, paStr.length())))) {
       System.out.println("Trennung zweier bekannter Silben an Folgeposition 3 aufgetreten " + paStr.substring(i)) ;
       i += 3 ;
       }
       */

      // st wurde getrennt
      else if (i > 1 && (i < paStr.length() - 1) && paStr.substring(i - 1).startsWith("st")) {
        // println("st wurde getrennt " + paStr.substring(i)) ;
        i-=1;
      }

      // st am Ende der Silbe
      else if ((i < paStr.length() - 1) && paStr.substring(i).startsWith("st")) {
        //println("st am Ende der Silbe " + paStr.substring(i)) ;
        i += 2;
      }

      // ft am Ende der Silbe
      else if ((i < paStr.length() - 1) && paStr.substring(i).startsWith("ft")) {
        //println("ft am Ende der Silbe " + paStr.substring(i)) ;
        i += 2;
      }

      // sch wurde getrennt
      else if (i > 2 && (i < paStr.length() - 1) && paStr.substring(i - 2).startsWith("sch")) {
        //println("sch wurde getrennt " + paStr.substring(i)) ;
        i -= 2;
      }

      // ch wurde getrennt
      else if (i > 1 && (i < paStr.length() - 1) && paStr.substring(i - 1).startsWith("ch")) {
        //println("ch wurde getrennt " + paStr.substring(i)) ;
        i-=1;
      }

      // str am Anfang der nächsten Silbe
      else if (i > 2 && (i < paStr.length() - 1) && paStr.substring(i - 2).startsWith("str")) {
        //println("str am Anfang der nächsten Silbe " + paStr.substring(i)) ;
        i -= 2;
      }

      // ck: c am Ende der Silbe und k am Anfang der nächsten Silbe
      // ck wird in kk umgewandelt
      else if ((i > 1 && (i < paStr.length() - 1) && paStr.substring(i - 1).startsWith("c")) // ...
               && (i < paStr.length() - 1) && paStr.substring(i).startsWith("k")) {
        //println("ck wird in kk umgewandelt " + paStr.substring(i)) ;
        bCkToKK = true ;
      }

      // Spezialregel Silbe ng
      // ng wird nicht getrennt
      else if ((i > 1 && (i < paStr.length() - 1) && paStr.substring(i - 1).toLowerCase().startsWith("n")) // ...
               && (i < paStr.length() - 1) && paStr.substring(i).startsWith("g")// ...
               // && ( ! ( paStrPreSyllable.toLowerCase() + paStr.toLowerCase() ).startsWith( "seiten" ) ) // seiten-gestaltung als Ausnahme
               // && (!startsWithKnownWord(paStrPreSyllables.toLowerCase() + paStr.toLowerCase())) // Suchen nach Ausnahmen für ng-Trennung (seitengestaltung, autorengemeinschaft)
               // && (!startsWithKnownWord(paStrPreSyllables.toLowerCase() + paStr.substring(0,i).toLowerCase())) // Suchen nach Ausnahmen für ng-Trennung (seitengestaltung, autorengemeinschaft)
               // && (!endsWithKnownWord(paStrPreSyllables.toLowerCase() )) // Suchen nach Ausnahmen für ng-Trennung (seitengestaltung, autorengemeinschaft)
               // && (!endsWithKnownWord(paStrPreSyllables.toLowerCase() + paStr.toLowerCase())) // Suchen nach Ausnahmen für ng-Trennung (seitengestaltung, autorengemeinschaft)
               && (!endsWithKnownWord(paStrPreSyllables.toLowerCase() + paStr.substring(0,i).toLowerCase())) // Suchen nach Ausnahmen für ng-Trennung (seitengestaltung, autorengemeinschaft)
      ) {
        //println("ng wird nicht getrennt " + paStr.substring(i)) ;
        i+=1;
        bCont = true;
        
      }

      // ein Vocal folgt sofort nach dem endenden Konsonant
      // der Konsonant soll zur nächsten Silbe gehören
      else if (i > 0 && (i < paStr.length() - 1) && isVocal(paStr.charAt(i + 1))) {
        //println("ein Vocal folgt sofort nach dem endenden Konsonant " + paStr.substring(i)) ;
        ;
      }

      // Ende der Silbe mit Konsonant
      else if (i < paStr.length() && isConsonant(paStr.charAt(i))) {
        //println("Ende der Silbe mit Konsonant " + paStr.substring(i)) ;
        i+=1;
      }

      // es folgen nur noch Konsonanten bis zum Ende des Wortes
      else if (isAllConsonantes(paStr.substring(i))) {
        //println("es folgen nur noch Konsonanten bis zum Ende des Wortes " + paStr.substring(i)) ;
        i = paStr.length();
      }
      // keine Regel hat gezogen
      else {
        //println("keine Regel hat gezogen") ;
      }

    }// while

    var retStr = paStr.substring(0 , i) ;

    if (bCkToKK) {
      retStr = retStr.substring(0 , retStr.length() - 1) + "k" ;
    }

    return retStr ;
  }// end method

  /**
   * stimmlos
   */
  def isConsonant(paCh:Char):Boolean = {
    return !isVocal(paCh) ;
  }// end method

  /**
   * Prüfung, ob alle Zeichen des Wortes Konsonanten sind
   */
  def isAllConsonantes(paStr:String):Boolean= {
    paStr.foreach(a => {
        if (isVocal(a)) {
          return false ;
        }
      }
    )
    return true ;

  }// end method

  /**
   * stimmhaft
   */
  def isVocal(paCh:Char):Boolean = {
    return (
      paCh == 'e' || // ...
      paCh == 'a' || // ...
      paCh == 'o' || // ...
      paCh == 'u' || // ...
      paCh == 'i' || // ...
      paCh == 'ä' || // ...
      paCh == 'ö' || // ...
      paCh == 'ü' || // ...

      paCh == 'E' || // ...
      paCh == 'A' || // ...
      paCh == 'O' || // ...
      paCh == 'U' || // ...
      paCh == 'I' || // ...
      paCh == 'Ä' || // ...
      paCh == 'Ö' || // ...
      paCh == 'Ü') ;
  }// end method

  private def startsWithKnownWord( paStr1:String) :Boolean = {
    //println("startsWithKnownWord:" + paStr1) ;
    var paStr2 = paStr1.toLowerCase() ;
    val iter = knownWordHashSet.iterator();

    while (iter.hasNext()) {
      if (paStr2.startsWith(iter.next())) {
        return true;
      }
    }
    return false;
  }// end method

  private def endsWithKnownWord(paStr1:String):Boolean = {
    //println("endsWithKnownWord:" + paStr1) ;
    var paStr2 = paStr1.toLowerCase() ;
    val iter = knownWordHashSet.iterator();

    while (iter.hasNext()) {
      if (paStr2.endsWith(iter.next())) {
        return true;
      }
    }
    return false;
  }// end method

  private def contentsKnownWord( paStr1:String):Boolean = {
    //println("contentsKnownWord:" + paStr1) ;
    var paStr2 = paStr1.toLowerCase() ;
    val iter = knownWordHashSet.iterator();

    while (iter.hasNext()) {
      if (paStr2.indexOf(iter.next()) > -1) {
        return true;
      }
    }
    return false;
  }// end method

  private def contentsKnownWord( paStrFirst:String, paStrNext:String):Boolean = {
    //println("contentsKnownWord:" + paStrFirst + "?" + paStrNext) ;
    val paStr = paStrFirst + paStrNext ;

    val iter = knownWordHashSet.iterator();

    while (iter.hasNext()) {
      val strKnownWord = iter.next() ;

      if ((!paStrFirst.endsWith(strKnownWord)) && paStr.indexOf(strKnownWord) > -1) {
        return true;
      }
    }
    return false;
  }// end method

  /**
   * Silben-String-Array zusammenfügen
   */
  private def arr2Str(paStrArr:Array[String] , paDelim:String):String = {
    val sb = paStrArr.foldLeft(new StringBuffer())((b:StringBuffer,a:String) =>
      (if (b.length == 0)  b else b.append(paDelim)).append(a)
    )
    return sb.toString();
  }// end method

  private def checkKnownSyllable( paStrLast: String , paStrNext: String): Boolean = {
    //println(paStrLast + "|" + paStrNext) ;
    if (endsWithKnownSyllable(paStrLast.toLowerCase()) && startsWithKnownSyllable(paStrNext)) {
      //println("!!!");
      return true;
    }
    return false;

  }// end method

  private def endsWithKnownSyllable( paStr:String): Boolean = {
    val iter = syllHashSet.iterator();

    while (iter.hasNext()) {
      if (paStr.endsWith(iter.next())) {
        return true;
      }
    }
    return false;
  }// end method

  private def startsWithKnownSyllable(paStr:String):Boolean = {
    val iter = syllHashSet.iterator();

    while (iter.hasNext()) {
      if (paStr.startsWith(iter.next())) {
        return true;
      }
    }
    return false;
  }// end method
}
