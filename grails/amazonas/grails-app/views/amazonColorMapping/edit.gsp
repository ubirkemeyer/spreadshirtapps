<%@ page import="net.sprd.groovy.amazonas.AmazonColorMapping" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping')}"/>
  <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <g:render template="/navigation"/>
</div>
<div class="nav">
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${amazonColorMappingInstance}">
    <div class="errors">
      <g:renderErrors bean="${amazonColorMappingInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form method="post">
    <g:hiddenField name="id" value="${amazonColorMappingInstance?.id}"/>
    <g:hiddenField name="version" value="${amazonColorMappingInstance?.version}"/>
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="spreadshirtColorId"><g:message code="amazonColorMapping.spreadshirtColorId.label" default="Spreadshirt Color Id"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonColorMappingInstance, field: 'spreadshirtColorId', 'errors')}">
            <g:textField name="spreadshirtColorId" value="${amazonColorMappingInstance?.spreadshirtColorId}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="spreadshirtName"><g:message code="amazonColorMapping.spreadshirtName.label" default="Spreadshirt Name"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonColorMappingInstance, field: 'spreadshirtName', 'errors')}">
            <g:textField name="spreadshirtName" value="${amazonColorMappingInstance?.spreadshirtName}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="amazonName"><g:message code="amazonColorMapping.amazonName.label" default="Amazon Name"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonColorMappingInstance, field: 'amazonName', 'errors')}">
            <g:textField name="amazonName" value="${amazonColorMappingInstance?.amazonName}"/>
          </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
