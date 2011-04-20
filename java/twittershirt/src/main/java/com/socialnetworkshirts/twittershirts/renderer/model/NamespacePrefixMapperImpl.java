package com.socialnetworkshirts.twittershirts.renderer.model;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @author mbs
 */
public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (Constants.SVG_NAMESPACE_URL.equals(namespaceUri))
            return "";
        if (Constants.XLINK_NAMESPACE_URL.equals(namespaceUri))
            return Constants.XLINK_NAMESPACE;
        return suggestion;
    }
}

