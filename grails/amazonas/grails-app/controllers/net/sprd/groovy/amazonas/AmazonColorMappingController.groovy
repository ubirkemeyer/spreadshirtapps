package net.sprd.groovy.amazonas

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class AmazonColorMappingController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 100, 100)
    [amazonColorMappingInstanceList: AmazonColorMapping.list(params), amazonColorMappingInstanceTotal: AmazonColorMapping.count()]
  }

  def create = {
    def amazonColorMappingInstance = new AmazonColorMapping()
    amazonColorMappingInstance.properties = params
    return [amazonColorMappingInstance: amazonColorMappingInstance]
  }

  def save = {
    def amazonColorMappingInstance = new AmazonColorMapping(params)
    if (amazonColorMappingInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), amazonColorMappingInstance.id])}"
      redirect(action: "show", id: amazonColorMappingInstance.id)
    }
    else {
      render(view: "create", model: [amazonColorMappingInstance: amazonColorMappingInstance])
    }
  }

  def show = {
    def amazonColorMappingInstance = AmazonColorMapping.get(params.id)
    if (!amazonColorMappingInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
      redirect(action: "list")
    }
    else {
      [amazonColorMappingInstance: amazonColorMappingInstance]
    }
  }

  def edit = {
    def amazonColorMappingInstance = AmazonColorMapping.get(params.id)
    if (!amazonColorMappingInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [amazonColorMappingInstance: amazonColorMappingInstance]
    }
  }

  def update = {
    def amazonColorMappingInstance = AmazonColorMapping.get(params.id)
    if (amazonColorMappingInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (amazonColorMappingInstance.version > version) {

          amazonColorMappingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping')] as Object[], "Another user has updated this AmazonColorMapping while you were editing")
          render(view: "edit", model: [amazonColorMappingInstance: amazonColorMappingInstance])
          return
        }
      }
      amazonColorMappingInstance.properties = params
      if (!amazonColorMappingInstance.hasErrors() && amazonColorMappingInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), amazonColorMappingInstance.id])}"
        redirect(action: "show", id: amazonColorMappingInstance.id)
      }
      else {
        render(view: "edit", model: [amazonColorMappingInstance: amazonColorMappingInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def amazonColorMappingInstance = AmazonColorMapping.get(params.id)
    if (amazonColorMappingInstance) {
      try {
        amazonColorMappingInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping'), params.id])}"
      redirect(action: "list")
    }
  }
}
