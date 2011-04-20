package net.sprd.api.model.inventory

import net.sprd.api._
import scala.collection._

class Appearance(
    id: String,
    val name: String,
    _colors: => Map[String,String],
    _printTypes: => Set[String]
    ) extends ReadOnlyEntity(id)  {
    
    lazy val colors = _colors
    lazy val printTypes = _printTypes
}

object Appearance{
    def apply(data: => scala.xml.Node) =
        new Appearance((data \ "@id").text,(data \ "name").text,immutable.Map.empty ++ ((data \ "colors" \ "color") map (a => ((a\"@index").text -> a.text))), immutable.Set.empty ++ ((data \ "printTypes" \ "printType") map (a => (a\"@id").text )));
}
