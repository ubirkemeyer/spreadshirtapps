package com.socialnetworkshirts.twittershirts.data.model;

import java.util.List;

/**
 * @author mbs
 */
public class ProductTypeAppearance {
    private String id;
    private String name;
    private String fontColor;
    private String imageUrl;
    private List<String> availableSizes;
    private PrintType printType;

    public ProductTypeAppearance(String id, String name, String fontColor, String imageUrl) {
        this.id = id;
        this.name = name;
        this.fontColor = fontColor;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(List<String> availableSizes) {
        this.availableSizes = availableSizes;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }
}
