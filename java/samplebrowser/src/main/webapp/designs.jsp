<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Design" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Design" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Design Browser Example</title>
</head>
<body>
<h1>Spreadshirt Design Browser Example</h1>

<div style="width: 840px;">
    <%
        List<Design> list = (List<Design>) request.getAttribute("designs");

        for (int i = 0; i < list.size(); i++) {
            Design design = list.get(i);
            String floatString = (i + 1 % 5 == 0) ? "" : "float: left;";
    %>
    <div style="<%=floatString%> width: 200px; height: 380px; margin: 5px;">
        <div style="border: black dotted 1px; height: 200px; width: 200px;">
            <img id="imgid-<%=design.getId()%>" src="<%=design.getImageUrl()%>"
                 alt="<%=design.getName()%>"/><br/>
        </div>
        <div style="float: left;"><b>Name:</b></div>
        <div style="text-align: right; margin: 2px;"><%=design.getName()%></div>
        <div style="float: left;"><b>Price:</b></div>
        <div style="text-align: right; margin: 2px;"><%=design.getPrice()%> <%=design.getCurrencySymbol()%></div>
        <div><b>Description:</b></div>
        <div><%=design.getDescription()%>...</div>
    </div>
    <%
        }
    %>
</div>
</body>
</html>