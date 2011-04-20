function PicasaLoginController(picasaAPI, successCallback) {
    this.picasaAPI = picasaAPI;
    this.successCallback = successCallback;
    // picasa stuff
    this.initPicasa = function () {
        var token = this.getUrlParameter("token");
        var picasaSessionToken = this.getUrlParameter("picasaSessionToken");

        if (picasaSessionToken !== undefined && picasaSessionToken != null) {
            this.picasaAPI.sessionToken = picasaSessionToken;
            this.successCallback();            
        } else {
            if (token !== undefined && token != null) {
                this.exchangeToken();
            }
        }
    }

    this.exchangeToken = function () {
        var token = this.getUrlParameter("token");
        var picasaSessionToken = this.getUrlParameter("picasaSessionToken");
        if (picasaSessionToken !== undefined && picasaSessionToken != null) {
            $("#picasaToken").html(picasaSessionToken);
        } else {
            if (token !== undefined && token != null) {
                picasaSessionToken = this.picasaAPI.exchangeToken(token);
                if (picasaSessionToken !== undefined && picasaSessionToken != null)
                    window.location.href = window.location.href + "&picasaSessionToken=" + picasaSessionToken;
            }
        }
    };
    this.getUrlParameter = function (name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null)
            return null;
        else
            return results[1];
    };
}


function PicasaAPI(useProxy) {
    // TODO security issue - use https
    this.exchangeToken = function (token) {
        var sessionToken = "";
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl("https://www.google.com/accounts/accounts/AuthSubSessionToken"),
            dataType: "text",
            beforeSend : function(request) {
                request.setRequestHeader("X-Authorization", "AuthSub token=\"" + token + "\"");
            },
            success: function(data) {
                sessionToken = data.split("\n")[0].split("=")[1];
            }
        });
        this.sessionToken = sessionToken;
        return sessionToken;
    };
    this.getAlbums = function(user) {
        var sessionToken = this.sessionToken;
        var albums = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl("http://picasaweb.google.com/data/feed/api/user/" + this.determineUser(user) + "?max-results=1000"),
            dataType: "xml",
            beforeSend : function(request) {
                request.setRequestHeader("X-Authorization", "AuthSub token=\"" + sessionToken + "\"");
            },
            success: function(data) {
                albums = $(data).find("feed");
            }
        });
        return albums;
    };
    this.getUploadedImages = function (user, offset, limit) {
        var sessionToken = this.sessionToken;
        if (offset == 0)
            offset = 1;
        var uploadedImages = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl("http://picasaweb.google.com/data/feed/api/user/" + this.determineUser(user) + "?kind=photo&start-index=" + offset + "&max-results=" + limit),
            dataType: "xml",
            beforeSend : function(request) {
                request.setRequestHeader("X-Authorization", "AuthSub token=\"" + sessionToken + "\"");
            },
            success: function(data) {
                uploadedImages = $(data).find("feed");
            }
        });
        return uploadedImages;
    };
    this.getImages = function(user, album, offset, limit) {
        var sessionToken = this.sessionToken;
        if (offset == 0)
            offset = 1;
        var images = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl("http://picasaweb.google.com/data/feed/api/user/" + this.determineUser(user) + "/albumid/" + album + "?kind=photo&start-index=" + offset + "&max-results=" + limit),
            dataType: "xml",
            beforeSend : function(request) {
                request.setRequestHeader("X-Authorization", "AuthSub token=\"" + sessionToken + "\"");
            },
            success: function(data) {
                images = $(data).find("feed");
            }
        });
        return images;
    };
    this.getCommunityImages = function (query, offset, limit) {
        var sessionToken = this.sessionToken;
        if (offset == 0)
            offset = 1;
        var communityImages = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl("http://picasaweb.google.com/data/feed/api/all?q=" + query + "&kind=photo&start-index=" + offset + "&max-results=" + limit),
            dataType: "xml",
            beforeSend : function(request) {
                request.setRequestHeader("X-Authorization", "AuthSub token=\"" + sessionToken + "\"");
            },
            success: function(data) {
                communityImages = $(data).find("feed");
            }
        });
        return communityImages;

    };
    this.createUrl = function(url) {
        url = url.replace(/\?/g, '%3F');
        url = url.replace(/\&/g, '%26');
        url = url.replace(/\+/g, '%2B');

        return (this.useProxy) ? "proxy.php?url=" + url : url;
    };
    this.determineUser = function(user) {
        if (user === undefined || user === null || user === "")
            return "default";
        else
            return user;
    };
    this.sessionToken = "";
    this.useProxy = (useProxy == null) ? true : useProxy;
}
;