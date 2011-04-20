/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sprd.api

import scala.ref.SoftReference

class EntityReference[B<:Entity](entity:B) {

  var _entityRef:SoftReference[B] = if (entity==null) null else new SoftReference(entity)

  def apply() = _entityRef()

  def set(entity:B) {
    _entityRef = new SoftReference(entity)
  }

  def isEmpty() = (_entityRef == null) || _entityRef.get.isEmpty

  def isDefined() = !isEmpty

}
