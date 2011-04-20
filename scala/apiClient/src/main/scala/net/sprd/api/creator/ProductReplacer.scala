/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api.creator

import net.sprd.api.creator.text.TextConfigurationReplacer
import net.sprd.api.model.product.ConfigurationUpdate
import net.sprd.api.model.product.DesignConfigurationUpdate
import net.sprd.api.model.product.ProductInterface
import net.sprd.api.model.product.ProductUpdate
import net.sprd.api.model.product.TextConfigurationUpdate
import scala.collection._
import scala.xml.XML._
import scala.xml._

object ProductReplacer {
  
  def replaceTemplate(product:ProductInterface, texts: Array[String]) = {
    val newProduct = new ProductUpdate(product)

    newProduct.configurations = newProduct.configurations.map(
      replaceTemplateConfiguration(_, texts)
    )

//    def addConfig(map: mutable.Map[String, mutable.ArrayBuffer[ConfigurationUpdate]], configuration: ConfigurationUpdate) = {
//      if (!map.contains(configuration.printAreaId)) {
//        map + (configuration.printAreaId -> new mutable.ArrayBuffer[ConfigurationUpdate]())
//      }
//      map.get(configuration.printAreaId).get + configuration
//    }
//    val map = mutable.Map[String, mutable.ArrayBuffer[ConfigurationUpdate]]()
//    newProduct.configurations.foreach(c => addConfig(map, c))
//    map.foreach( (entry:(String,mutable.ArrayBuffer[ConfigurationUpdate])) =>{
//
//      })
//    
    newProduct
  }



  def replaceTemplateConfiguration(config: ConfigurationUpdate, texts: Array[String]) = {
    config match {
      case textConfig:TextConfigurationUpdate => replaceTemplateTextConfiguration(textConfig, texts)
      case designConfig:DesignConfigurationUpdate => replaceTemplateDesignConfiguration(designConfig, texts)
    }
  }

  def replaceTemplateTextConfiguration(config: TextConfigurationUpdate, texts: Array[String]) = {
    TextConfigurationReplacer.handleTextConfiguration(config, texts)
  }

  def replaceTemplateDesignConfiguration(config: DesignConfigurationUpdate, texts: Array[String]) = {
    config
  }
  

}
