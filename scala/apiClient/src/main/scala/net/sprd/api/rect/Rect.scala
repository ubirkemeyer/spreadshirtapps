/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.rect

object Rect {

  def union(rect1:Tuple4[Double, Double, Double, Double], rect2:Tuple4[Double, Double, Double, Double]) = {
    val minX = Math.min(rect1._1, rect2._1);
    val minY = Math.min(rect1._2, rect2._2);
    val maxX = Math.max(rect1._1+rect1._3, rect2._1+rect2._3);
    val maxY = Math.max(rect1._2+rect1._4, rect2._2+rect2._4);
    (minX, minY, (maxX-minX), (maxY-minY))
  }

  def transform(rect:Tuple4[Double, Double, Double, Double], f:Double=>Double) =
    (f(rect._1), f(rect._2), f(rect._3), f(rect._4))


}
