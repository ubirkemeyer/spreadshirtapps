<%@ page import="net.sprd.groovy.amazonas.AmazonPrintType" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonPrintType.label', default: 'AmazonPrintType')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
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

        <g:sortableColumn property="id" title="${message(code: 'amazonPrintType.id.label', default: 'Id')}"/>

        <g:sortableColumn property="spreadshirtPrintTypeId" title="${message(code: 'amazonPrintType.spreadshirtPrintTypeId.label', default: 'Spreadshirt Print Type Id')}"/>

        <g:sortableColumn property="additionalDescription" title="${message(code: 'amazonPrintType.additionalDescription.label', default: 'Additional Description')}"/>

      </tr>
      </thead>
      <tbody>
      <g:each in="${amazonPrintTypeInstanceList}" status="i" var="amazonPrintTypeInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show" id="${amazonPrintTypeInstance.id}">${fieldValue(bean: amazonPrintTypeInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: amazonPrintTypeInstance, field: "spreadshirtPrintTypeId")}</td>

          <td>${fieldValue(bean: amazonPrintTypeInstance, field: "additionalDescription")}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
  <div class="paginateButtons">
    <g:paginate total="${amazonPrintTypeInstanceTotal}"/>
  </div>
</div>
</body>
</html>
