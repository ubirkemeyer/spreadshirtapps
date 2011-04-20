package com.socialnetworkshirts.twittershirts.data.model;

import net.spreadshirt.api.Dimension;
import net.spreadshirt.api.Point;

/**
 * @author mbs
 */
public class PrintArea {
    private String id;
    private String viewId;
    private Point viewOffset;
    private Dimension viewSize;
    private Dimension size;

    public PrintArea(String id, String viewId, Point viewOffset, Dimension viewSize, Dimension size) {
        this.id = id;
        this.viewId = viewId;
        this.viewOffset = viewOffset;
        this.viewSize = viewSize;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public Point getViewOffset() {
        return viewOffset;
    }

    public void setViewOffset(Point viewOffset) {
        this.viewOffset = viewOffset;
    }

    public Dimension getViewSize() {
        return viewSize;
    }

    public void setViewSize(Dimension viewSize) {
        this.viewSize = viewSize;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }
}
