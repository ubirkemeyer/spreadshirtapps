<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Basket" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    Basket basket = (Basket) request.getAttribute("basket");
    if (basket.getNoItems().equals("0")) {
%>
Shopping&nbsp;Basket&nbsp;is&nbsp;empty
<%
} else {
%>
<a href="/samplebrowser/basket">Shopping Basket</a>&nbsp;<%=basket.getNoItems()%>&nbsp;items&nbsp;<%=basket.getTotal()%>&nbsp;<%=basket.getCurrencySymbol()%>
<%
    }
%>