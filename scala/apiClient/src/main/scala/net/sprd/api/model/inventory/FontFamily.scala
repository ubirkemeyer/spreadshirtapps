package net.sprd.api.model.inventory

import net.sprd.api._
import net.sprd.api.model._
import scala.collection._

class FontFamily(root :RootResource, data: => scala.xml.Node, full:Boolean) extends ListEntity(data, full)  {
  lazy val name = (fullData \ "name").text

  lazy val printTypes = immutable.Set.empty ++ ((data \ "printTypes" \ "printType") map (a => (a\"@id").text ))
  
  lazy val fonts = immutable.Map.empty ++ ((fullData \ "fonts"\ "font") map (a => (a\"@id").text -> new Font(root, this, a)))
    
}



