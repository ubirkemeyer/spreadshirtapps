package com.socialnetworkshirts.twittershirts.renderer.converter;

/**
 * @author mbs
 * @version $version$
 */
public class PixelToMMConverter {
    // 3 nachkommastellen
    // should be 72?
    public static double pixelToMM(double value, double dpi) {
        return value * 25.4 / dpi;
    }

    public static int mmToPixel(double value, double dpi) {
        return (int) Math.round(value * dpi / 25.4);
    }
}
