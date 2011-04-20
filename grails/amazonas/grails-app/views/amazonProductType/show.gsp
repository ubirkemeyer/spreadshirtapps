<%@ page import="net.sprd.groovy.amazonas.AmazonProductType" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'amazonProductType.label', default: 'AmazonProductType')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <g:render template="/navigation"/>
</div>
<div class="nav">
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>

</div>
<div class="body">
  <h1><g:message code="amazon.producttype.properties"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="dialog">
    <table>
      <tbody>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.id.label" default="Id"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "id")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.spreadshirtProductTypeId.label" default="Spreadshirt Product Type Id"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "spreadshirtProductTypeId")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.browseNode.label" default="Browse Node"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "browseNode")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.careInstructions.label" default="Care Instructions"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "careInstructions")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.clothingType.label" default="Clothing Type"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "clothingType")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.collarType.label" default="Collar Type"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "collarType")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.department1.label" default="Department1"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "department1")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.department2.label" default="Department2"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "department2")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.department3.label" default="Department3"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "department3")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.innerMaterial.label" default="Inner Material"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "innerMaterial")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.materialComposition.label" default="Material Composition"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "materialComposition")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.name.label" default="Name"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "name")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.outerMaterial.label" default="Outer Material"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "outerMaterial")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.platinumKeywords1.label" default="Platinum Keywords1"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "platinumKeywords1")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.platinumKeywords2.label" default="Platinum Keywords2"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "platinumKeywords2")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.platinumKeywords3.label" default="Platinum Keywords3"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "platinumKeywords3")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.platinumKeywords4.label" default="Platinum Keywords4"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "platinumKeywords4")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.platinumKeywords5.label" default="Platinum Keywords5"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "platinumKeywords5")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.sizeModifier.label" default="Size Modifier"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "sizeModifier")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.sleeveType.label" default="Sleeve Type"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "sleeveType")}</td>

      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.styleKeyword.label" default="Style Keyword"/></td>

        <td valign="top" class="value">${fieldValue(bean: amazonProductTypeInstance, field: "styleKeyword")}</td>

      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="amazonProductType.sizeChart.label" default="Size Chart"/></td>

        <td valign="top" class="value">
          <g:if test="${amazonProductTypeInstance.sizeChart && new File(amazonProductTypeInstance.sizeChart).canRead()}">
            <a href="<g:createLink action="sizeChart" id="${amazonProductTypeInstance.id}" controller="export" absolute="true"></g:createLink>">
              <g:createLink action="sizeChart" id="${amazonProductTypeInstance.id}" controller="export" absolute="true"/>
            </a>

          </g:if>
          <g:else>
            No size chart available
          </g:else>
        </td>

      </tr>

      </tbody>
    </table>
    <h1><g:message code="spreadshirt.producttype.properties"/></h1>
    <g:if test="${spreadshirtProductTypeInstance == null}">
      n/a
    </g:if>
    <g:else>
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="spreadshirtProductType.name.label" default="Name"/></td>
          <td valign="top" class="value">${fieldValue(bean: spreadshirtProductTypeInstance, field: "name")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="spreadshirtProductType.description.label" default="Description"/></td>
          <td valign="top" class="value">${fieldValue(bean: spreadshirtProductTypeInstance, field: "description")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="spreadshirtProductType.brand.label" default="Brand"/></td>
          <td valign="top" class="value">${fieldValue(bean: spreadshirtProductTypeInstance, field: "brand")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="spreadshirtProductType.price.label" default="Price"/></td>
          <td valign="top" class="value">${fieldValue(bean: spreadshirtProductTypeInstance, field: "priceFormatted")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="spreadshirtProductType.image.label" default="Image"/></td>
          <td valign="top" class="value"><img src="${spreadshirtProductTypeImage}"/></td>

        </tr>

        </tbody>
      </table>
    </g:else>
  </div>
  <div class="buttons">
    <g:form>
      <g:hiddenField name="id" value="${amazonProductTypeInstance?.id}"/>
      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('Really delete spreadshirt product type ${spreadshirtProductTypeInstance?.id} (${spreadshirtProductTypeInstance?.name})?');"/></span>
    </g:form>
  </div>
</div>
</body>
</html>
