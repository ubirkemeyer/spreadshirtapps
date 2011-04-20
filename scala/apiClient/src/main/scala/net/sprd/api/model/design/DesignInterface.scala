package net.sprd.api.model.design

import scala.collection._
import net.sprd.api.model._
import net.sprd.api.model.inventory._
import net.sprd.api._

trait DesignInterface extends Entity {
  def root: RootResource
  def name: String
  def description: String
  def price: Double
}