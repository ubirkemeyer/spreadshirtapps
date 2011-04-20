<%@ page import="net.sprd.groovy.amazonas.AmazonColorMapping" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonColorMapping.label', default: 'AmazonColorMapping')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <g:render template="/navigation"/>
</div>
<div class="nav">
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.list.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>

        <g:sortableColumn property="id" title="${message(code: 'amazonColorMapping.id.label', default: 'Id')}"/>

        <g:sortableColumn property="spreadshirtColorId" title="${message(code: 'amazonColorMapping.spreadshirtColorId.label', default: 'Spreadshirt Color Id')}"/>

        <g:sortableColumn property="spreadshirtName" title="${message(code: 'amazonColorMapping.spreadshirtName.label', default: 'Spreadshirt Name')}"/>

        <g:sortableColumn property="amazonName" title="${message(code: 'amazonColorMapping.amazonName.label', default: 'Amazon Name')}"/>

      </tr>
      </thead>
      <tbody>
      <g:each in="${amazonColorMappingInstanceList}" status="i" var="amazonColorMappingInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show" id="${amazonColorMappingInstance.id}">${fieldValue(bean: amazonColorMappingInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: amazonColorMappingInstance, field: "spreadshirtColorId")}</td>

          <td>${fieldValue(bean: amazonColorMappingInstance, field: "spreadshirtName")}</td>

          <td>${fieldValue(bean: amazonColorMappingInstance, field: "amazonName")}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
  <div class="paginateButtons">
    <g:paginate total="${amazonColorMappingInstanceTotal}"/>
  </div>
</div>
</body>
</html>
