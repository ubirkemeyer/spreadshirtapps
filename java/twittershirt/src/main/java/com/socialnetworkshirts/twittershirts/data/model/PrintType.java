package com.socialnetworkshirts.twittershirts.data.model;

import java.math.BigDecimal;

/**
 * @author mbs
 */
public class PrintType {
    private String id;
    private BigDecimal price;
    private String currencySymbol;
    private double dpi;

    public PrintType(String id, BigDecimal price, String currencySymbol, double dpi) {
        this.id = id;
        this.price = price;
        this.currencySymbol = currencySymbol;
        this.dpi = dpi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getDpi() {
        return dpi;
    }

    public void setDpi(double dpi) {
        this.dpi = dpi;
    }
}
