package com.socialnetworkshirts.twittershirts.renderer.model;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author mbs
 * @version $version$
 */
@XmlRootElement(name = Constants.TAG_SVG, namespace = Constants.SVG_NAMESPACE_URL)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Svg {
    @XmlAttribute
    private String version;
    @XmlAttribute
    private double width;
    @XmlAttribute
    private double height;
    @XmlElement(name = "image")
    private List<Image> images = new ArrayList<Image>();
    @XmlElement
    private Text text;
    @XmlElement
    private G g;

    public Svg() {
    }

    public Svg(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public G getG() {
        return g;
    }

    public void setG(G g) {
        this.g = g;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Svg svg = (Svg) o;

        if (Double.compare(svg.height, height) != 0) return false;
        if (Double.compare(svg.width, width) != 0) return false;
        if (g != null ? !g.equals(svg.g) : svg.g != null) return false;
        if (images != null ? !images.equals(svg.images) : svg.images != null)
            return false;
        if (text != null ? !text.equals(svg.text) : svg.text != null) return false;
        if (version != null ? !version.equals(svg.version) : svg.version != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = version != null ? version.hashCode() : 0;
        temp = width != +0.0d ? Double.doubleToLongBits(width) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = height != +0.0d ? Double.doubleToLongBits(height) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (g != null ? g.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Svg");
        sb.append("{version='").append(version).append('\'');
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", images=").append(images);
        sb.append(", text=").append(text);
        sb.append(", g=").append(g);
        sb.append('}');
        return sb.toString();
    }
}
