package net.sprd.api

trait EntityCreate extends Entity {
  def createXml : scala.xml.NodeSeq
}

