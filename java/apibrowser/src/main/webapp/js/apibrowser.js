var ApplicationController = {
    allValidResources: null,
    validLanguages: null,
    validCountries: null,
    initUrl: true,
    initEntry: true,
    knownPlaceholderValues: new Object(),
    init: function () {
        Preferences.loadPreferences();
        ApplicationController.updateUrlPlaceHolderValues();
        ApplicationController.initResourceHostData();
    },
    reinit: function() {
        Preferences.storePreferences();
        ApplicationController.initResourceHostData();
    },
    initResourceHostData: function () {
        Preferences.loadPreferenceOptions('hostList');
        Preferences.loadPreferenceForOption('hostList');
        new Ajax.Request('httpgateway?apiUrl=http://' + $F('hostList') + '/api/v1/metaData/api.wadl',
        {
            method:'get',
            asynchronous: false,
            onSuccess: function(transport) {
                var validResources = new Array();
                var response = RequestHandling.getXMLResponse(transport);
                var resources = response.getElementsByTagName('resources')[0];
                var base = resources.getAttribute("base");
                var resourceList = resources.getElementsByTagName('resource');
                for (var i = 0; i < resourceList.length; i++) {
                    var validResource = new Object();
                    validResource['name'] = resourceList[i].getAttribute("path");
                    validResource['path'] = base + resourceList[i].getAttribute("path");
                    validResource['pattern'] = resourceList[i].getAttribute("path");
                    validResource['methods'] = new Array();

                    var methodList = resourceList[i].getElementsByTagName('method');
                    for (var j = 0; j < methodList.length; j++) {
                        var validMethod = new Object();
                        var methodDescription = '';
                        if (methodList[j].getElementsByTagName("doc")[0].childNodes.length > 0)
                            methodDescription = methodList[j].getElementsByTagName("doc")[0].childNodes[0].nodeValue;
                        validMethod['description'] = '' + resourceList[i].getElementsByTagName("doc")[0].childNodes[0].nodeValue + '\n\n' + methodDescription;
                        validMethod['method'] = methodList[j].getAttribute("name");
                        var paramList = methodList[j].getElementsByTagName('param');
                        validMethod['apiKeyProtected'] = ApplicationController.isResourceParameterSet(paramList, "apiKey");
                        validMethod['sessionIdProtected'] = ApplicationController.isResourceParameterSet(paramList, "sessionId");
                        validMethod['list'] = (ApplicationController.isResourceParameterSet(paramList, "offset") &&
                                               ApplicationController.isResourceParameterSet(paramList, "limit")) ||
                                              ApplicationController.isResourceParameterSet(paramList, "fullData");
                        validResource['methods'].push(validMethod);
                        // move get first
                        if (j > 0 && validMethod['method'].toUpperCase() == 'GET') {
                            validResource['methods'][j] = validResource['methods'][0];
                            validResource['methods'][0] = validMethod;
                        }
                    }
                    validResources.push(validResource);
                }
                ApplicationController.allValidResources = validResources;
                ApplicationController.initTemplates();
                ApplicationController.initLocales();
                ApplicationController.initResourceList();
            },
            onFailure: function() {
                $('response').update('Could not load wadl...');
            }
        });
    },
    isResourceParameterSet: function(paramList, name) {
        for (var l = 0; l < paramList.length; l++) {
            if (paramList[l].getAttribute("name") == name) {
                return true;
            }
        }
        return false;
    },
    initResourceList: function () {
        var resourceList = $('resourceList');
        resourceList.options.length = 0;
        for (var i = 0; i < ApplicationController.allValidResources.length; i++) {
            resourceList.options[i] = new Option(ApplicationController.allValidResources[i]['name'], i);
        }
        Preferences.loadPreferenceForOption('resourceList');
        $('response').update('Wadl loaded ...');
        ApplicationController.selectResource();
    },
    initLocales: function () {
        ApplicationController.validLanguages = null;
        ApplicationController.validCountries = null;
        new Ajax.Request('httpgateway?apiUrl=http://' + $F('hostList') + '/api/v1/languages?fullData=true',
        {
            method:'get',
            asynchronous: false,
            onSuccess: function(transport) {
                var languages = new Array();
                var response = RequestHandling.getXMLResponse(transport);
                var isoCodes = response.getElementsByTagName('isoCode');
                for (var i = 0; i < isoCodes.length; i++) {
                    languages[i] = isoCodes[i].childNodes[0].nodeValue;
                }
                ApplicationController.validLanguages = languages;
            },
            onFailure: function() {
                $('response').update('Could not load languages ...');
            }
        });
        new Ajax.Request('httpgateway?apiUrl=http://' + $F('hostList') + '/api/v1/countries?fullData=true',
        {
            method:'get',
            asynchronous: false,
            onSuccess: function(transport) {
                var countries = new Array();
                var response = RequestHandling.getXMLResponse(transport);
                var isoCodes = response.getElementsByTagName('isoCode');
                for (var i = 0; i < isoCodes.length; i++) {
                    countries[i] = isoCodes[i].childNodes[0].nodeValue;
                }
                ApplicationController.validCountries = countries;
            },
            onFailure: function() {
                $('response').update('Could not load countries ...');
            }
        });
        if (ApplicationController.validLanguages != null && ApplicationController.validCountries != null) {
            var localeList = $('localeList');
            localeList.options.length = 0;
            var count = 0;
            for (var i = 0; i < ApplicationController.validCountries.length; i++) {
                for (var j = 0; j < ApplicationController.validLanguages.length; j++) {
                    var locale = ApplicationController.validLanguages[j] + '_' + ApplicationController.validCountries[i];
                    localeList.options[count++] = new Option(locale, locale);
                }
            }
            localeList.selectedIndex = 0;
            Preferences.loadPreferenceForOption('localeList');
        } else {
            $('response').update('Could not init locales ...');
        }
    },
    initTemplates: function () {
        var templates = $('templates');
        templates.options.length = 0;
        templates.options[0] = new Option("Request (Normal)", "request-normal.xml");
        templates.options[1] = new Option("Request (Compat)", "request-compat.xml");
        templates.options[2] = new Option("Basket", "basket.xml");
        templates.options[3] = new Option("BasketItem Article", "basketItem-article.xml");
        templates.options[4] = new Option("BasketItem Product", "basketItem-product.xml");
        templates.options[5] = new Option("Design", "design.xml");
        templates.options[6] = new Option("Product with Design", "product-with-design.xml");
        templates.options[7] = new Option("Product with Design and Text", "product-with-text-and-design.xml");
        templates.options[8] = new Option("Product with One Text Line", "product-with-text-one-text-line.xml");
        templates.options[9] = new Option("Product with One Text Line", "product-with-text-one-text-line1.xml");
        templates.options[10] = new Option("Product with Two Text Lines", "product-with-text-two-text-lines.xml");
        templates.options[11] = new Option("Product with Multiple Text Lines", "product-with-text-multiple-text-lines.xml");
        templates.options[12] = new Option("Product with Rotated Design", "product-with-design-rotated-at-center.xml");
        templates.options[13] = new Option("Product with Rotated Design Around Zero", "product-with-design-rotated-around-zero.xml");
        templates.options[14] = new Option("Product with Rotated and Translated Design", "product-with-design-rotated-and-translated.xml");
        templates.options[15] = new Option("Product with Design Mirrored around X", "product-with-design-mirrored-around-x.xml");
        templates.options[16] = new Option("Product with Design Mirrored around Y", "product-with-design-mirrored-around-y.xml");
        templates.options[17] = new Option("Product with Design Mirrored around X and Y", "product-with-design-mirrored-around-x-y.xml");
        templates.options[18] = new Option("Product with Rotated Text", "product-with-text-rotated.xml");
        templates.options[19] = new Option("Product with Rotated and Translated Text", "product-with-text-rotated-and-translated.xml");
        templates.options[20] = new Option("Product with Text Mirrored around Y", "product-with-text-mirrored-around-y.xml");
        templates.options[21] = new Option("Product with Text Mirrored around X", "product-with-text-mirrored-around-x.xml");
        templates.options[22] = new Option("Product with Text Mirrored around X and Y", "product-with-text-mirrored-around-x-y.xml");
        templates.options[23] = new Option("Product with Text Mirrored, Translated, Rotated", "product-with-text-mirrored-rotated-translated.xml");
        templates.options[24] = new Option("Product with Text in Text Tag", "product-with-text-in-text-tag.xml");
        templates.options[25] = new Option("Product with Text in TSpan Tag", "product-with-text-in-tspan-tag.xml");
        templates.options[26] = new Option("Product with Text Overlapping", "product-with-overlapping-text.xml");
        templates.options[27] = new Option("Product with Text Digital Direct Print Type Colors (RGB)", "product-with-text-digi-printtype-colors.xml");
        templates.options[28] = new Option("Product with Text Flex Print Type Colors (RGB)", "product-with-text-flex-printtype-colors.xml");
        templates.options[29] = new Option("Product with Text Flock Print Type Colors (RGB)", "product-with-text-flock-printtype-colors.xml");
        templates.options[30] = new Option("Product with Text Multiple Print Color Layers", "product-with-text-and-multiple-layered-printcolors.xml");
        templates.options[31] = new Option("Product with Text Max Print Color Layers", "product-with-text-and-max-layered-printcolors.xml");
        templates.options[32] = new Option("Product with Text Minimal Font Size", "product-with-text-min-font-size.xml");
        templates.options[33] = new Option("Product with Text Maximal Font Size", "product-with-text-max-font-size.xml");
        templates.options[34] = new Option("Product with Text Empty", "product-with-empty-text.xml");
        templates.options[35] = new Option("Product with Text and different Fonts", "product-with-text-different-fonts.xml");
        templates.options[36] = new Option("Product with Text and different Font Access Patterns", "product-with-text-and-different-font-access-patterns.xml");
        templates.options[37] = new Option("Product with Text aligned Left", "product-with-text-aligned-left.xml");
        templates.options[38] = new Option("Product with Text aligned Middle", "product-with-text-aligned-middle.xml");
        templates.options[39] = new Option("Product with Text aligned Right", "product-with-text-aligned-right.xml");
        templates.options[40] = new Option("Product with Text aligned Differently", "product-with-text-aligned-differently.xml");
        templates.options[41] = new Option("Product with Text almost outside Print Type Print Size", "product-with-text-outside-printtype-size-almost.xml");
        templates.options[42] = new Option("Product with Text outside Print Type Print Size but Multicolored", "product-with-text-outside-printtype-size-but-multicolored.xml");
        templates.options[43] = new Option("Product with No Configuration", "product-with-text-no-configuration.xml");
        templates.options[44] = new Option("Product with Design Digi Print Type Colors", "product-with-design-digi-printtype-colors.xml");
        templates.options[45] = new Option("Product with Design Flex Print Type Colors", "product-with-design-flex-printtype-colors.xml");
        templates.options[46] = new Option("Product with Design Flock Print Type Colors", "product-with-design-flock-printtype-colors.xml");
    },
    selectResource: function () {
        ApplicationController.updateResourceMethod();
        Preferences.loadPreferenceForOption('resourceMethod');
        ApplicationController.selectResourceMethod();
    },
    updateResourceMethod: function () {
        var resourceList = $('resourceList');
        var resourceMethod = $('resourceMethod');
        resourceMethod.options.length = 0;
        var methods = ApplicationController.allValidResources[resourceList.selectedIndex]['methods'];
        for (var i = 0; i < methods.length; i++) {
            resourceMethod.options[i] = new Option(methods[i]['method'], i);
        }
    },
    selectResourceMethod: function () {
        ApplicationController.updateResourceMethodInfo();
        var path = ApplicationController.allValidResources[$F('resourceList')]['path'];
        path = ApplicationController.replaceUrlPlaceHolderValues(path);
        if (ApplicationController.initUrl && $F('method') == 'GET') {
            $('url').value = Preferences.getPreferenceForField('historyList', path);
            ApplicationController.checkSettings($F('url'));
        } else {
            path = path.indexOf('#') != -1 ? path.substring(0, path.indexOf('#')) : path;
            $('url').value = path;
        }
        ApplicationController.initUrl = false;

        if ($F('method') == 'GET' || $F('method') == 'DELETE') {
            ResponseDialogController.loadData();
        } else if ($F('method') == 'POST') {
            RequestDialogController.displayRequestBoxForAdd();
        } else if ($F('method') == 'PUT') {
            RequestDialogController.displayRequestBoxForReplace();
        }
    },
    updateResourceMethodInfo: function () {
        var resourceMethod = ApplicationController.getCurrentResourceMethod();
        $('method').value = resourceMethod['method'];
        $('apiKeyProtected').checked = resourceMethod['apiKeyProtected'] ? 1 : 0;
        $('sessionIdProtected').checked = resourceMethod['sessionIdProtected'] ? 1 : 0;
        ApplicationController.setResourceMethod(resourceMethod['method']);
        $('resourceDescription').update(resourceMethod['description']);
    },
    getCurrentResourceMethod: function() {
        return ApplicationController.allValidResources[$F('resourceList')]['methods'][$F('resourceMethod')];
    },
    setResourceMethod: function (name) {
        var resourceMethods = $('resourceMethod');
        for (var i = 0; i < resourceMethods.options.length; i++) {
            if (resourceMethods.options[i].text == name) {
                resourceMethods.selectedIndex = i;
            }
        }
    },
    checkSettings: function(url) {
        var i = ApplicationController.findPatternForUrl(url);
        if (i != -1) {
            $('resourceList').selectedIndex = i;
            $('resourceMethod').selectedIndex = 0;
            ApplicationController.updateResourceMethod();
            ApplicationController.updateResourceMethodInfo();
        }
    },
    findPatternForUrl: function(url) {
        var urlSegments = ApplicationController.getUrlSegments(url);
        for (var i = 0; i < ApplicationController.allValidResources.length; i++) {
            var resource = ApplicationController.allValidResources[i];
            var patternSegments = resource['pattern'].split('/');
            if (urlSegments.length == patternSegments.length) {
                for (var j = 0; j < urlSegments.length; j++) {
                    if (patternSegments[j].indexOf('{') == -1) {
                        if (urlSegments[j] != patternSegments[j])
                            break;
                    }
                    if (j + 1 == urlSegments.length) {
                        return i;
                    }
                }
            }
        }
        return -1;
    },
    getUrlSegments: function (url) {
        url = url.indexOf('#') == -1 ? url : url.substring(0, url.indexOf('#'));
        url = url.indexOf('?') == -1 ? url : url.substring(0, url.indexOf('?'));
        url = url.substring(url.indexOf('api/v1/') + 7);
        return url.split('/');
    },
    storeUrlPlaceHolderValues: function() {
        var url = $F('url');
        var urlSegments = ApplicationController.getUrlSegments(url);
        var k = ApplicationController.findPatternForUrl(url);
        if (k != -1) {
            var resource = ApplicationController.allValidResources[k];
            var patternSegments = resource['pattern'].split('/');
            var placeHolderValues = new Array();
            if (urlSegments.length == patternSegments.length) {
                for (var j = 0; j < urlSegments.length; j++) {
                    if (patternSegments[j].indexOf('{') == -1) {
                        if (urlSegments[j] != patternSegments[j])
                            break;
                    } else {
                        if (urlSegments[j].indexOf('{') == -1) {
                            var placeHolderValue = new Object();
                            placeHolderValue['placeHolder'] = patternSegments[j];
                            placeHolderValue['value'] = urlSegments[j];
                            placeHolderValues.push(placeHolderValue);
                        }
                    }
                    if (j + 1 == urlSegments.length) {
                        for (var i = 0; i < placeHolderValues.length; i++) {
                            ApplicationController
                                    .knownPlaceholderValues[placeHolderValues[i]['placeHolder']] =
                            placeHolderValues[i]['value'];
                        }
                    }
                }
            }
        }
    },
    updateUrlPlaceHolderValues: function() {
        ApplicationController.knownPlaceholderValues['{userId}'] = $F('userId');
        ApplicationController.knownPlaceholderValues['{shopId}'] = $F('shopId');
    },
    replaceUrlPlaceHolderValues: function(path) {
        var segments = path.split('/');
        for (var i = 0; i < segments.length; i++) {
            if (segments[i].indexOf('{') != -1) {
                var replacement = ApplicationController.knownPlaceholderValues[segments[i]];
                if (replacement != null && replacement != '') {
                    path = path.replace(segments[i], replacement);
                }
            }
        }
        return path;
    },
    resizeApp: function () {
        var responseScrollBox = document.getElementById("responseScrollBox");
        var editBox = document.getElementById("editBox");
        var responseBox = document.getElementById("responseBox");
        var historyList = document.getElementById("historyList");
        var requestBox = document.getElementById("requestBox");
        var request = document.getElementById("request");

        var htmlheight = document.body.parentNode.scrollHeight;
        var windowheight = window.innerHeight;
        var htmlwidth = document.body.parentNode.scrollWidth;
        var windowwidth = window.innerWidth;

        var height = (htmlheight < windowheight) ? windowheight : htmlheight;
        height = (height < parseInt(editBox.style.height.replace('px', ''))) ? editBox.style.height.replace('px', '') : height;
        var width = (htmlwidth < windowwidth) ? windowwidth : htmlwidth;

        responseBox.style.height = (height - responseBox.style.top.replace('px', '') - 10) + "px";
        responseBox.style.width = (width - editBox.style.width.replace('px', '') - 40) + "px";

        historyList.style.width = (width - historyList.style.left.replace('px', '') - editBox.style.width.replace('px', '') - 45) + "px";

        responseScrollBox.style.height = (responseBox.style.height.replace('px', '') - responseScrollBox.style.top.replace('px', '') - 5) + "px";
        responseScrollBox.style.width = (responseBox.style.width.replace('px', '') - 20) + "px";

        requestBox.style.height = (height - requestBox.style.top.replace('px', '') - 10) + "px";
        requestBox.style.width = (width - editBox.style.width.replace('px', '') - 40) + "px";

        request.style.height = (requestBox.style.height.replace('px', '') - request.style.top.replace('px', '')) + "px";
        request.style.width = (requestBox.style.width.replace('px', '') - 20) + "px";

        editBox.style.left = (width - editBox.style.width.replace('px', '') - 30) + "px";
    }
};

