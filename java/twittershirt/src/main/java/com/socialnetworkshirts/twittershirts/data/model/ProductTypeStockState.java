package com.socialnetworkshirts.twittershirts.data.model;

/**
 * @author mbs
 */
public class ProductTypeStockState {
    private String appearanceId;
    private String sizeId;
    private boolean available;

    public ProductTypeStockState(String appearanceId, String sizeId, boolean available) {
        this.appearanceId = appearanceId;
        this.sizeId = sizeId;
        this.available = available;
    }

    public String getAppearanceId() {
        return appearanceId;
    }

    public void setAppearanceId(String appearanceId) {
        this.appearanceId = appearanceId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
