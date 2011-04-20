package net.sprd.groovy.amazonas

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import java.nio.charset.Charset
import org.apache.commons.io.IOUtils

class ExportController {
  def spreadshirtAPIService
  def converterService
  def springSecurityService
  public static final String MODE_PRICES = 'prices-stocks'

  def test = {
    def taglib = new ApplicationTagLib()
    def link = new ApplicationTagLib().createLink(controller: 'foo', action: 'bar', absolute: true)
    render link
  }

  private def getDownloadListing() {
    def exportDir = new File(grailsApplication.config.spreadshirt.amazonas.exportPath)
    def responseText = ""
    def decimalFormat = new DecimalFormat('###,###.##')

    def csvFilter = GeneralIO.getFileFilter('csv')
    def listing = []

    listing.addAll(exportDir.listFiles(csvFilter))

    if (getLoggedInUsername()) {
      def privateDir = new File("${grailsApplication.config.spreadshirt.amazonas.exportPath}/${getLoggedInUsername()}")
      if (privateDir.isDirectory()) {
        listing.addAll(privateDir.listFiles(csvFilter))
      }
    }

    listing = listing.sort().reverse()
    listing.each {file ->
      def x = decimalFormat.format(file.size() / 1024)
      def link = new ApplicationTagLib().createLink(
              controller: 'export',
              action: 'download',
              id: file.getName()
      )
      def deleteLink = new ApplicationTagLib().createLink(
              controller: 'export',
              action: 'delete',
              id: file.getName()
      )
      responseText += "<p><a href=\"$link\">${file.getName()}</a> ($x kbytes) "
      responseText += "<a href=\"$deleteLink\">(Delete)</a></p>"
    }
    return responseText
  }

  private def getLoggedInUsername() {
    def result = false
    if (springSecurityService.principal != 'anonymousUser') {
      result = springSecurityService.principal.username
    }
    return result
  }

  def index = {
    def model = [:]
    model.operationsAvailable = ['update', 'delete', MODE_PRICES]
    model.listing = getDownloadListing()
    model.version = grailsApplication.getMetadata().get('app.version')
    render view: 'index', 'model': model
  }

  def export = {
    def dateFormat = new SimpleDateFormat('yyyy-MM-dd_HH:mm:ss')
    def shopId = params.shopId
    def operation = params.operation
    def brand = params.brand
    def directoryName = "${grailsApplication.config.spreadshirt.amazonas.exportPath}"
    if (getLoggedInUsername()) {
      directoryName += "/${getLoggedInUsername()}"
      def directory = new File(directoryName)
      if (!directory.isDirectory()) {
        directory.mkdirs()
      }
    }
    def fileNameStub = "$directoryName/${dateFormat.format(new Date())}_export_shop_${shopId}_${operation}"

    converterService.exportAmazonProducts(shopId, fileNameStub, operation, brand)
    render getDownloadListing()
  }

  private def doDownload(def response, def file) {
    def result = false
    if (file.isFile() && file.canRead()) {
      response.setContentType("application/octet-stream")
      response.setHeader("Content-disposition", "filename=${file.getName()}")
      IOUtils.copy(new FileInputStream(file), response.outputStream)
      result = true
    }
    return result
  }

  def delete = {
    def requestedFileName = params.id?.replaceAll('%3A', ':')
    def file

    if (getLoggedInUsername()) {
      file = new File("${grailsApplication.config.spreadshirt.amazonas.exportPath}/${getLoggedInUsername()}/${requestedFileName}.csv")
      if (file.isFile()) {
        file.delete()
      }
    }

    file = new File("${grailsApplication.config.spreadshirt.amazonas.exportPath}/${requestedFileName}.csv")
    if (file.isFile()) {
      file.delete()
    }
    redirect controller: 'export', action: 'index'
  }

  def download = {
    def requestedFileName = params.id?.replaceAll('%3A', ':')
    def file

    if (getLoggedInUsername()) {
      file = new File("${grailsApplication.config.spreadshirt.amazonas.exportPath}/${getLoggedInUsername()}/${requestedFileName}.csv")
      if (doDownload(response, file)) {
        return
      }
    }

    file = new File("${grailsApplication.config.spreadshirt.amazonas.exportPath}/${requestedFileName}.csv")
    if (file.isFile() && file.canRead()) {
      if (doDownload(response, file)) {
        return
      }
    } else {
      response.sendError(404)
      return
    }
  }
  def sizeChart = {
    def amazonProductType = AmazonProductType.get(params.id)
    if (amazonProductType) {
      def file = new File(amazonProductType.sizeChart)
      if (file.isFile() && file.canRead()) {
        response.setContentType("image/png")
        IOUtils.copy(new FileInputStream(file), response.outputStream)
        return
      }
    }
    response.sendError(404)
  }
}
