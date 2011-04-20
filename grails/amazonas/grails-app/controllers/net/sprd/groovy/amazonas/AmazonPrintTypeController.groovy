package net.sprd.groovy.amazonas

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class AmazonPrintTypeController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [amazonPrintTypeInstanceList: AmazonPrintType.list(params), amazonPrintTypeInstanceTotal: AmazonPrintType.count()]
  }

  def create = {
    def amazonPrintTypeInstance = new AmazonPrintType()
    amazonPrintTypeInstance.properties = params
    return [amazonPrintTypeInstance: amazonPrintTypeInstance]
  }

  def save = {
    def amazonPrintTypeInstance = new AmazonPrintType(params)
    if (amazonPrintTypeInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), amazonPrintTypeInstance.id])}"
      redirect(action: "show", id: amazonPrintTypeInstance.id)
    }
    else {
      render(view: "create", model: [amazonPrintTypeInstance: amazonPrintTypeInstance])
    }
  }

  def show = {
    def amazonPrintTypeInstance = AmazonPrintType.get(params.id)
    if (!amazonPrintTypeInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
      redirect(action: "list")
    }
    else {
      [amazonPrintTypeInstance: amazonPrintTypeInstance]
    }
  }

  def edit = {
    def amazonPrintTypeInstance = AmazonPrintType.get(params.id)
    if (!amazonPrintTypeInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [amazonPrintTypeInstance: amazonPrintTypeInstance]
    }
  }

  def update = {
    def amazonPrintTypeInstance = AmazonPrintType.get(params.id)
    if (amazonPrintTypeInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (amazonPrintTypeInstance.version > version) {

          amazonPrintTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'amazonPrintType.label', default: 'AmazonPrintType')] as Object[], "Another user has updated this AmazonPrintType while you were editing")
          render(view: "edit", model: [amazonPrintTypeInstance: amazonPrintTypeInstance])
          return
        }
      }
      amazonPrintTypeInstance.properties = params
      if (!amazonPrintTypeInstance.hasErrors() && amazonPrintTypeInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), amazonPrintTypeInstance.id])}"
        redirect(action: "show", id: amazonPrintTypeInstance.id)
      }
      else {
        render(view: "edit", model: [amazonPrintTypeInstance: amazonPrintTypeInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def amazonPrintTypeInstance = AmazonPrintType.get(params.id)
    if (amazonPrintTypeInstance) {
      try {
        amazonPrintTypeInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'amazonPrintType.label', default: 'AmazonPrintType'), params.id])}"
      redirect(action: "list")
    }
  }
}
