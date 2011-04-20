package net.sprd.api.creator

import net.sprd.api.model.design._
import net.sprd.api.model.inventory._
import net.sprd.api.model.common._

object PrintTypes {
  
  def possiblePrintTypes(design : Design, appearance: Appearance, printArea: PrintArea) = {
    design.printTypeIds.filter(a => appearance.printTypes.contains(a) && !printArea.excludedPrintTypeIds.contains(a))
  }

}
