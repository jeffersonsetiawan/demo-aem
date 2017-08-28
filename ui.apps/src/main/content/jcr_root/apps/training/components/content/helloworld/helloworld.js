(function () {
    "use strict";

    var CONST = {
        PROP_DEFAULT_TYPE: "defaultType",
        PROP_TYPE: "type",
        DEFAULT_TYPE: "h4",

        PROP_TITLE: "title",
        PROP_PAGE_TITLE: "jcr:title",

        VALID_TYPES: ["h1", "h2", "h3", "h4"]
    };

    var _getValidType = function(type, defaultType) {
        for(var idx = 0; idx < CONST.VALID_TYPES.length; idx ++) {
            if (type == CONST.VALID_TYPES[idx]) {
                return type;
            }
        }

        return defaultType;
    };

    var nodetitle = granite.resource.properties[CONST.PROP_TITLE] || wcm.currentPage.properties[CONST.PROP_PAGE_TITLE];

    var defaultType = CONST.DEFAULT_TYPE;

    var type = _getValidType(granite.resource.properties[CONST.PROP_TYPE], CONST.DEFAULT_TYPE);

    return {
        outputContent: nodetitle,
        type: type
    };
})();