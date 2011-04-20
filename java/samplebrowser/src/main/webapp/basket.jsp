<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Article" %>
<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Basket" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Basket</title>
</head>
<body>
<div style="width: 840px;">
    <%
        Basket basket = (Basket) request.getAttribute("basket");
        if (basket.getNoItems().equals("0")) {
    %>
    Shopping&nbsp;Basket&nbsp;is&nbsp;empty
    <%
    } else {
    %>

    <table border="0" cellspacing="0" cellpadding="5" width="100%">
        <tr>
            <th align="left">Name</th>
            <th>Appearance</th>
            <th>Size</th>
            <th align="right">Price</th>
            <th>Quantity</th>
            <th align="right">Total</th>
            <th>&nbsp;</th>
        </tr>
        <%
            for (Basket.BasketItem basketItem : basket.getBasketItems()) {
        %>
        <tr style="background-color: lightgray;">
            <td><%=basketItem.getArticle().getName()%>
            </td>
            <td align="center"><img
                    src="<%=basketItem.getAppearance().getImageUrl()%>"
                    alt="" border="1"/></td>
            <td align="center"><%=basketItem.getSize().getName()%>
            </td>
            <td align="right"><%=basketItem.getPrice()%>
                &nbsp;<%=basketItem.getCurrencySymbol()%>
            </td>
            <td align="center"><%=basketItem.getQuantity()%>
            </td>
            <td align="right"><%=basketItem.getTotal()%>
                &nbsp;<%=basketItem.getCurrencySymbol()%>
            </td>
            <td>
                <form action="basket" method="POST">
                    <input type="hidden" id="itemId" name="itemId" value="<%=basketItem.getId()%>"/>
                    <input type="submit" id="action" name="action"
                           value="delete"></form>
            </td>
        </tr>
        <tr>
            <td>
                <div style="float:left;">
                    <img src="<%=basketItem.getArticle().getPreviewImageUrl(basketItem.getAppearance().getId())%>"
                         alt="<%=basketItem.getArticle().getName()%>"
                         style="border: black dotted 1px;"/>
                </div>
                <div>
                    <%
                        for (int i = 0; i < basketItem.getArticle().getViews().size(); i++) {
                            Article.View view = basketItem.getArticle().getViews().get(i);
                            String floatString = (i + 1 % 2 == 0) ? "" : "float: left;";
                    %>
                    <div style="<%=floatString%> border: black dotted 1px; width:50px; height: 50px; margin:2px;">
                        <img id="viewid-<%=view.getId()%>"
                             src="<%=view.getPreviewImageUrl(basketItem.getAppearance().getId())%>"
                             alt="<%=view.getName()%>"/><br/>
                    </div>
                    <%
                        }
                    %>
                </div>
            </td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <%}%>
        <tr style="background-color: darkgray;">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td align="center"><b><%=basket.getNoItems()%>
            </b></td>
            <td align="right"><b><%=basket.getTotal()%>
                &nbsp;<%=basket.getCurrencySymbol()%>
            </b></td>
            <td>
                <form action="checkout" method="POST">
                    <input type="submit" value="Checkout"/>
                </form>
            </td>
        </tr>
    </table>
    <%
        }
    %>
</div>
</body>
</html>