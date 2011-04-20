package com.socialnetworkshirts.twittershirts.data.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 */
public class ProductType {
    private String id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String currencySymbol;
    private List<ProductTypeAppearance> appearances = new ArrayList<ProductTypeAppearance>();
    private List<ProductTypeSize> sizes = new ArrayList<ProductTypeSize>();
    private List<ProductTypeStockState> stockStates = new ArrayList<ProductTypeStockState>();
    private PrintArea printArea;

    public ProductType(String id, String name, String imageUrl, BigDecimal price, String currencySymbol) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.currencySymbol = currencySymbol;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<ProductTypeAppearance> getAppearances() {
        return appearances;
    }

    public void setAppearances(List<ProductTypeAppearance> appearances) {
        this.appearances = appearances;
    }

    public ProductTypeAppearance getAppearance(String id) {
        for (ProductTypeAppearance appearance : appearances) {
            if (appearance.getId().equals(id))
                return appearance;
        }
        return null;
    }

    public List<ProductTypeSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<ProductTypeSize> sizes) {
        this.sizes = sizes;
    }

    public List<ProductTypeStockState> getStockStates() {
        return stockStates;
    }

    public void setStockStates(List<ProductTypeStockState> stockStates) {
        this.stockStates = stockStates;
    }

    public PrintArea getPrintArea() {
        return printArea;
    }

    public void setPrintArea(PrintArea printArea) {
        this.printArea = printArea;
    }
}
