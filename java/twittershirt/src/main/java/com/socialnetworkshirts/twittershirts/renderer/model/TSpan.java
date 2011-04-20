package com.socialnetworkshirts.twittershirts.renderer.model;

import javax.xml.bind.annotation.*;

/**
 * @author mbs
 * @version $version$
 */
@XmlRootElement(name = Constants.TAG_TSPAN, namespace = Constants.SVG_NAMESPACE_URL)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TSpan {
    @XmlAttribute
    private double x;
    @XmlAttribute
    private double y;
    @XmlAttribute
    private double width;
    @XmlAttribute
    private double height;
    @XmlAttribute(name = Constants.ATTRIBUTE_FONT_FAMILY)
    private String fontFamily;
    @XmlAttribute(name = Constants.ATTRIBUTE_FONT_SIZE)
    private double fontSize;
    @XmlValue
    private String value;

    public TSpan() {
    }

    public TSpan(double x, double y, double width, double height, String fontFamily, double fontSize, String value) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.value = value;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TSpan tSpan = (TSpan) o;

        if (Double.compare(tSpan.fontSize, fontSize) != 0) return false;
        if (Double.compare(tSpan.height, height) != 0) return false;
        if (Double.compare(tSpan.width, width) != 0) return false;
        if (Double.compare(tSpan.x, x) != 0) return false;
        if (Double.compare(tSpan.y, y) != 0) return false;
        if (fontFamily != null ? !fontFamily.equals(tSpan.fontFamily) : tSpan.fontFamily != null)
            return false;
        if (value != null ? !value.equals(tSpan.value) : tSpan.value != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        long temp;
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = width != +0.0d ? Double.doubleToLongBits(width) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = height != +0.0d ? Double.doubleToLongBits(height) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (fontFamily != null ? fontFamily.hashCode() : 0);
        temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("TSpan{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", fontFamily='").append(fontFamily == null ? "null" : fontFamily).append("'");
        sb.append(", fontSize=").append(fontSize);
        sb.append(", value='").append(value == null ? "null" : value).append("'");
        sb.append('}');
        return sb.toString();
    }
}