var HostDialogController = {
    initHosts: function () {
        var editHostList = $('editHostList');
        var hostList = $('hostList');
        editHostList.options.length = 0;
        for (var i = 0; i < hostList.options.length; i++) {
            editHostList.options[i] = new Option(hostList.options[i].value, hostList.options[i].value);
        }
        $('newHost').value = '';
    },
    addHost: function() {
        var editHostList = $('editHostList');
        editHostList.options[editHostList.options.length] = new Option($('newHost').value, $('newHost').value);
        editHostList.selectedIndex = editHostList.options.length - 1;
        $('newHost').value = '';
    },
    deleteHost: function() {
        var editHostList = $('editHostList');
        if (editHostList.selectedIndex > 1) {
            editHostList.remove(editHostList.selectedIndex);
        }
    },
    synchronizeHosts: function() {
        var editHostList = $('editHostList');
        var hostList = $('hostList');
        hostList.options.length = 0;
        for (var i = 0; i < editHostList.options.length; i++) {
            hostList.options[i] = new Option(editHostList.options[i].value, editHostList.options[i].value);
        }
        ApplicationController.reinit();
    }
};

var LoginDialogController = {
    login: function() {
        new Ajax.Request('httpgateway?apiUrl=http://' + $F('hostList') + '/api/v1/sessions',
        {
            contentType: 'application/xml',
            encoding:     'UTF-8',
            method: 'post',
            postBody: '<login xmlns="http://api.spreadshirt.net"><username>' + $F('username') + '</username><password>' + $F('password') + '</password></login>',
            asynchronous: false,
            onSuccess: function(transport) {
                var location = transport.getHeader('Location');
                $('sessionId').value = location.substring(location.lastIndexOf('/') + 1);
                Preferences.storePreferences();
                ApplicationController.selectResourceMethod();
                Modalbox.hide();
            },
            onFailure: function(transport) {
                $('loginError').update(RequestHandling.getErrorText(transport));
                $('loginError').show();
                Modalbox.resizeToContent();
            }
        });
    }
};

