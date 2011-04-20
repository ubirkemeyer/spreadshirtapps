<%@ page import="net.sprd.groovy.amazonas.AmazonProductType" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonProductType.label', default: 'AmazonProductType')}"/>
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

        <g:sortableColumn style="width:80px" property="id" title="${message(code: 'amazonProductType.id.label', default: 'Id')}"/>

        <g:sortableColumn property="spreadshirtProductTypeId" title="${message(code: 'amazonProductType.spreadshirtProductTypeId.label', default: 'Spreadshirt Product Type Id')}"/>

        <g:sortableColumn property="name" title="${message(code: 'amazonProductType.name.label', default: 'Name')}"/>

        <g:sortableColumn property="department1" title="${message(code: 'amazonProductType.department1.label', default: 'Department 1')}"/>

        <g:sortableColumn property="department2" title="${message(code: 'amazonProductType.department2.label', default: 'Department 2')}"/>

        <g:sortableColumn property="department3" title="${message(code: 'amazonProductType.department3.label', default: 'Department 3')}"/>

      </tr>
      </thead>
      <tbody>
      <g:each in="${amazonProductTypeInstanceList}" status="i" var="amazonProductTypeInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show" id="${amazonProductTypeInstance.id}">${fieldValue(bean: amazonProductTypeInstance, field: "id")}</g:link></td>

          <td width="15%">
            ${fieldValue(bean: amazonProductTypeInstance, field: "spreadshirtProductTypeId")}
          </td>

          <td>${fieldValue(bean: amazonProductTypeInstance, field: "name")}</td>

          <td>${fieldValue(bean: amazonProductTypeInstance, field: "department1")}</td>

          <td>${fieldValue(bean: amazonProductTypeInstance, field: "department2")}</td>

          <td>${fieldValue(bean: amazonProductTypeInstance, field: "department3")}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
  <div class="paginateButtons">
    <g:paginate total="${amazonProductTypeInstanceTotal}"/>
  </div>
</div>
</body>
</html>
