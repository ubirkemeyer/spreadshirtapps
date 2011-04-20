package com.socialnetworkshirts.twittershirts.renderer.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
@XmlRootElement(name = Constants.TAG_TEXT, namespace = Constants.SVG_NAMESPACE_URL)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Text {
    @XmlAttribute
    private double x;
    @XmlAttribute
    private double y;
    @XmlAttribute(name = Constants.ATTRIBUTE_FONT_FAMILY)
    private String fontFamily;
    @XmlAttribute(name = Constants.ATTRIBUTE_FONT_SIZE)
    private double fontSize;
    @XmlAttribute
    private String fill;
    @XmlAttribute
    private String transform;
    @XmlElement(name = Constants.TAG_TSPAN)
    private List<TSpan> tspans = new ArrayList<TSpan>();

    public Text() {
    }

    public Text(double x, double y, String fontFamily, double fontSize) {
        this.x = x;
        this.y = y;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
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

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public List<TSpan> getTspans() {
        return tspans;
    }

    public void setTspans(List<TSpan> tspans) {
        this.tspans = tspans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text = (Text) o;

        if (Double.compare(text.fontSize, fontSize) != 0) return false;
        if (Double.compare(text.x, x) != 0) return false;
        if (Double.compare(text.y, y) != 0) return false;
        if (fill != null ? !fill.equals(text.fill) : text.fill != null) return false;
        if (fontFamily != null ? !fontFamily.equals(text.fontFamily) : text.fontFamily != null)
            return false;
        if (transform != null ? !transform.equals(text.transform) : text.transform != null)
            return false;
        if (tspans != null ? !tspans.equals(text.tspans) : text.tspans != null)
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
        result = 31 * result + (fontFamily != null ? fontFamily.hashCode() : 0);
        temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (fill != null ? fill.hashCode() : 0);
        result = 31 * result + (transform != null ? transform.hashCode() : 0);
        result = 31 * result + (tspans != null ? tspans.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Text");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", fontFamily='").append(fontFamily).append('\'');
        sb.append(", fontSize=").append(fontSize);
        sb.append(", fill='").append(fill).append('\'');
        sb.append(", transform='").append(transform).append('\'');
        sb.append(", tspans=").append(tspans);
        sb.append('}');
        return sb.toString();
    }
}