var ResponseDialogController = {
    loadLink: function (apiUrl)
    {
        $('url').value = apiUrl;
        ApplicationController.storeUrlPlaceHolderValues();
        ApplicationController.checkSettings($F('url'));
        ResponseDialogController.loadData();
    },
    loadData: function () {
        var url = $F('url');
        if ($F('method') == 'GET' || $F('method') == 'DELETE') {
            if (!ApplicationController.initEntry) {
                if (url.indexOf('image-server') == -1) {
                    var scrollPosition = url.indexOf('#') != -1 ? url.substring(url.indexOf('#') + 1) : 0;
                    url = url.indexOf('#') != -1 ? url.substring(0, url.indexOf('#')) : url;
                    url = RequestHandling.updateUrlParameter(url, 'locale', /locale=(\w\w_\w\w)/, $F('localeList'));
                    url = RequestHandling.addOrUpdateModeOnUrl(url);
                    if (ApplicationController.getCurrentResourceMethod()['list']) {
                        url = RequestHandling.updateUrlParameter(url, 'fullData', /fullData=\w+/, $('fullData').checked ? 'true' : 'false');
                        url = RequestHandling.updateUrlParameter(url, 'limit', /limit=\d+/, $F('limit'));
                        url = RequestHandling.updateUrlParameter(url, 'offset', /offset=\d+/, $F('entries'));
                    }
                    url += '#' + scrollPosition;
                } else {
                    url = RequestHandling.updateUrlParameter(url, 'mediaType', /mediaType=(\w\w\w)/, $F('imageMediaType'));
                    url = RequestHandling.updateUrlParameter(url, 'width', /width=(\d+)/, $F('imageWidth'));
                    url = RequestHandling.updateUrlParameter(url, 'height', /height=(\d+)/, $F('imageHeight'));
                }
            }
            ApplicationController.initEntry = false;
            $('url').value = url;

            History.storeHistoryLink(url);
            Preferences.storePreferences();
            ResponseDialogController.loadDataInternal();
        }
    },
    loadDataInternal: function () {
        if ($F('method') == 'GET' || $F('method') == 'DELETE') {
            $('response').update('Loading ...');
            Element.show('responseBox');
            Element.hide('requestBox');
            ResponseDialogController.hideImage();
            URLEncoder.encodeUrl();

            if ($F('url').indexOf('image-server') != -1) {
                ResponseDialogController.displayImageOptions();
                var imageUrl = $F('url').replace(/[\?&]/g, ',');
                $('response').update('<img src="' + imageUrl + '"/>');
            }
            else {
                ApplicationController.storeUrlPlaceHolderValues();
                if (ApplicationController.getCurrentResourceMethod()['list'])
                    ResponseDialogController.displayListOptions();
                else
                    ResponseDialogController.displayEntityOptions();
                new Ajax.Request('httpgateway?apiUrl=' + RequestHandling.prepareRequestUrl($F('encodedUrl')),
                {
                    method:'get',
                    onSuccess: function(transport) {
                        var count = null;
                        var offset = null;
                        if (ApplicationController.getCurrentResourceMethod()['list'] && $F('renderList')) {
                            var response = RequestHandling.getXMLResponse(transport);
                            if ($F('compatMode') == 'true' || $F('compatMode') == 'on' || $F('compatMode') == '1') {
                                var payloadNode = response.getElementsByTagName('payload')[0];
                                if (payloadNode.childNodes[0].nodeName == '#text')
                                    response = payloadNode.childNodes[1];
                                else
                                    response = payloadNode.childNodes[0];
                            }

                            if (response.getAttributeNode('count') != null)
                                count = response.getAttributeNode('count').nodeValue;
                            if (response.getAttributeNode('offset') != null)
                                offset = response.getAttributeNode('offset').nodeValue;

                            if (count != null && offset != null) {
                                var renderContent = '<table style="border: 1px solid #DDDDDD;">';
                                for (var i = 0; i < response.childNodes.length; i++) {
                                    if (response.childNodes[i].nodeName != '#text') {
                                        var node = response.childNodes[i];
                                        var name = '';
                                        var description = '';
                                        var entityUrl = '';
                                        var imageUrl = '';

                                        if (node.getAttributeNode('xlink:href') != null)
                                            entityUrl = node.getAttributeNode('xlink:href').nodeValue;
                                        for (var j = 0; j < node.childNodes.length; j++) {
                                            if (node.childNodes[j].nodeName != '#text') {
                                                var subNode = node.childNodes[j];

                                                if (subNode.nodeName == 'name') {
                                                    if (subNode.firstChild != null && subNode.firstChild != undefined)
                                                        name = subNode.firstChild.nodeValue;
                                                }
                                                if (subNode.nodeName == 'description') {
                                                    if (subNode.firstChild != null && subNode.firstChild != undefined)
                                                        description = subNode.firstChild.nodeValue;
                                                }
                                                if (subNode.nodeName == 'resources') {
                                                    imageUrl = subNode.getElementsByTagName('resource')[0].getAttributeNode('xlink:href').nodeValue;
                                                }
                                            }
                                        }

                                        renderContent += '<tr>';
                                        if (imageUrl != undefined && imageUrl != null && imageUrl != '') {
                                            imageUrl = imageUrl.indexOf(',') != -1 ? imageUrl.substring(0, imageUrl.indexOf(',')) : imageUrl;
                                            imageUrl = imageUrl.indexOf('?') != -1 ? imageUrl.substring(0, imageUrl.indexOf('?')) : imageUrl;
                                            renderContent += '<td valign="top"><a style="text-decoration: underlined; cursor:pointer;cursor:hand;" onclick="ResponseDialogController.loadLink(\'' + imageUrl + '\')"><img src="' + imageUrl + '"/></a></td>';
                                        }
                                        renderContent += '<td valign="top"><b>' + name + '</b><br/><br/>' + description + '</td>';
                                        renderContent += '<td valign="top"><a style="background-color: #FFD700; text-decoration: underlined; cursor:pointer;cursor:hand;" onclick="ResponseDialogController.loadLink(\'' + entityUrl + '\')">See Entity</a></td>';
                                        renderContent += '</tr>';
                                    }
                                }
                                renderContent += '</table>';
                                $('response').innerHTML = renderContent;
                            }
                        }

                        if (count == null || offset == null) {
                            var text = transport.responseText;
                            var regex = /count="(\d+)"/;
                            var matches = regex.exec(text);
                            if (matches != null && matches.length > 1)
                                count = matches[1];
                            regex = /offset="(\d+)"/;
                            matches = regex.exec(text);
                            if (matches != null && matches.length > 1)
                                offset = matches[1];
                            text = text.replace(/\</g, '&lt;');
                            text = text.replace(/\>/g, '&gt;');
                            text = text.replace(/\n/g, '<br/>');
                            text = text.replace(/  /g, '&nbsp;&nbsp;');
                            text = text.replace(/&amp;/g, '&');
                            text = text.replace(/xlink:href=\"([a-zA-Z0-9_\/:,.\-=\?\[\]\\(\)&]+)\"/g,
                                    'xlink:href="<a style="background-color: #FFD700; cursor:pointer;cursor:hand;" onmouseover="ResponseDialogController.displayImage(event, \'$1\')" onmouseout="ResponseDialogController.hideImage(event)" onclick="ResponseDialogController.loadLink(\'$1\')">$1</a>"');
                            $('response').update(text);
                        }

                        if (ApplicationController.getCurrentResourceMethod()['list'])
                            ResponseDialogController.updatePageList(offset, count);

                        var url = $F('url');
                        if (url.indexOf('#') != -1) {
                            $('responseScrollBox').scrollTop = parseInt(url.substr(url.indexOf('#') + 1));
                        }
                    },
                    onFailure: function(transport) {
                        $('response').update(RequestHandling.getErrorText(transport));
                    }
                });
            }
        }
    },
    updatePageList: function (offset, count) {
        var entries = $('entries');
        entries.options.length = 0;
        if (offset != null && count != null && offset != '' && count != '' && count != -1) {
            count = parseInt(count);
            offset = parseInt(offset);
            var iter = 0;
            var limit = parseInt($F('limit'));
            var first = 0;

            var lastPageEntries = count % limit;
            var last = count - lastPageEntries;

            var min;
            var max;

            if (offset != first) {
                entries.options[iter++] = new Option(first + '-' + limit, first);
                entries.options[iter++] = new Option('...', offset);
            }
            for (var i = 10; i > 0; i--) {
                if (offset - (i * limit) >= limit) {
                    min = offset - (i * limit);
                    max = min + limit;
                    entries.options[iter++] = new Option(min + '-' + max, offset - (i * limit));
                }
            }
            max = offset + limit;
            max = (max > count) ? count : max;
            entries.options[iter++] = new Option(offset + '-' + max, offset);
            entries.selectedIndex = iter - 1;
            for (var j = 0; j < 10; j++) {
                if (offset + limit + (j * limit) < last) {
                    min = offset + limit + (j * limit);
                    max = min + limit;
                    max = (max > count) ? count : max;
                    entries.options[iter++] = new Option(min + '-' + max, offset + limit + (j * limit));
                }
            }
            if (offset != last) {
                if (last + lastPageEntries == last) {
                    last -= limit;
                    max = last + limit;
                } else {
                    max = (last + lastPageEntries);
                }
                entries.options[iter++] = new Option('...', offset);
                entries.options[iter++] = new Option(last + '-' + max, last);
            }
        }
        else {
            ResponseDialogController.cleanEntries();
        }
    },
    cleanEntries: function () {
        var entries = $('entries');
        entries.options[0] = new Option('0', 0);
        entries.selectedIndex = 0;
    },
    displayImage: function (event, imageUrl) {
        if (imageUrl.indexOf('image-server') != -1) {
            imageUrl = RequestHandling.updateUrlParameter(imageUrl, 'width', /width=[0-9]+/, '300');
            imageUrl = RequestHandling.updateUrlParameter(imageUrl, 'height', /height=[0-9]+/, '300');
            imageUrl = imageUrl.replace(/[\?&]/g, ',');
            $('image').update('<img src="' + imageUrl + '"/>');
            $('image').style.left = Event.pointerX(event) + 15;
            $('image').style.top = Event.pointerY(event) + 15;
            $('image').show();
        }
    },
    hideImage: function () {
        $('image').hide();
    },
    displayImageOptions: function () {
        $('imageOptionsBox').show();
        $('entityOptionsBox').hide();
        $('listOptionsBox').hide();
    },
    displayListOptions: function () {
        $('imageOptionsBox').hide();
        $('entityOptionsBox').hide();
        $('listOptionsBox').show();
    },
    displayEntityOptions: function() {
        $('imageOptionsBox').hide();
        $('entityOptionsBox').show();
        $('listOptionsBox').hide();

        $('editDataButton').disabled = true;
        $('deleteDataButton').disabled = true;

        for (var i = 0; i < $('resourceMethod').options.length; i++) {
            if ($('resourceMethod').options[i].text == 'PUT') {
                $('editDataButton').disabled = false;
            }
            if ($('resourceMethod').options[i].text == 'DELETE') {
                $('deleteDataButton').disabled = false;
            }
        }
    },
    synchImageSize: function (source) {
        if ($('synchImageSize').checked) {
            if (source == 'imageHeight') {
                $('imageWidth').value = $F('imageHeight');
            }
            else if (source == 'imageWidth') {
                $('imageHeight').value = $F('imageWidth');
            }
        }
    }
};

