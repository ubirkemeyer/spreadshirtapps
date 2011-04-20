<%@ page import="com.socialnetworkshirts.twittershirts.Constants" %>
<%@ page import="com.socialnetworkshirts.twittershirts.data.model.ProductType" %>
<%@ page
        import="com.socialnetworkshirts.twittershirts.data.model.ProductTypeAppearance" %>
<%@ page import="com.socialnetworkshirts.twittershirts.data.model.ProductTypeSize" %>
<%@ page import="com.socialnetworkshirts.twittershirts.data.model.ProductTypeStockState" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String twitterUserId = (String) request.getAttribute(Constants.PARAM_TWITTER_USER_ID);
    ProductType productType = (ProductType) request.getAttribute(Constants.PARAM_PRODUCT_TYPE);
%>
<html>
<head>
<title>Twittershirt.xxx</title>
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript">
    var productTypePrice = <%=productType.getPrice()%>;
    var currencySymbol = '<%=productType.getCurrencySymbol()%>';
    var appearancePrintTypePrices = new Object();
    <%
        for (ProductTypeAppearance appearance : productType.getAppearances()) {
    %>
        appearancePrintTypePrices['<%=appearance.getId()%>'] = <%=appearance.getPrintType().getPrice().doubleValue()%>;
    <%
        }
    %>
    var appearanceSizes = new Object();
    <%
        for (ProductTypeStockState stockState : productType.getStockStates()) {
    %>
            if (appearanceSizes['<%=stockState.getAppearanceId()%>'] == null) 
                appearanceSizes['<%=stockState.getAppearanceId()%>'] = new Object();
            appearanceSizes['<%=stockState.getAppearanceId()%>']['<%=stockState.getSizeId()%>'] = <%=stockState.isAvailable()%>;
    <%
        }
    %>
    var sizes = new Array();
    <%
        int count = 0;
        for (ProductTypeSize size : productType.getSizes()) {
        %>
            sizes[<%=count%>] = '<%=size.getId()%>';
        <%
            count++;
        }
    %>
    var allowedSizes;
    var ApplicationController = {
        init: function() {
            Preferences.loadPreferences();
            ApplicationController.updateProductTypeAppearanceId($('productTypeAppearanceId').value);
            ApplicationController.updateProductTypeSizeId($('productTypeSizeId').value);
            $('tempUserId').value = $('twitterUserId').value;
            $('tempQuantity').value = $('quantity').value;
            ApplicationController.updateProduct();
        },
        updateProduct: function () {
            var twitterUserId = $('twitterUserId').value;
            var productTypeId = $('productTypeId').value;
            var productTypeAppearanceId = $('productTypeAppearanceId').value;
            var productTypeUrl = $('productTypeImageUrl').value;
            productTypeUrl = productTypeUrl.gsub('{productTypeAppearanceId}', productTypeAppearanceId);
            $('productTypeImage').src = productTypeUrl;

            $('loaderImage').removeClassName('invisible');
            $('textImage').addClassName('invisible');
            var textUrl = $('imageUrl').value;
            textUrl = textUrl.gsub('{twitterUserId}', twitterUserId);
            textUrl = textUrl.gsub('{productTypeId}', productTypeId);
            textUrl = textUrl.gsub('{productTypeAppearanceId}', productTypeAppearanceId);
            $('textImage').src = textUrl;
            new Ajax.Request(textUrl,
            {
                method:'get',
                asynchronous: false,
                onSuccess: function(transport) {
                    $('loaderImage').addClassName('invisible');
                    $('textImage').removeClassName('invisible');
                },
                onFailure: function() {

                }
            });
        },
        updateTwitterUserId: function() {
            $('twitterUserId').value = $('tempUserId').value;
            Preferences.storePreferences();
        },
        updateQuantity: function(id) {
            $('quantity').value = $('tempQuantity').value;
            Preferences.storePreferences();
        },
        updateProductTypeAppearanceId: function(id) {
            if ($('appearance_' + $('productTypeAppearanceId').value) != null) {
                $('appearance_' + $('productTypeAppearanceId').value).removeClassName('selected');
            }
            $('appearance_' + id).addClassName('selected');
            $('productTypeAppearanceId').value = id;

            var firstSize = null;
            allowedSizes = new Object();
            for (var i = 0; i <= sizes.length; i++) {                
                allowedSizes[sizes[i]] = appearanceSizes[id][sizes[i]];
                if ($('size_' + sizes[i]) != null) {
                    $('size_' + sizes[i]).removeClassName('disabled');
                    if (appearanceSizes[id][sizes[i]] == false)
                        $('size_' + sizes[i]).addClassName('disabled');
                    else {
                        if (firstSize == null)
                            firstSize = sizes[i];
                    }
                }
            }
            
            var price = '' + (productTypePrice +
                              appearancePrintTypePrices[id]) ;
            if (price.substring(price.indexOf('.') + 1, price.length).length == 1)
                price += 0;
            $('price').update(price + ' ' + currencySymbol);
            Preferences.storePreferences();

            if (allowedSizes[$('productTypeSizeId').value] == false) {
                ApplicationController.updateProductTypeSizeId(firstSize);   
            }
        },
        updateProductTypeSizeId: function(id) {
            if (allowedSizes[id] == true) {
                if ($('size_' + $('productTypeSizeId').value) != null) {
                    $('size_' + $('productTypeSizeId').value).removeClassName('selected');
                }
                $('size_' + id).addClassName('selected');
                $('productTypeSizeId').value = id;
                Preferences.storePreferences();
            }
        }
    };
    var Preferences = {
        loadPreferences: function () {
            Preferences.loadPreferenceForField('productTypeId', '<%=productType.getId()%>');
            Preferences.loadPreferenceForField('productTypeAppearanceId', '<%=productType.getAppearances().get(0).getId()%>');
            Preferences.loadPreferenceForField('twitterUserId', '<%=twitterUserId%>');
            Preferences.loadPreferenceForField('productTypeSizeId', '<%=productType.getSizes().get(0).getId()%>');
            Preferences.loadPreferenceForField('quantity', '1');
        },
        storePreferences: function () {
            Preferences.storePreferenceForField('productTypeId');
            Preferences.storePreferenceForField('productTypeAppearanceId');
            Preferences.storePreferenceForField('twitterUserId');
            Preferences.storePreferenceForField('productTypeSizeId');
            Preferences.storePreferenceForField('quantity');
        },
        getPreferenceForField: function (key, defaultValue) {
            var value = Cookies[key] == undefined ? defaultValue : Cookies[key];
            return value.replace(/(%3D)/g, '=');
        },
        loadPreferenceForField: function (key, defaultValue) {
            $(key).value = (Cookies[key] == undefined ? defaultValue : Cookies[key]);
        },
        loadPreferenceForCheckbox: function (key, defaultValue) {
            var value = Cookies[key] == undefined ? defaultValue : Cookies[key];
            $(key).checked = (value == "true" || value == "yes" || value == "1");
        },
        loadPreferenceOptions: function (key) {
            if (Cookies[key + 'Opt'] != undefined) {
                var select = $(key);
                select.options.length = 0;
                var options = Cookies[key + 'Opt'].split(',');
                for (var i = 0; i < options.length; i++) {
                    select.options[i] = new Option(options[i], options[i]);
                }
            }
        },
        loadPreferenceForOption: function(key) {
            var select = $(key);
            var value = Cookies[key];
            if (value !== undefined && value != null) {
                for (var i = 0; i < select.options.length; i++) {
                    if (select.options[i].value == value) {
                        select.selectedIndex = i;
                        break;
                    }
                }
            } else {
                select.selectedIndex = 0;
            }
        },
        storePreferenceForField: function(key) {
            Preferences.storePreference(key, $F(key));
        },
        storePreferenceForCheckbox: function(key) {
            Preferences.storePreference(key, $(key).checked);
        },
        storePreferenceForOption: function (key) {
            Preferences.storePreference(key, $F(key));
        },
        storePreferenceOptions: function(key) {
            var options = '';
            var select = $(key);
            for (var i = 0; i < select.options.length; i++) {
                if (i != 0) {
                    options += ',';
                }
                options += select.options[i].value;
            }
            Preferences.storePreference(key + 'Opt', options);
        },
        storePreference: function (key, value) {
            value = ('' + value).replace(/=/g, '%3D');
            Cookies.create(key, value);
        }
    };

    var Cookies = {
        init: function () {
            var allCookies = document.cookie.split('; ');
            for (var i = 0; i < allCookies.length; i++) {
                var cookiePair = allCookies[i].split('=');
                this[cookiePair[0]] = cookiePair[1];
            }
        },
        create: function (name, value, days) {
            if (!days) {
                days = 365;
            }
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            var expires = "; expires=" + date.toGMTString();
            document.cookie = name + "=" + value + expires + "; path=/";
            this[name] = value;
        },
        erase: function (name) {
            this.create(name, '', -1);
            this[name] = undefined;
        }
    };
    Cookies.init();
