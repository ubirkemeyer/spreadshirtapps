package net.sprd.api

import net.sprd.api._
import net.sprd.api.model._
import scala.collection._


abstract class TimeEntityCache[B<:Entity](){
  
  val mapEntity = new mutable.HashMap[String, EntityReference[B]]();
  val mapTime = new mutable.HashMap[String, Long]();
  
  /**Load data for id */
  protected def load(id:String):B
  
  /**Get entity with ID from Cache*/
  def apply(id : String):B = {
    soft(id)()
  }

  def soft(id : String):EntityReference[B] = {
    if (internalContains(id)) {
      mapEntity(id)
    } else {
      val soft=
        if (!mapEntity.contains(id)) mapEntity.synchronized{
          if (!mapEntity.contains(id)) {
            val ref=new EntityReference[B](null.asInstanceOf[B])
            mapEntity+=(id -> ref)
            mapTime+=(id -> System.currentTimeMillis)
            ref
          } else mapEntity(id)
        } else mapEntity(id)
      soft.synchronized{
        if (soft.isEmpty) {
          try{
            val entity = load(id)
            soft.set(entity)
            mapTime+=(id -> System.currentTimeMillis);
            soft
          } catch {
            case e: Exception => null
          }
        } else soft
      }
    }
  }
  
  
  /**Internal check, if entity is already cached and young enough*/
  protected def internalContains(id:String) = 
    (mapEntity.contains(id) &&  mapEntity(id).isDefined && (mapTime(id) + ApiClient.maxTime > System.currentTimeMillis))
  
  /**get Entity if cached or add as new Entity from lazy entity parameter*/
  def applyOrAdd(id :String, entity: =>B) {
    if (internalContains(id)) {
      mapEntity(id)
    } else {
      mapEntity+=(id -> new EntityReference(entity))
      mapTime+=(id -> System.currentTimeMillis);
      entity
    }
  }
  
  /**add new Entity */
  def += (entity:B) {
    mapEntity+=(entity.id -> new EntityReference(entity))
    mapTime+=(entity.id -> System.currentTimeMillis);
  }
}

/** Singleton class with implicit conversion to Map
 */
object TimeEntityCache {
  implicit def timeEntityCache2Map[B<:Entity](a:TimeEntityCache[B]) = immutable.Map.empty ++ a.mapEntity
}