var RequestDialogController = {
    replaceMode: true,
    displayRequestBoxForAdd: function ()
    {
        ApplicationController.setResourceMethod('POST');
        ApplicationController.updateResourceMethodInfo();
        RequestDialogController.replaceMode = false;
        Element.show('requestBox');
        Element.hide('responseBox');
        $('request').value = '';
        $('templates').selectedIndex = ($('compatMode').checked) ? 1 : 0;
        RequestDialogController.addTemplateData();
    },

    displayRequestBoxForReplace: function () {
        ApplicationController.setResourceMethod('PUT');
        ApplicationController.updateResourceMethodInfo();
        RequestDialogController.replaceMode = true;
        Element.show('requestBox');
        Element.hide('responseBox');

        $('method').value = 'GET';
        URLEncoder.encodeUrl();
        $('method').value = 'PUT';

        var callerUrl = $F('encodedUrl');
        callerUrl = RequestHandling.addOrUpdateModeOnUrl(callerUrl);

        if ($F('url').indexOf('image-server') == -1) {
            new Ajax.Request('httpgateway?apiUrl=' + RequestHandling.prepareRequestUrl(callerUrl),
            {
                method:'get',
                onSuccess: function(transport) {
                    var text = transport.responseText;
                    text = text.replace(/<response/, "<request");
                    text = text.replace(/<\/response>/, "<\/request>");
                    $('request').value = text;
                },
                onFailure: function(transport) {
                    $('request').value = RequestHandling.getErrorText(transport);
                }
            });
        }
    },
    addTemplateData: function () {
        new Ajax.Request('templates/' + $F('templates'),
        {
            method: 'get',

            onSuccess: function(transport) {
                var text = transport.responseText;
                text = text.replace(/{resourceHost}/g, $F('hostList'));
                RequestDialogController.insertTemplateData($('request'), text, null);
            },
            onFailure: function(transport) {
                alert("Template " + $F('templates') + " not found!");
            }
        });
    },
    insertTemplateData: function (input, aTag, eTag) {
        input.focus();

        if (!aTag) return;
        if (!eTag) eTag = '';

        var insText;
        if (typeof document.selection != 'undefined') {
            var range = document.selection.createRange();
            insText = range.text;
            if (!insText && eTag)
                return;
            range.text = aTag + insText + eTag;
            range = document.selection.createRange();
            if (insText.length == 0)
                range.move('character', -eTag.length);
            else
                range.moveStart('character', aTag.length + insText.length + eTag.length);
            range.select();
        } else if (typeof input.selectionStart != 'undefined') {
            var start = input.selectionStart;
            var end = input.selectionEnd;
            insText = input.value.substring(start, end);
            if (!insText && eTag)
                return;
            input.value = input.value.substr(0, start) + aTag + insText + eTag + input.value.substr(end);
            var pos;
            if (insText.length == 0)
                pos = start + aTag.length;
            else
                pos = start + aTag.length + insText.length + eTag.length;
            input.selectionStart = pos;
            input.selectionEnd = pos;
        } else {
            input.value = input.value + aTag;
        }
    },
    cancelEdit: function () {
        if ($F('method') == 'PUT') {
            Element.hide('requestBox');
            Element.show('responseBox');
            ApplicationController.setResourceMethod('GET');
            ResponseDialogController.loadData();
        }
    },
    submitData: function () {
        if (RequestDialogController.replaceMode) {
            RequestDialogController.putData();
        } else {
            RequestDialogController.postData();
        }
    },
    postData: function () {
        ApplicationController.setResourceMethod('POST');
        ApplicationController.updateResourceMethodInfo();
        URLEncoder.encodeUrl();

        if (!$F('url').indexOf('image-server') != -1) {
            var callerUrl = $F('encodedUrl');
            callerUrl = RequestHandling.addOrUpdateModeOnUrl(callerUrl);

            new Ajax.Request('httpgateway?apiUrl=' + RequestHandling.prepareRequestUrl(callerUrl),
            {
                contentType: 'application/xml',
                encoding:     'UTF-8',
                method: 'post',
                postBody: $F('request'),
                onSuccess: function(transport) {
                    if (RequestHandling.wasRequestSuccessful(transport)) {
                        ResponseDialogController.loadLink(transport.getHeader('Location'));
                    } else {
                        alert("Error: " + RequestHandling.getErrorText(transport));
                    }
                },
                onFailure: function(transport) {
                    alert("Error: " + RequestHandling.getErrorText(transport));
                }
            });
        }
    },
    putData: function () {
        ApplicationController.setResourceMethod('PUT');
        ApplicationController.updateResourceMethodInfo();
        URLEncoder.encodeUrl();

        if (!$F('url').indexOf('image-server') != -1) {
            var callerUrl = $F('encodedUrl');
            //+ "&method=put";
            callerUrl = RequestHandling.addOrUpdateModeOnUrl(callerUrl);
            new Ajax.Request('httpgateway?apiUrl=' + RequestHandling.prepareRequestUrl(callerUrl),
            {
                contentType: 'application/xml',
                encoding:     'UTF-8',
                method: 'put',
                postBody: $F('request'),
                onSuccess: function(transport) {
                    if (RequestHandling.wasRequestSuccessful(transport)) {
                        ResponseDialogController.loadLink($F('url'));
                    } else {
                        alert("Error: " + RequestHandling.getErrorText(transport));
                    }
                },
                onFailure: function(transport) {
                    alert("Error: " + RequestHandling.getErrorText(transport));
                }
            });
        }
    },
    deleteData: function () {
        ApplicationController.setResourceMethod('DELETE');
        ApplicationController.updateResourceMethodInfo();
        URLEncoder.encodeUrl();

        if (!$F('url').indexOf('image-server') != -1) {
            var callerUrl = $F('encodedUrl');
                    //+ "&method=delete";
            callerUrl = RequestHandling.addOrUpdateModeOnUrl(callerUrl);

            new Ajax.Request('httpgateway?apiUrl=' + RequestHandling.prepareRequestUrl(callerUrl),
            {
                contentType: 'application/xml',
                encoding:     'UTF-8',
                method: 'delete',
                postBody: $F('request'),
                onSuccess: function(transport) {
                    if (RequestHandling.wasRequestSuccessful(transport)) {
                        $('response').update("Entry deleted successfully: " + callerUrl);
                    } else {
                        alert("Error: " + RequestHandling.getErrorText(transport));
                    }
                },
                onFailure: function(transport) {
                    alert("Error: " + RequestHandling.getErrorText(transport));
                }
            });
        }
    }
};

