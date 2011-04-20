package net.sprd.groovy.amazonas

import java.nio.channels.FileChannel
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class AmazonProductTypeController {
  def spreadshirtImageService
  def spreadshirtInventoryService

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 100, 100)

    def amazonProductTypes = AmazonProductType.list(params)

    [
            amazonProductTypeInstanceList: amazonProductTypes,
            amazonProductTypeInstanceTotal: AmazonProductType.count(),
    ]
  }

  def create = {
    def amazonProductTypeInstance = new AmazonProductType()
    amazonProductTypeInstance.properties = params
    return [amazonProductTypeInstance: amazonProductTypeInstance]
  }

  def save = {
    def amazonProductTypeInstance = new AmazonProductType(params)
    if (amazonProductTypeInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), amazonProductTypeInstance.id])}"
      redirect(action: "show", id: amazonProductTypeInstance.id)
    }
    else {
      render(view: "create", model: [amazonProductTypeInstance: amazonProductTypeInstance])
    }
  }

  def show = {
    def amazonProductTypeInstance = AmazonProductType.get(params.id)
    if (!amazonProductTypeInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
      redirect(action: "list")
    }
    else {

      def spreadshirtProductTypeInstance = spreadshirtInventoryService.getProductType(
              grailsApplication.config.spreadshirt.amazonas.defaultShopId,
              amazonProductTypeInstance.spreadshirtProductTypeId
      )

      def thumbnailSize = grailsApplication.config.spreadshirt.amazonas.thumbnailSize

      def img = ''

      if (spreadshirtProductTypeInstance) {
        img = spreadshirtImageService.getProductTypeImageURL(spreadshirtProductTypeInstance).getURL(thumbnailSize)
      }
      [
              amazonProductTypeInstance: amazonProductTypeInstance,
              spreadshirtProductTypeInstance: spreadshirtProductTypeInstance,
              spreadshirtProductTypeImage: img
      ]
    }
  }

  def edit = {
    def amazonProductTypeInstance = AmazonProductType.get(params.id)
    if (!amazonProductTypeInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
      redirect(action: "list")
    }
    else {
      def spreadshirtProductTypeInstance = spreadshirtInventoryService.getProductType(
              grailsApplication.config.spreadshirt.amazonas.defaultShopId,
              amazonProductTypeInstance.spreadshirtProductTypeId
      )

      return [amazonProductTypeInstance: amazonProductTypeInstance, spreadshirtProductTypeInstance: spreadshirtProductTypeInstance]
    }
  }

  def update = {
    def amazonProductTypeInstance = AmazonProductType.get(params.id)
    if (amazonProductTypeInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (amazonProductTypeInstance.version > version) {

          amazonProductTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'amazonProductType.label', default: 'AmazonProductType')] as Object[], "Another user has updated this AmazonProductType while you were editing")
          render(view: "edit", model: [amazonProductTypeInstance: amazonProductTypeInstance])
          return
        }
      }
      def sizeChartTmp = amazonProductTypeInstance.sizeChart
      amazonProductTypeInstance.properties = params
      amazonProductTypeInstance.sizeChart = sizeChartTmp
      
      def file = request.getFile('sizeChart')

      def extension = GeneralIO.getExtension(file?.fileItem?.fileName)
      def outFile = new File("${grailsApplication.config.spreadshirt.amazonas.sizeChartPath}/${amazonProductTypeInstance.spreadshirtProductTypeId}.$extension")
      if (file.size) {
        GeneralIO.copy(file.inputStream, outFile)
        amazonProductTypeInstance.sizeChart = outFile.getAbsolutePath()
      }

      if (!amazonProductTypeInstance.hasErrors() && amazonProductTypeInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), amazonProductTypeInstance.id])}"
        redirect(action: "show", id: amazonProductTypeInstance.id)
      }
      else {
        render(view: "edit", model: [amazonProductTypeInstance: amazonProductTypeInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def amazonProductTypeInstance = AmazonProductType.get(params.id)
    if (amazonProductTypeInstance) {
      try {
        amazonProductTypeInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonProductType.label', default: 'AmazonProductType'), params.id])}"
      redirect(action: "list")
    }
  }


}
