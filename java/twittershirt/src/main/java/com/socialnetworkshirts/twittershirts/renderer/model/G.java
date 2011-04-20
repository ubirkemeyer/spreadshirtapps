package com.socialnetworkshirts.twittershirts.renderer.model;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author mbs
 */
@XmlRootElement(name = Constants.TAG_G, namespace = Constants.SVG_NAMESPACE_URL)
@XmlAccessorType(value = XmlAccessType.FIELD)
public class G {
    @XmlElement(name = "image")
    private List<Image> images = new ArrayList<Image>();
    @XmlElement
    private Text text;
     @XmlAttribute
    private String transform;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        G g = (G) o;

        if (images != null ? !images.equals(g.images) : g.images != null) return false;
        if (text != null ? !text.equals(g.text) : g.text != null) return false;
        if (transform != null ? !transform.equals(g.transform) : g.transform != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = images != null ? images.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (transform != null ? transform.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("G");
        sb.append("{images=").append(images);
        sb.append(", text=").append(text);
        sb.append(", transform='").append(transform).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