var URLEncoder = {
    encodeUrl: function () {
        URLEncoder.updateTime();

        var apiKey = $F('apiKey');
        var secret = $F('secret');
        var sessionId = $F('sessionId');
        var method = $F('method');
        var url = $F('url');
        url = url.indexOf('#') != -1 ? url.substring(0, url.indexOf('#')) : url;
        var time = $F('time');
        var apiKeyProtected = $F('apiKeyProtected');
        var sessionIdProtected = $F('sessionIdProtected');

        if (apiKeyProtected) {
            if (!URLEncoder.isAPIKeyValid(apiKey)) {
                alert("Please provide a valid api key!");
                return;
            }
            if (!URLEncoder.isSecretValid(secret)) {
                alert("Please provide a valid secret!");
                return;
            }
        }
        if (sessionIdProtected) {
            if (!URLEncoder.isSessionIdValid(sessionId)) {
                alert("Please provide a valid sessionId!");
                return;
            }
        }

        if (!URLEncoder.isUrlValid(url)) {
            alert("Please provide a valid url!");
            return;
        }

        if (apiKeyProtected) {
            var index = url.indexOf('?');
            var urlNoQuery = (index == -1) ? url : url.substring(0, index);
            var data = method + " " + urlNoQuery + " " + time + " " + secret;

            $('dataForEncoding').value = data;

            var sig = SHA1(data);
            var encodedUrl = url + ((index == -1) ? "?" : "&") +
                             "apiKey=" + apiKey + "&sig=" + sig + "&time=" + time;
            if (sessionIdProtected) {
                encodedUrl += "&sessionId=" + sessionId;
            }
            $('encodedUrl').value = encodedUrl;
        } else {
            $('dataForEncoding').value = 'No data for encoding required';
            $('encodedUrl').value = url;
        }
    },
    isAPIKeyValid: function (apiKey) {
        return apiKey != null && apiKey != "";
    },
    isSecretValid: function (secret) {
        return secret != null && secret != "";
    },
    isSessionIdValid: function (sessionId) {
        return sessionId != null && sessionId != "";
    },
    isUrlValid: function (url) {
        return url != null && url != "" && url.indexOf("http://") == 0;
    },
    // TODO fix backspace issue
    updateTime: function () {
        if ($F('apiKeyProtected')) {
            var url = $F('url');
            if (!URLEncoder.isUrlValid($F('url'))) {
                alert("Please provide a valid api key!");
                return;
            }

            var timeUrl = null;
            var index = url.indexOf("/", url.indexOf("http://") + 7);
            if (index != -1)
                index = url.indexOf("/v", index + 1);
            if (index != -1)
                index = url.indexOf("/", index + 2);
            if (index != -1)
                timeUrl = url.substring(0, index + 1);

            if (timeUrl == null) {
                $('time').value = new Date().getTime();
            } else {
                $('time').value = new Date().getTime();
                /*timeUrl += "serverTime";

                new Ajax.Request('httpgateway?apiUrl=' + timeUrl,
                {
                    method:'get',
                    asynchronous: false,
                    onSuccess: function(transport) {
                        $('time').value = transport.responseText;
                    },
                    onFailure: function() {
                        $('time').value = new Date().getTime();
                    }
                });*/
            }
        } else {
            $('time').value = 'No time for encoding required';
        }
    }
};

