package net.sprd.api.svg.model

class Image (
  var width: String,
  var height: String,
  var transform: String,
  var designId: String,
  var href: String,
  var printColorIds: String,
  var printColorRgbs: String) {
  
  def toXml() =
    <image width={width} height={height} transform={transform} designId={designId} printColorIds={printColorIds} printColorRgbs={printColorRgbs} xlink:href={href} />

}
