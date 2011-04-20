package net.sprd.api.model.inventory

import java.awt.geom.Rectangle2D
 
import net.sprd.api._

class PrintArea(val productType : ProductType, data: => scala.xml.Node) extends ReadOnlyEntity(data)  {

    lazy val size = ((data \ "boundary" \ "size"\"width").text.toDouble,
            (data\ "boundary" \ "size"\"height").text.toDouble)
    lazy val excludedPrintTypeIds = List.empty ++ (data \ "restrictions"\ "excludedPrintTypes" \ "excludedPrintType" \ "@id") map (a => a.text)
 
}
