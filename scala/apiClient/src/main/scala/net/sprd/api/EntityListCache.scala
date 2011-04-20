package net.sprd.api

import scala.collection._
import scala.xml.XML
import model._
import access._
import scala.ref.SoftReference

class EntityListCache[B<:Entity](baseUrl:String, tagName:String, var entityCache:XmlEntityCache[B]) {


  val buffer = new mutable.ArrayBuffer[EntityReference[B]]()
  
  private var _length:Option[Int] = None
  
  private var nextOffset = 0;

  private var time: Option[Long] = None

  protected def checkTime() {
    buffer.synchronized {
      if (time.isEmpty || (time.get + ApiClient.maxTime) < System.currentTimeMillis) {
        buffer.clear
        _length = None
        nextOffset = 0
        time = Some(System.currentTimeMillis)
      }
    }
  }
  
  def apply(id : String) = entityCache(id)
  
  def size():Int = {
    checkTime()
    _length match {
      case None => load(0,1,false)
      case Some(a) => a
    }
  }

  def head():B = head(false)

  def head(full:Boolean):B = {
    checkTime()
    slice(0,1,full).head
  }
  
  def slice(from:Int, until:Int, full:Boolean):mutable.ArrayBuffer[B] = buffer.synchronized {
    checkTime()
    if (nextOffset<until) {
      load(buffer.length, until, full)
    }
    buffer.slice(from, until).map(_())
  }
  
  protected def load(from:Int, until:Int, full:Boolean):Int = buffer.synchronized {
    val params = mutable.Map(("offset" -> nextOffset.toString),("limit" -> (until-nextOffset).toString));
    if (full) params += ("fullData" -> "true")

    val data = XML.load(HttpAccess.get(baseUrl, params))
    (data \ tagName) foreach (a =>
      try{
        += (entityCache.loadXml(a, full))
      } catch {
        case e: Exception =>;
      }
    )
    nextOffset = until
    _length = new Some((data \ "@count").text.toInt)
    _length.get
  }
  
  def += (entity:B) {
    entityCache.+=(entity)
    buffer += entityCache.soft(entity.id)
  }
}

object EntityListCache {
  implicit def entityListCache2Map[B<:Entity](cache: EntityListCache[B]) = immutable.Map.empty ++ cache.entityCache.mapEntity
}

