package com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mbs
 */
public class ValidProductType {
    private String id;
    private Map<String, ValidProductTypeAppearance> productTypeAppearances =
            new HashMap<String, ValidProductTypeAppearance>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, ValidProductTypeAppearance> getProductTypeAppearances() {
        return productTypeAppearances;
    }

    public void setProductTypeAppearances(Map<String, ValidProductTypeAppearance> productTypeAppearances) {
        this.productTypeAppearances = productTypeAppearances;
    }
}
