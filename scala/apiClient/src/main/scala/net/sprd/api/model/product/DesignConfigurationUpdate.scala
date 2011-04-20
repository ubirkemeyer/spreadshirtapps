package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._

class DesignConfigurationUpdate (root :RootResource, product:ProductUpdate,
    offset :(Double,Double),
    svg :scala.xml.NodeSeq,
    printAreaId: String,
    printTypeId:String,
    var designId :String,
    var printColorIds:Array[String],
    var printColorRGBs: Array[String],
    id : String = ""
  ) extends ConfigurationUpdate(root, product, ConfigurationInterface.design, offset, svg, printAreaId,
  printTypeId, Set.empty ++ printColorIds, Set.empty ++ printColorRGBs, id) with DesignConfigurationInterface {

  def this(config:DesignConfigurationInterface, product:ProductUpdate) {
    this(config.root, product, config.offset,
         config.svg, config.printAreaId, config.printTypeId,
         config.designId, config.printColorIds, config.printColorRGBs,
         config.id)
  }
    
}
