function SearchController(viewName, selfName, entriesPerPage, searchCallback, selectCallback,
                          initSearchCallback, beforeSearchCallback, afterSearchCallback,
                          beforeRenderCallback, afterRenderCallback, customRenderFunction) {
    this.searchFormSuffix = "SearchForm";
    this.searchFieldSuffix = "SearchField";
    this.searchButtonSuffix = "SearchButton";
    this.searchResultSuffix = "SearchResult";
    this.searchPreviousSuffix = "SearchPrevious";
    this.searchNextSuffix = "SearchNext";
    this.searchCountSuffix = "SearchCount";

    this.searchCallback = searchCallback;
    this.selectCallback = selectCallback;
    this.initSearchCallback = initSearchCallback;
    this.beforeSearchCallback = beforeSearchCallback;
    this.afterSearchCallback = afterSearchCallback;
    this.beforeRenderCallback = beforeSearchCallback;
    this.afterRenderCallback = beforeSearchCallback;
    this.customRenderFunction = customRenderFunction;
    this.viewName = viewName;
    this.selfName = selfName;
    this.entriesPerPage = entriesPerPage;
    this.result = null;
    this.currentEntry = null;
    this.executedQuery = null;

    this.conductSearch = function() {
        if (this.initSearchCallback !== undefined && this.initSearchCallback !== null)
            this.initSearchCallback();
        this.conductSearchInternal($('#' + this.viewName + this.searchFieldSuffix).val(), 0, this.entriesPerPage);
    };

    this.refineSearch = function(term, before) {
        if (this.initSearchCallback !== undefined && this.initSearchCallback !== null)
            this.initSearchCallback();
        var newQuery = (before)
                ? term + ' ' + $('#' + this.viewName + 'SearchField').val()
                : $('#' + this.viewName + 'SearchField').val() + ' ' + term ;
        this.conductSearchInternal(newQuery, 0, this.entriesPerPage);
    };

    this.getCurrentQuery = function() {
        return $('#' + this.viewName + 'SearchField').val();
    };

    this.nextPage = function() {
        if (this.result != null) {
            var offset = this.result.offset;
            if ((offset + this.result.limit) <= this.result.count)
                offset += this.result.limit;
            this.conductSearchInternal(this.result.query, offset, this.result.limit);
        }
    };

    this.previousPage = function() {
        if (this.result != null) {
            var offset = this.result.offset;
            if ((offset - this.result.limit) >= 0)
                offset -= this.result.limit;
            this.conductSearchInternal(this.result.query, offset, this.result.limit);
        }
    };

    this.conductSearchInternal = function(query, offset, limit) {
        this.executedQuery = query;
        if (this.beforeSearchCallback !== undefined && this.beforeSearchCallback !== null)
            this.beforeSearchCallback();

        $('#' + this.viewName + 'SearchField').val(query);
        this.currentEntry = null;
        this.result = this.searchCallback(this.executedQuery, offset, limit);

        if (this.afterSearchCallback !== undefined && this.afterSearchCallback !== null)
            this.afterSearchCallback();

        this.renderResult();
    };

    this.renderResult = function() {
        if (this.beforeRenderCallback !== undefined && this.beforeRenderCallback !== null)
            this.beforeRenderCallback();

        if (this.customRenderFunction !== undefined && this.customRenderFunction !== null) {
            this.customRenderFunction();
        } else {
            if (this.hasNextPage())
                $('#' + this.viewName + this.searchNextSuffix).show();
            else
                $('#' + this.viewName + this.searchNextSuffix).hide();
            if (this.hasPreviousPage())
                $('#' + this.viewName + this.searchPreviousSuffix).show();
            else
                $('#' + this.viewName + this.searchPreviousSuffix).hide();
            $('#' + this.viewName + this.searchCountSuffix).html(this.getPage());
            var searchResult = $('#' + this.viewName + this.searchResultSuffix);
            searchResult.html("");
            searchResult.show();
            if (this.result.entries.length > 0) {
                for (var i = 0; i < this.result.entries.length; i++) {
                    var entry = this.result.entries[i];
                    var div = this.itemDiv.replace("#id#", entry.id);
                    div = div.replace("#src#", entry.thumbnailUrl);
                    div = div.replace("#width#", entry.thumbnailWidth);
                    div = div.replace("#height#", entry.thumbnailHeight);
                    div = div.replace("onclick=\"\"", "onclick=\"javascript:" + this.selfName + ".selectEntry('" + entry.id + "');\"");
                    searchResult.append(div);
                }
            } else {
                searchResult.append("Nothing found ...");
            }
        }

        if (this.afterRenderCallback !== undefined && this.afterRenderCallback !== null)
            this.afterRenderCallback();
    };

    this.getPage = function() {
        var current = (this.result.offset + this.result.limit) / this.result.limit;
        var max = ((this.result.count - this.result.count % this.result.limit) /
                   this.result.limit + (this.result.count % this.result.limit > 0 ? 1 : 0));
        if (max == 0)
            return "&nbsp;";
        else
            return "" + current + "/" + max;
    };
    this.hasNextPage = function() {
        var current = (this.result.offset + this.result.limit) / this.result.limit;
        var max = ((this.result.count - this.result.count % this.result.limit) /
                   this.result.limit + (this.result.count % this.result.limit > 0 ? 1 : 0));
        return (current < max);
    };

    this.hasPreviousPage = function() {
        var current = (this.result.offset + this.result.limit) / this.result.limit;
        return current > 1;
    };
    this.selectEntry = function(id) {
        for (var i = 0; i < this.result.entries.length; i++) {
            if (this.result.entries[i].id == id) {
                this.currentEntry = this.result.entries[i];
                this.selectCallback(this.result.entries[i]);
            }
        }
    };
    this.itemDiv = $('#' + this.viewName + this.searchResultSuffix).html();
    $('#' + this.viewName + this.searchResultSuffix).html("");
    this.previousDiv = $('#' + this.viewName + this.searchPreviousSuffix).hide();
    this.nextDiv = $('#' + this.viewName + this.searchNextSuffix).hide();

    var controller = this;
    $('#' + this.viewName + this.searchButtonSuffix).click(function() {
        controller.conductSearch();
    });
    $('#' + this.viewName + this.searchFormSuffix).attr("action", "javascript:" + this.selfName + ".conductSearch();");
    $('#' + this.viewName + this.searchPreviousSuffix).attr("href", "javascript:" + this.selfName + ".previousPage();");
    $('#' + this.viewName + this.searchNextSuffix).attr("href", "javascript:" + this.selfName + ".nextPage();");
}

function SearchResult(query, count, offset, limit, entries) {
    this.query = query;
    this.count = count;
    this.offset = offset;
    this.limit = limit;
    this.entries = entries;
}

function SearchResultEntry(id, name, description, thumbnailUrl, thumbnailWidth, thumbnailHeight, imageUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.thumbnailUrl = thumbnailUrl;
    this.thumbnailWidth = thumbnailWidth;
    this.thumbnailHeight = thumbnailHeight;
    this.imageUrl = imageUrl;
}