</script>
<style type="text/css">
    body, div, p, td, form, input, select, textarea {
        font: 12px Helvetica, Verdana, Arial, sans-serif;
    }

    .colorSelector {
        margin: 1px;
        border: black solid 1px;
        float: left;
        width: 30px;
        height: 30px;
    }

    .sizeSelector {
        margin: 1px;
        border: black solid 1px;
        float: left;
        width: 55px;
        height: 30px;
        font-weight: bold;
        text-align: center;
        vertical-align: middle;
        font-size: 20px;
        padding-top: 2px;
    }

    h1 {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 5px;
        clear: left;
    }

    h2 {
        font-size: 18px;
        font-weight: bold;
        margin-top: 15px;
        margin-bottom: 5px;
        clear: left;
    }

    .selected {
        margin: 0px;
        border-width: 2px;
    }

    .disabled {
        background-color: lightgray;
    }

    .invisible {
        display: none;
    }

    .text {
        padding: 4px;
        border: 1px solid black;
    }
</style>
</head>
<body>
<div style="width: 1020px;">
    <div style="width: 360px; height: 600px; margin-left:50px; margin-top: 20px; float: right;">
        <h1>Twittershirts.xxx</h1>

        <p>This page allows you to create your custom twitter shirt showing your latest
            tweet followers as a
            tag cloud and the number of followers. To create your custom shirt enter your
            twitter account name or id,
            select a shirt color, select a shirt size and finally click on buy now.<br/>
            Our fulfiller Spreadshirt will send your custom shirt to you in a couple of
            days.
        </p>

        <form>
            <h2>Twitter&nbsp;User</h2>
            <input id="tempUserId" type="text" name="twitterUserId"
                   value="<%=twitterUserId%>"
                   onchange="ApplicationController.updateTwitterUserId(); ApplicationController.updateProduct();"
                   style="font-size: 18px; width: 350px;" class="text"/>

            <h2>Shirt&nbsp;Color</h2>

            <div>
                <%
                    for (ProductTypeAppearance appearance : productType.getAppearances()) {
                %>
                <div id="appearance_<%=appearance.getId()%>" class="colorSelector"><img
                        src="<%=appearance.getImageUrl()%>,width=30,height=30"
                        onclick="ApplicationController.updateProductTypeAppearanceId('<%=appearance.getId()%>'); ApplicationController.updateProduct();"
                        width="30" height="30"/></div>
                <%
                    }
                %>
                <div style="clear:left;"></div>
            </div>

            <h2>Shirt&nbsp;Size</h2>

            <div>
                <%
                    for (ProductTypeSize size : productType.getSizes()) {
                %>
                <div id="size_<%=size.getId()%>" class="sizeSelector"
                     onclick="ApplicationController.updateProductTypeSizeId('<%=size.getId()%>');"><%=size.getName()%>
                </div>
                <%
                    }
                %>
                <div style="clear:left;"></div>
            </div>

            <h2>Quantity</h2>
            <input id="tempQuantity" type="text" name="quantity" value="1"
                   style="font-size: 18px; width: 40px;" class="text"
                   onchange="ApplicationController.updateQuantity();"/>
            <input type="hidden" id="productTypeImageUrl"
                   value="http://image.spreadshirt.net/image-server/v1/productTypes/6/views/1/appearances/{productTypeAppearanceId},width=600,height=600"/>
            <input type="hidden" id="imageUrl"
                   value="./image?twitterUserId={twitterUserId}&productTypeId={productTypeId}&productTypeAppearanceId={productTypeAppearanceId}&renderProductImage=true"/>
        </form>
        <div style="margin-top: 50px;">
            <form action="./order" method="POST">
                <input type="hidden" id="productTypeId" name="productTypeId" value=""/>
                <input type="hidden" id="productTypeAppearanceId"
                       name="productTypeAppearanceId" value=""/>
                <input type="hidden" id="productTypeSizeId" name="productTypeSizeId"
                       value=""/>
                <input type="hidden" id="twitterUserId" name="twitterUserId" value=""/>
                <input type="hidden" id="quantity" name="quantity" value=""/>

                <div id="price" style="font-size: 26px; font-weight: bold; float: left;">
                    -
                </div>
                <input type="submit" name="Submit" value="Buy Now!"
                       style="float:right; background-color: black; color: white; font-size: 18px; font-weight: bold;"/>
            </form>
        </div>
    </div>
    <div style="width: 600px; height: 600px;">
        <img id="productTypeImage"
             src=""
             alt="" width="600" height="600" style="z-index: 1; position:absolute;">
        <img id="textImage"
             src="empty.gif"
             alt="" width="600" height="600" style="z-index: 2; position:absolute; "
             class="">
        <img id="loaderImage"
             src="ajax-loader1.gif"
             alt="" width="128" height="15"
             style="z-index: 3; position:absolute; margin-left: 236px; margin-top: 200px;"
             class="">
    </div>
</div>
<script type="text/javascript">
    ApplicationController.init();
</script>
</body>
</html>