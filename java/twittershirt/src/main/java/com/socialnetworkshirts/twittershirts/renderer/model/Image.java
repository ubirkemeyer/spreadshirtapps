package com.socialnetworkshirts.twittershirts.renderer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mbs
 */
@XmlRootElement(name = Constants.TAG_IMAGE, namespace = Constants.SVG_NAMESPACE_URL)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Image {
    @XmlAttribute
    private double x;
    @XmlAttribute
    private double y;
    @XmlAttribute
    private double width;
    @XmlAttribute
    private double height;
    @XmlAttribute(namespace = Constants.XLINK_NAMESPACE_URL)
    private String href;

    public Image() {
    }

    public Image(double x, double y, double width, double height, String href) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.href = href;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (Double.compare(image.height, height) != 0) return false;
        if (Double.compare(image.width, width) != 0) return false;
        if (Double.compare(image.x, x) != 0) return false;
        if (Double.compare(image.y, y) != 0) return false;
        if (href != null ? !href.equals(image.href) : image.href != null)
            return false;

        return true;
    }

    @Override
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
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Image");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", href='").append(href).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
