<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Article" %>
<%@ page import="java.util.List" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Shop Articles</title>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/samplebrowser.js"></script>
    <script type="text/javascript">
        var sizes = new Object();
        <%
            List<Article> list = (List<Article>) request.getAttribute("articles");
            for (int i = 0; i < list.size(); i++) {
                Article article = list.get(i);
                %>
            sizes['<%=article.getId()%>'] = new Object();
                <%
                List<Article.Appearance> appearances = article.getAppearances();
                for (int j = 0; j < appearances.size(); j++) {
                    Article.Appearance appearance = appearances.get(j);
            %>
            sizes['<%=article.getId()%>']['<%=appearance.getId()%>'] = new Array();
            <%
                    List<Article.Size> sizes = article.getSizes();
                    int count = 0;
                    for (int k = 0; k < sizes.size(); k++) {
                        Article.Size size = sizes.get(k);
                        if (article.isAvailable(appearance.getId(), size.getId()))
                        {
        %>
        sizes['<%=article.getId()%>']['<%=appearance.getId()%>'][<%=count%>] = new Option("<%=size.getName()%>", <%=size.getId()%>);
        <%
                            count++;
                        }
                    }
                }
             }
        %>

        function changeAppearance(articleId, appearanceId) {
            document.getElementById('appid-' + articleId).value = appearanceId;
            var url = document.getElementById('imgid-' + articleId).src;
            url = url.substring(0, url.lastIndexOf('=') + 1) + appearanceId;
            document.getElementById('imgid-' + articleId).src = url;

            var select = document.getElementById('sizeid-' + articleId);
            select.options.length = 0;
            if (sizes[articleId][appearanceId].length == 0) {
                select.options[0] = new Option("No Size Available", -1);
                select.disabled = true;
                document.getElementById('buynow-' + articleId).disabled = true;
            } else {
                var i = 0;
                for (i = 0; i <= sizes[articleId][appearanceId].length; i++) {
                    select.options[i] = sizes[articleId][appearanceId][i];
                }
                select.disabled = false;
                document.getElementById('buynow-' + articleId).disabled = false;
            }

        }

        function switchToCompositionImage(articleId) {
            var appearanceId = document.getElementById('appid-' + articleId).value;
            var url = document.getElementById('compurl-' + articleId).value;
            url = url.substring(0, url.lastIndexOf('=') + 1) + appearanceId;
            document.getElementById('imgid-' + articleId).src = url;
        }

        function switchToProductImage(articleId) {
            var appearanceId = document.getElementById('appid-' + articleId).value;
            var url = document.getElementById('imgurl-' + articleId).value;
            url = url.substring(0, url.lastIndexOf('=') + 1) + appearanceId;
            document.getElementById('imgid-' + articleId).src = url;
        }
    </script>
</head>
<body onload="javascript:loadBasket()">
<div style="width: 840px;">
    <div id="basket">Loading ...</div>
    <%
        for (int i = 0; i < list.size(); i++) {
            Article article = list.get(i);
            String floatString = (i + 1 % 5 == 0) ? "" : "float: left;";
    %>
    <div style="<%=floatString%> width: 200px; height: 320px; margin: 5px;">
        <form action="./checkout" method="POST">
            <div>
                <a href="./articles/<%=article.getId()%>" style="border: 0px;">
                <img style="border: black dotted 1px;" id="imgid-<%=article.getId()%>" src="<%=article.getPreviewImageUrl()%>"
                     alt="<%=article.getName()%>"
                     onmouseover="javascript:switchToCompositionImage(<%=article.getId()%>)"
                     onmouseout="javascript:switchToProductImage(<%=article.getId()%>)" style="border: 0px;"/></a>
            </div>
            <div style="float: left; height: 55px;">Color:</div>
            <div style="text-align: right; margin: 2px; height: 65px; padding-left: 60px;">
                <%
                    List<Article.Appearance> appearances = article.getAppearances();
                    for (int j = 0; j < appearances.size(); j++) {
                        Article.Appearance appearance = appearances.get(j);
                %>
                <div style="margin: 2px; border: black solid 1px; float: left;">
                    <img src="<%=appearance.getImageUrl()%>"
                         alt="<%=appearance.getName()%>"
                         onclick="javascript:changeAppearance(<%=article.getId()%>, <%=appearance.getId()%>)"/>
                </div>
                <%
                    }
                %>
                <div>&nbsp;</div>
            </div>
            <div style="float: left;">Size:</div>
            <div style="text-align: right; margin: 2px;">
                <select id="sizeid-<%=article.getId()%>" name="sizeId">
                    <%
                        int count = 0;
                        List<Article.Size> sizes = article.getSizes();
                        for (int j = 0; j < sizes.size(); j++) {
                            Article.Size size = sizes.get(j);
                            if (article.isAvailable(article.getDefaultAppearanceId(), size.getId())) {
                                count++;
                    %>
                        <option value="<%=size.getId()%>" <%=size.getId().equals(article.getDefaultSizeId()) ? "selected" : ""%>><%=size.getName()%></option>
                    <%
                            }
                        }
                        if (count == 0) {
                    %>
                        <option value="-1">No Size Available</option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div style="float: left;"><%=article.getPrice()%> <%=article.getCurrencySymbol()%>
            </div>
            <div style="text-align: right; margin: 2px;">
                <input type="hidden" name="articleId" value="<%=article.getId()%>"/>
                <input id="appid-<%=article.getId()%>" type="hidden" name="appearanceId"
                       value="<%=article.getDefaultAppearanceId()%>"/>
                <input type="hidden" name="quantity"
                       value="1"/>
                <input id="compurl-<%=article.getId()%>" type="hidden" name="compurl"
                       value="<%=article.getPreviewCompositionUrl()%>"/>
                <input id="imgurl-<%=article.getId()%>" type="hidden" name="imgurl"
                       value="<%=article.getPreviewImageUrl()%>"/>
                <input id="buynow-<%=article.getId()%>" type="submit" name="buynow" value="Buy Now" <%=(count == 0) ? "disabled" : ""%>>
            </div>
        </form>
    </div>
    <%
        }
    %>
</div>
</body>
</html>