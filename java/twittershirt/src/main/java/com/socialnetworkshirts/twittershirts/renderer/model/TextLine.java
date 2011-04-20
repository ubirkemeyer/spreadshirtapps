package com.socialnetworkshirts.twittershirts.renderer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public class TextLine {
    private List<TSpan> tspans = new ArrayList<TSpan>();

    public TextLine() {
    }

    public List<TSpan> getTspans() {
        return tspans;
    }

    public void setTspans(List<TSpan> tspans) {
        this.tspans = tspans;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextLine textLine = (TextLine) o;

        if (tspans != null ? !tspans.equals(textLine.tspans) : textLine.tspans != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (tspans != null ? tspans.hashCode() : 0);
    }


    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("TextLine{");
        sb.append("tspans=").append(tspans);
        sb.append('}');
        return sb.toString();
    }
}
