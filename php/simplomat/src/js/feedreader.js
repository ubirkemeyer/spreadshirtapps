function FeedReader(url, useProxy) {
    this.readFeed = function (query, offset, limit) {
        var params = "";
        if (offset !== undefined && offset !== null) {
            params += "?offset=" + offset;
        }
        if (limit !== undefined && limit !== null) {
            params += (params === "") ? "?" : "&";
            params += "limit=" + limit;
        }
        if (query !== undefined && query !== null) {
            params += (params === "") ? "?" : "&";
            params += "query=" + query;
        }


        var feed = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl(url + params),
            dataType: "xml",
            success: function(data) {
                feed = $(data).find("feed");
            }
        });
        return feed;
    };

    this.readRss = function () {
        var feed = null;
        $.ajax({
            type: "GET",
            async: false,
            url: this.createUrl(url),
            dataType: "xml",
            success: function(data) {
                feed = $(data).find("rss");
            }
        });
        return feed;
    };
    
    this.createUrl = function(url) {
        url = url.replace(/\?/g, '%3F');
        url = url.replace(/\&/g, '%26');
        url = url.replace(/\+/g, '%2B');

        return (this.useProxy) ? "proxy.php?url=" + url : url;
    };
    this.url = url;
    this.useProxy = (useProxy == null) ? true : useProxy;
};