var RequestHandling = {
    prepareRequestUrl: function (url) {
        url = url.replace(/\?/g, '%3F');
        url = url.replace(/\&/g, '%26');
        return url;
    },
    addOrUpdateModeOnUrl: function (url) {
        var mode = ($('compatMode').checked ? 'compat' : '');
        url = RequestHandling.updateUrlParameter(url, 'mode', /mode=\w+/, mode);
        if (mode == '') {
            url = url.replace('&mode=', '');
            url = url.replace('?mode=', '');
        }
        return url;
    },
    updateUrlParameter: function(url, paramName, regex, replacement) {
        if (url.indexOf(paramName) == -1) {
            url += (url.indexOf('?') == -1) ? '?' : '&';
            url += paramName + '=' + replacement;
        }
        else {
            url = url.replace(regex, paramName + '=' + replacement);
        }
        return url;
    },
    getXMLResponse: function (transport) {
        var response = transport.responseXML.documentElement;
        if (Prototype.Browser.IE) {
            var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async = false;
            xmlDoc.loadXML(transport.responseText);
            response = xmlDoc.documentElement;
        }
        return response;
    },
    wasRequestSuccessful: function (transport) {
        if ($('compatMode').checked) {
            var text = transport.responseText;
            var regex = /<code>(\d+)<\/code>/;
            var status = null;
            var matches = regex.exec(text);
            if (matches != null && matches.length > 1)
                status = matches[1];
            return (status != null && (status == '200' || status == '201'));
        }
        else {
            return true;
        }
    },
    getErrorText: function (transport) {
        var text = transport.responseText;
        if ($('compatMode').checked || text.indexOf('<error>') != -1) {
            var response = RequestHandling.getXMLResponse(transport);
            if (response.getElementsByTagName('message')[0] != null ||
                response.getElementsByTagName('message')[0].firstChild != null) {
                text = response.getElementsByTagName('message')[0].firstChild.nodeValue;
            }
        }
        return text;
    }
};

