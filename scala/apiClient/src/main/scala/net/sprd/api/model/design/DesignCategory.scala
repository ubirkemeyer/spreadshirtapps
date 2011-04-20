package net.sprd.api.model.design

import net.sprd.api._
import scala.collection._
import net.sprd.api.model._
import java.awt.geom.Rectangle2D

class DesignCategory (root: RootResource, data: => scala.xml.Node) extends ReadOnlyEntity(data) {
    
  lazy val name = (data \ "name").text
  lazy val categoryType = (data \ "@type").text
  lazy val weight = (data \ "@weight").text
  lazy val designCategories = immutable.Map.empty ++ ((data \ "designCategories"\ "designCategory") map (a => (a\"@id").text -> new DesignCategory(root,a)))
  lazy val designsUrl = (data \ "@{http://www.w3.org/1999/xlink}href").text
  lazy val designs = new EntityListCache[Design](designsUrl,"design", root.designs.entityCache)
}
