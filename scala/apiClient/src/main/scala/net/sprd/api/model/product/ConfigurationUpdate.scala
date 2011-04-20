package net.sprd.api.model.product

import net.sprd.api._
import net.sprd.api.model._

abstract class ConfigurationUpdate (val root :RootResource, var product:ProductUpdate,
    var configurationType: String,
    var offset :(Double,Double),
    var svg :scala.xml.NodeSeq,
    var printAreaId: String,
    var printTypeId:String,
    var printColorIdSet:Set[String],
    var printColorRGBSet:Set[String],
    var id : String = ""
  ) extends ConfigurationInterface with UpdateEntity {

  def this(config:ConfigurationInterface, product:ProductUpdate) {
    this(config.root, product, config.configurationType, config.offset,
         config.svg, config.printAreaId, config.printTypeId, config.printColorIdSet,
         config.printColorRGBSet,
         config.id)
  }
    
  def createXml = <configuration type={configurationType}>
    <printArea id={printAreaId}/>
    <printType id={printTypeId}/>
    <offset><x>{offset._1}</x><y>{offset._2}</y></offset>
    <content>{svg}</content>
    </configuration>
}

object ConfigurationUpdate {
  def apply(config:ConfigurationInterface, product:ProductUpdate): ConfigurationUpdate  = {
      config match {
      case designConfig:DesignConfigurationInterface => new DesignConfigurationUpdate(designConfig, product)
      case textConfig:TextConfigurationInterface => new TextConfigurationUpdate(textConfig, product)
      case _ => null
    }
  }
}
