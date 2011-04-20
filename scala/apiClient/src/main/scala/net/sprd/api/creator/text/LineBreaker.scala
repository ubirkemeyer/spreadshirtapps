/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator.text
import net.sprd.api.model.inventory.Font
import scala.collection._

object LineBreaker {

  def breakWidth(font:Font, fontSize:Double,fullWidth: Double, words: Traversable[String]) = {
    var width = 0.0;
    var height = 0.0;//height of highest line
    
    def checkLine(size:(Double, Double, Double, Double), newLine:Boolean) {
      if (size._3 > width) width = size._3
      if (size._4 > height) height = size._4
    }

    def wordsplit(word:String, tail:List[String]) = {
      println("wordsplit:" + word)
      val syllabels = Spell.spellWord(word)
      var full = false;
      val (start, end) = syllabels.foldLeft(("",""))((a,b) => {
          if (!full) {
            val size = TextSize.size(font, fontSize)(a._1 + b + "-")
            if (size._3 > fullWidth) {
              full = true;
              (a._1 + "-", b)
            } else {
              (a._1 + b, "")
            }
          } else (a._1, a._2 + b)

        })
      checkLine(TextSize.size(font, fontSize)(start), true)
      end :: start :: tail
    }

    def subsplit(word:String, tail:List[String]):List[String] = {
      println("subsplit:" + word)
      var parts = TextSplitter.splitSubWords(word)
      var full = false;
      val (start, end) = parts.foldLeft(("",""))((a,b) => {
          if (!full) {
            val size = TextSize.size(font, fontSize)(a._1 + b.trim )
            if (size._3 > fullWidth) {
              full = true;
              (a._1.trim, b)
            } else {
              (a._1 + b, "")
            }
          }
          else {
            (a._1, a._2 + b)
          }
        })
      val size = TextSize.size(font, fontSize)(start)
      if (start.isEmpty || size._3 > fullWidth) {
        wordsplit(word, tail)
      } else {
        checkLine(TextSize.size(font, fontSize)(start), true)
        val size2 = TextSize.size(font, fontSize)(end)
        if (size2._3 > fullWidth) {
          subsplit(end, start :: tail)
        }
        checkLine(TextSize.size(font, fontSize)(end), true)
        end :: start :: tail
      }
    }
    
    val lines = words.foldLeft[List[String]](Nil)((a:List[String],b:String) =>
      if (a.isEmpty) {
        val size = TextSize.size(font, fontSize)(b)
        if (size._3 > fullWidth) {
          subsplit(b, Nil)
        } else {
          checkLine(size, true)
          b :: Nil
        }
      } else {
        val line = a.head + " " + b
        val size = TextSize.size(font, fontSize)(line)
        //println(line+" :"+size)
        if (size._3 > fullWidth) {
          if (a.head == "") {
            subsplit(b, a.tail)
          } else {
            val size2 = TextSize.size(font, fontSize)(b)
            if (size2._3 > fullWidth) {
              subsplit(b,a)
            } else {
              checkLine(TextSize.size(font, fontSize)(b), true)
              b :: a
            }
          }
        }
        else {
          checkLine(size, false)
          line :: a.tail
        }
      }
    )
    (lines.reverse,width, height)
  }

  

}
