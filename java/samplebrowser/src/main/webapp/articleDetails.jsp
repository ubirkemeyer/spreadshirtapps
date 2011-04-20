<%@ page import="net.sprd.sampleapps.common.dataaccess.model.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sprd.sampleapps.common.dataaccess.model.PrintType" %>
<%@ page import="net.sprd.sampleapps.common.dataaccess.model.FontFamily" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Article Details</title>
    <script type="text/javascript" src="../js/prototype.js"></script>
    <script type="text/javascript" src="../js/samplebrowser.js"></script>
    <script type="text/javascript">
        var sizes = new Object();
        <%
            Article article = (Article)request.getAttribute("article");
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
        %>

        var views = new Array();
        <%
          List<Article.View> views = article.getViews();
            for (int j = 0; j < views.size(); j++) {
                Article.View view = views.get(j);
        %>
        views[<%=j%>] = new Object();
        views[<%=j%>]['id'] = '<%=view.getId()%>';
        views[<%=j%>]['imgurl'] = '<%=view.getImageUrl()%>';
        views[<%=j%>]['compurl'] = '<%=view.getCompositionUrl()%>';
        <%
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

            for (i = 0; i < views.length; i++) {
                var viewId = views[i]['id'];
                url = document.getElementById('viewid-' + viewId).src;
                url = url.substring(0, url.lastIndexOf('=') + 1) + appearanceId;
                document.getElementById('viewid-' + viewId).src = url;
            }
        }

        function changeView(articleId, viewId) {
            for (i = 0; i < views.length; i++) {
                if (views[i]['id'] == viewId) {
                    document.getElementById('imgurl-' + articleId).value = views[i]['imgurl'];
                    document.getElementById('compurl-' + articleId).value = views[i]['compurl'];
                    switchToProductImage(articleId);
                }
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
    <form action="../checkout" method="POST">
        <div style="width: 400px; float: left;">
            <div style="border: black dotted 1px; width:400px;">
                <img id="imgid-<%=article.getId()%>" src="<%=article.getImageUrl()%>"
                     alt="<%=article.getName()%>"
                     onmouseover="javascript:switchToCompositionImage(<%=article.getId()%>)"
                     onmouseout="javascript:switchToProductImage(<%=article.getId()%>)"/><br/>
            </div>
            <br/>
            <%
                for (int i = 0; i < article.getViews().size(); i++) {
                    Article.View view = article.getViews().get(i);
                    String floatString = (i + 1 % 4 == 0) ? "" : "float: left;";
            %>
            <div style="<%=floatString%> border: black dotted 1px; width:50px; height: 50px; margin:2px;">
                <img id="viewid-<%=view.getId()%>" src="<%=view.getPreviewImageUrl()%>"
                     alt="<%=view.getName()%>"
                     onclick="javascript:changeView(<%=article.getId()%>, <%=view.getId()%>)"/><br/>
            </div>
            <%
                }
            %>
        </div>
        <div style="padding-left: 410px;">
            <h2><%=article.getName()%>
            </h2>
            <%
                if (!article.getName().equals(article.getTypeName())) {
            %>
            <h3><%=article.getTypeName()%>
            </h3>
            <%
                }
            %>
            <div><%=article.getTypeDescription()%><br/></div>
            <div><b>Color:</b></div>
            <div style="margin: 2px;">
                <%
                    appearances = article.getAppearances();
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
            <div><b>Size:</b></div>
            <div style="margin: 2px;">
                <select id="sizeid-<%=article.getId()%>" name="sizeId">
                    <%
                        int count = 0;
                        List<Article.Size> sizes = article.getSizes();
                        for (int j = 0; j < sizes.size(); j++) {
                            Article.Size size = sizes.get(j);
                            if (article.isAvailable(article.getDefaultAppearanceId(), size.getId())) {
                                count++;
                    %>
                    <option value="<%=size.getId()%>" <%=size.getId().equals(article.getDefaultSizeId()) ? "selected" : ""%>><%=size.getName()%>
                    </option>
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
            <div><b>Quantity:</b></div>
            <div><input type="text" name="quantity" value="1" style="width: 30px;"/></div>
            <div><b>Article Number:</b></div>
            <div><%=article.getId()%>
            </div>
            <div><b>Price:</b></div>
            <div><%=article.getPrice()%> <%=article.getCurrencySymbol()%>
            </div>
            <div style="text-align: right; margin: 2px;">
                <input type="hidden" name="articleId" value="<%=article.getId()%>"/>
                <input id="appid-<%=article.getId()%>" type="hidden" name="appearanceId"
                       value="<%=article.getDefaultAppearanceId()%>"/>
                <input id="compurl-<%=article.getId()%>" type="hidden" name="compurl"
                       value="<%=article.getCompositionUrl()%>"/>
                <input id="imgurl-<%=article.getId()%>" type="hidden" name="imgurl"
                       value="<%=article.getImageUrl()%>"/>
                <input id="buynow-<%=article.getId()%>" type="submit" name="buynow"
                       value="Buy Now" <%=(count == 0) ? "disabled" : ""%>>
            </div>
        </div>
        <div style="clear: left;"><b>Configurations:</b></div>
        <div>
            <%for (Article.Configuration configuration : article.getConfigurations()) {%>
                <div style="clear: left;">
                    <div style="float: left; border: black dotted 1px; width:50px; height: 50px; margin:2px;">
                        <img src="<%=configuration.getImageUrl()%>" alt="<%=configuration.getId()%>"/>
                    </div>
                    <div>
                        <%=configuration.getPrintType().getName()%>, <%=configuration.getWidth()%>&nbsp;x&nbsp;<%=configuration.getHeight()%>&nbsp;mm
                        , <% for (PrintType.PrintColor printColor : configuration.getPrintColors()) { 
                            %>
                        <%=printColor.getName()%>
                        <%
                        } %>
                        <% if (configuration.getType().equals("design")) {%>
                            , <%=configuration.getDesignId()%>
                        <%} else {%>
                            , <%=configuration.getText()%>
                            ,
                            <%for (FontFamily fontFamily : configuration.getFontFamilies()) {%>
                              <%=fontFamily.getName()%>&nbsp;
                            <%}%>
                        <%}%>
                    </div>
                </div>
            <%}%>
        </div>
        <div  style="clear: left;">
            <div><b>Images:</b></div>
            <div><img src="<%=article.getTypePhotoUrl()%>"/></div>
        </div>
        <div>
            <div><b>Sizes:</b></div>
            <div><img src="<%=article.getTypeSizeUrl()%>"/></div>
        </div>
        <div>
            <div><b>Print Types:</b></div>
            <div>
                <table border="0" cellspacing="5">
                <%for (PrintType printType : article.getUsedPrintTypes()) {%>
                       <tr>
                           <td valign="top"><b><%=printType.getName()%></b></td>
                           <td><%=printType.getDescription()%></td>
                       </tr>
                <%}%>
                    </table>
            </div>
        </div>
    </form>
</div>
</div>
</body>
</html>