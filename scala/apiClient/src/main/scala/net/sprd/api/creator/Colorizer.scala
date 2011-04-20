package net.sprd.api.creator

import net.sprd.api.model.design._
import net.sprd.api.model.inventory._
import net.sprd.api.model.common._
import scala.collection._

object Colorizer {
  
  private object order extends Ordering[((Int,String),Int)] {
    def compare(v1:((Int,String),Int),v2:((Int,String),Int)) = 
      if (v1._2<v2._2) -1
      else if (v1._2>v2._2) 1
      else 0
  }
  
  def bestColors (printType: PrintType, defaultColors: Seq[String]): Seq[String] = {
    val printColors = Map.empty ++ printType.printColors map (a=> (a._1,a._2.fill))
    var diffs = Map.empty ++ (0 until defaultColors.size map (i => printColors map (pc => ((i, pc._1) -> rgbDistance(defaultColors(i), pc._2)))) flatten)
    var result = mutable.Map.empty[Int, String]
    0 until defaultColors.size foreach (_ => {
      val ((i,pc),dist) = diffs.min(order);
      diffs = diffs filter (p => p._1._1 != i && p._1._2 != pc)
      result += (i -> pc)
    })
    0 until defaultColors.size map ( i => result(i))
  }
     
  def rgbDistance(color1: String, color2: String) = {
    square(parse(color1.substring(0,2))-parse(color2.substring(0,2))) +
    square(parse(color1.substring(2,4))-parse(color2.substring(2,4))) +
    square(parse(color1.substring(4,6))-parse(color2.substring(4,6)))
  }
  
  private def square(value:Int) = {
    value*value
  }
  
  private def parse(hex2: String) = {
    value(hex2(0))*16+value(hex2(1))
  }
  
  private def value(ch: Char) = {
    ch match {
      case ch if (ch >= 'a') => ch - 'a' +10
      case ch if (ch  >= 'A') => ch - 'A' +10
      case _ => ch - '0'
    }
  }
}
