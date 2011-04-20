package net.sprd.api

class ReadOnlyEntity(val id:String) extends Entity{
  def this(data: => scala.xml.Node) {
      this((data \ "@id").text)
  }
}
