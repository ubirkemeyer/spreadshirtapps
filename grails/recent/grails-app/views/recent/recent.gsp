<%--
  Created by IntelliJ IDEA.
  User: kab
  Date: 15.09.2010
  Time: 14:07:38
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html style="background-color:#${backgroundColor};color:${textColor}">
<head><title>Simple GSP page</title></head>
<body style="font-family:${fontFamily};font-size:${fontSize}">
<g:if test="${headline}"><div style="text-align:center"><h2>New at ${shopName} t-shirt shop:</h2></div></g:if>


<div style="text-align:center"><img src="${imageURL}" alt="${article.name}"/></div>
<div style="text-align:center">
  <g:if test="${name}">${article.name}</g:if>
  <g:if test="${name && price}">-</g:if>
  <g:if test="${price}">${article.priceFormatted}</g:if>
</div>
</body>
</html>