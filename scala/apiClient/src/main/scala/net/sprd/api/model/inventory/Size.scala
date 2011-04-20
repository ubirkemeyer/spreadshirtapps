package net.sprd.api.model.inventory

import net.sprd.api._

class Size(data: => scala.xml.Node) extends ReadOnlyEntity(data)  {
    lazy val name = (data \ "name").text
}
