<%@ page import="net.sprd.groovy.amazonas.AmazonProductType" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonProductType.label', default: 'AmazonProductType')}"/>
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
  <g:hasErrors bean="${amazonProductTypeInstance}">
    <div class="errors">
      <g:renderErrors bean="${amazonProductTypeInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form method="post" enctype="multipart/form-data">
    <g:hiddenField name="id" value="${amazonProductTypeInstance?.id}"/>
    <g:hiddenField name="version" value="${amazonProductTypeInstance?.version}"/>
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="spreadshirtProductTypeId"><g:message code="amazonProductType.spreadshirtProductTypeId.label" default="Spreadshirt Product Type Id"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'spreadshirtProductTypeId', 'errors')}">
            <g:textField name="spreadshirtProductTypeId" value="${amazonProductTypeInstance?.spreadshirtProductTypeId}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="browseNode"><g:message code="amazonProductType.browseNode.label" default="Browse Node"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'browseNode', 'errors')}">
            <g:textField name="browseNode" value="${amazonProductTypeInstance?.browseNode}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="careInstructions"><g:message code="amazonProductType.careInstructions.label" default="Care Instructions"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'careInstructions', 'errors')}">
            <g:textArea name="careInstructions" value="${amazonProductTypeInstance?.careInstructions}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="clothingType"><g:message code="amazonProductType.clothingType.label" default="Clothing Type"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'clothingType', 'errors')}">
            <g:textField name="clothingType" value="${amazonProductTypeInstance?.clothingType}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="collarType"><g:message code="amazonProductType.collarType.label" default="Collar Type"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'collarType', 'errors')}">
            <g:textField name="collarType" value="${amazonProductTypeInstance?.collarType}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="department1"><g:message code="amazonProductType.department1.label" default="Department1"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'department1', 'errors')}">
            <g:textField name="department1" value="${amazonProductTypeInstance?.department1}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="department2"><g:message code="amazonProductType.department2.label" default="Department2"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'department2', 'errors')}">
            <g:textField name="department2" value="${amazonProductTypeInstance?.department2}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="department3"><g:message code="amazonProductType.department3.label" default="Department3"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'department3', 'errors')}">
            <g:textField name="department3" value="${amazonProductTypeInstance?.department3}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="innerMaterial"><g:message code="amazonProductType.innerMaterial.label" default="Inner Material"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'innerMaterial', 'errors')}">
            <g:textField name="innerMaterial" value="${amazonProductTypeInstance?.innerMaterial}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="materialComposition"><g:message code="amazonProductType.materialComposition.label" default="Material Composition"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'materialComposition', 'errors')}">
            <g:textField name="materialComposition" value="${amazonProductTypeInstance?.materialComposition}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="name"><g:message code="amazonProductType.name.label" default="Name"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'name', 'errors')}">
            <g:textField size="50" name="name" value="${amazonProductTypeInstance?.name}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="outerMaterial"><g:message code="amazonProductType.outerMaterial.label" default="Outer Material"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'outerMaterial', 'errors')}">
            <g:textField name="outerMaterial" value="${amazonProductTypeInstance?.outerMaterial}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="platinumKeywords1"><g:message code="amazonProductType.platinumKeywords1.label" default="Platinum Keywords1"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'platinumKeywords1', 'errors')}">
            <g:textField name="platinumKeywords1" value="${amazonProductTypeInstance?.platinumKeywords1}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="platinumKeywords2"><g:message code="amazonProductType.platinumKeywords2.label" default="Platinum Keywords2"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'platinumKeywords2', 'errors')}">
            <g:textField name="platinumKeywords2" value="${amazonProductTypeInstance?.platinumKeywords2}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="platinumKeywords3"><g:message code="amazonProductType.platinumKeywords3.label" default="Platinum Keywords3"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'platinumKeywords3', 'errors')}">
            <g:textField name="platinumKeywords3" value="${amazonProductTypeInstance?.platinumKeywords3}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="platinumKeywords4"><g:message code="amazonProductType.platinumKeywords4.label" default="Platinum Keywords4"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'platinumKeywords4', 'errors')}">
            <g:textField name="platinumKeywords4" value="${amazonProductTypeInstance?.platinumKeywords4}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="platinumKeywords5"><g:message code="amazonProductType.platinumKeywords5.label" default="Platinum Keywords5"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'platinumKeywords5', 'errors')}">
            <g:textField name="platinumKeywords5" value="${amazonProductTypeInstance?.platinumKeywords5}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="sizeModifier"><g:message code="amazonProductType.sizeModifier.label" default="Size Modifier"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'sizeModifier', 'errors')}">
            <g:textField name="sizeModifier" value="${amazonProductTypeInstance?.sizeModifier}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="sleeveType"><g:message code="amazonProductType.sleeveType.label" default="Sleeve Type"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'sleeveType', 'errors')}">
            <g:textField name="sleeveType" value="${amazonProductTypeInstance?.sleeveType}"/>
          </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="sleeveType"><g:message code="amazonProductType.styleKeyword.label" default="Style Keyword"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'styleKeyword', 'errors')}">
            <g:textField name="styleKeyword" value="${amazonProductTypeInstance?.styleKeyword}"/>
          </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="sizeChart"><g:message code="amazonProductType.sizeChart.label" default="Size Chart"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: amazonProductTypeInstance, field: 'sizeChart', 'errors')}">
            <input type="file" name="sizeChart" id="sizeChart"/>
          </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('Really delete spreadshirt product type ${spreadshirtProductTypeInstance?.id} (${spreadshirtProductTypeInstance?.name})?');"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