var History = {
    storeHistoryLink: function(url) {
        var historyList = $('historyList');
        historyList.options[historyList.options.length] = new Option(url, url);
        historyList.selectedIndex = historyList.options.length - 1;
        Preferences.storePreferenceForOption('historyList');
    },
    storeScrollPositionInHistoryLink: function () {
        var historyList = $('historyList');
        if (historyList.options.length > 0) {
            var historyUrl = historyList.options[historyList.options.length - 1].value;
            if (historyUrl.indexOf('#') == -1) {
                historyUrl += '#' + $('responseScrollBox').scrollTop;
            }
            else {
                historyUrl = historyUrl.replace(/#\d+/, '#' + $('responseScrollBox').scrollTop);
            }
            historyList.options[historyList.options.length - 1] = new Option(historyUrl.substring(0, historyUrl.indexOf('#')), historyUrl);
            historyList.selectedIndex = historyList.options.length - 1;
            Preferences.storePreferenceForOption('historyList');
        }
    },
    loadHistoryLink: function () {
        $('url').value = $F('historyList');
        var historyList = $('historyList');
        historyList.options.length = historyList.selectedIndex + 1;
        ApplicationController.checkSettings($F('url'));
        Preferences.storePreferences();
        Preferences.storePreferenceForOption('historyList');
        ResponseDialogController.loadDataInternal();
    },
    backInHistory: function () {
        var historyList = $('historyList');
        if (historyList.selectedIndex > 0) {
            historyList.selectedIndex = historyList.selectedIndex - 1;
            History.loadHistoryLink();
        }
    }
};

var Preferences = {
    loadPreferences: function () {
        Preferences.loadPreferenceForField('apiKey', '123456789');
        Preferences.loadPreferenceForField('secret', '987654321');
        Preferences.loadPreferenceForField('sessionId', '123');
        Preferences.loadPreferenceForField('userId', '40000');
        Preferences.loadPreferenceForField('shopId', '40000');
        Preferences.loadPreferenceForOption('imageHeight');
        Preferences.loadPreferenceForOption('imageWidth');
        Preferences.loadPreferenceForOption('imageMediaType');
        Preferences.loadPreferenceForCheckbox('fullData', false);
        Preferences.loadPreferenceForOption('limit');
        Preferences.loadPreferenceForCheckbox('synchImageSize', true);
        Preferences.loadPreferenceForCheckbox('compatMode', false);
        Preferences.loadPreferenceForCheckbox('renderList', false);
    },
    storePreferences: function () {
        Preferences.storePreferenceForField('apiKey');
        Preferences.storePreferenceForField('secret');
        Preferences.storePreferenceForField('sessionId');
        Preferences.storePreferenceForField('userId');
        Preferences.storePreferenceForField('shopId');
        Preferences.storePreferenceForOption('localeList');
        Preferences.storePreferenceForOption('imageHeight');
        Preferences.storePreferenceForOption('imageWidth');
        Preferences.storePreferenceForOption('imageMediaType');
        Preferences.storePreferenceForCheckbox('fullData');
        Preferences.storePreferenceForOption('limit');
        Preferences.storePreferenceForOption('entries');
        Preferences.storePreferenceOptions('hostList');
        Preferences.storePreferenceForOption('hostList');
        Preferences.storePreferenceForOption('resourceList');
        Preferences.storePreferenceForOption('resourceMethod');
        Preferences.storePreferenceForCheckbox('synchImageSize');
        Preferences.storePreferenceForCheckbox('compatMode');
        Preferences.storePreferenceForCheckbox('renderList');
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

function SHA1(msg) {
    function rotate_left(n, s) {
        return ( n << s ) | (n >>> (32 - s));
    }

    function lsb_hex(val) {
        var str = "";
        var i;
        var vh;
        var vl;

        for (i = 0; i <= 6; i += 2) {
            vh = (val >>> (i * 4 + 4)) & 0x0f;
            vl = (val >>> (i * 4)) & 0x0f;
            str += vh.toString(16) + vl.toString(16);
        }
        return str;
    }

    function cvt_hex(val) {
        var str = "";
        var i;
        var v;

        for (i = 7; i >= 0; i--) {
            v = (val >>> (i * 4)) & 0x0f;
            str += v.toString(16);
        }
        return str;
    }

    function Utf8Encode(string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    }

    var blockstart;
    var i, j;
    var W = new Array(80);
    var H0 = 0x67452301;
    var H1 = 0xEFCDAB89;
    var H2 = 0x98BADCFE;
    var H3 = 0x10325476;
    var H4 = 0xC3D2E1F0;
    var A, B, C, D, E;
    var temp;

    msg = Utf8Encode(msg);

    var msg_len = msg.length;

    var word_array = new Array();
    for (i = 0; i < msg_len - 3; i += 4) {
        j = msg.charCodeAt(i) << 24 | msg.charCodeAt(i + 1) << 16 |
            msg.charCodeAt(i + 2) << 8 | msg.charCodeAt(i + 3);
        word_array.push(j);
    }

    switch (msg_len % 4) {
        case 0:
            i = 0x080000000;
            break;
        case 1:
            i = msg.charCodeAt(msg_len - 1) << 24 | 0x0800000;
            break;

        case 2:
            i = msg.charCodeAt(msg_len - 2) << 24 | msg.charCodeAt(msg_len - 1) << 16 | 0x08000;
            break;

        case 3:
            i = msg.charCodeAt(msg_len - 3) << 24 | msg.charCodeAt(msg_len - 2) << 16 | msg.charCodeAt(msg_len - 1) << 8 | 0x80;
            break;
    }

    word_array.push(i);

    while ((word_array.length % 16) != 14) word_array.push(0);

    word_array.push(msg_len >>> 29);
    word_array.push((msg_len << 3) & 0x0ffffffff);


    for (blockstart = 0; blockstart < word_array.length; blockstart += 16) {

        for (i = 0; i < 16; i++) W[i] = word_array[blockstart + i];
        for (i = 16; i <= 79; i++) W[i] = rotate_left(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);

        A = H0;
        B = H1;
        C = H2;
        D = H3;
        E = H4;

        for (i = 0; i <= 19; i++) {
            temp = (rotate_left(A, 5) + ((B & C) | (~B & D)) + E + W[i] + 0x5A827999) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B, 30);
            B = A;
            A = temp;
        }

        for (i = 20; i <= 39; i++) {
            temp = (rotate_left(A, 5) + (B ^ C ^ D) + E + W[i] + 0x6ED9EBA1) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B, 30);
            B = A;
            A = temp;
        }

        for (i = 40; i <= 59; i++) {
            temp = (rotate_left(A, 5) + ((B & C) | (B & D) | (C & D)) + E + W[i] + 0x8F1BBCDC) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B, 30);
            B = A;
            A = temp;
        }

        for (i = 60; i <= 79; i++) {
            temp = (rotate_left(A, 5) + (B ^ C ^ D) + E + W[i] + 0xCA62C1D6) & 0x0ffffffff;
            E = D;
            D = C;
            C = rotate_left(B, 30);
            B = A;
            A = temp;
        }

        H0 = (H0 + A) & 0x0ffffffff;
        H1 = (H1 + B) & 0x0ffffffff;
        H2 = (H2 + C) & 0x0ffffffff;
        H3 = (H3 + D) & 0x0ffffffff;
        H4 = (H4 + E) & 0x0ffffffff;

    }

    temp = cvt_hex(H0) + cvt_hex(H1) + cvt_hex(H2) + cvt_hex(H3) + cvt_hex(H4);
    return temp.toLowerCase();
}

//Event.observe(window, 'load', ApplicationController.resizeApp);
Event.observe(window, 'load', function() {
    if (typeof window.event != 'undefined') {
        Event.observe(document.body, 'keydown', function(event) {
            if (Event.element(event).tagName.toUpperCase() != 'INPUT' && Event.element(event).tagName.toUpperCase() != 'TEXTAREA' && event.keyCode == Event.KEY_BACKSPACE) {
                Event.stop(event);
                History.backInHistory();
            }
        });
    } else {
        Event.observe(window, 'keydown', function(event) {
            if (Event.element(event).nodeName.toUpperCase() != 'INPUT' && Event.element(event).nodeName.toUpperCase() != 'TEXTAREA' && event.keyCode == Event.KEY_BACKSPACE) {
                Event.stop(event);
                History.backInHistory();
            }
        });
    }
});
Event.observe(window, 'resize', ApplicationController.resizeApp);