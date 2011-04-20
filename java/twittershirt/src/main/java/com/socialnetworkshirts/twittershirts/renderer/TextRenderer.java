package com.socialnetworkshirts.twittershirts.renderer;

import com.socialnetworkshirts.twittershirts.renderer.model.TSpan;
import com.socialnetworkshirts.twittershirts.renderer.model.Text;
import com.socialnetworkshirts.twittershirts.renderer.model.TextOrientation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author mbs
 * @version $version$
 */
public class TextRenderer {
    public void renderText(String value, Text text, Rectangle2D renderArea,
                           int fontSize, TextOrientation xOrientation, TextOrientation yOrientation) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        String fontFamily = text.getFontFamily();
        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
        FontMetrics fm = g.getFontMetrics(font);

        try {
            Rectangle2D rect = fm.getStringBounds(value, g);
            if (rect.getWidth() >= renderArea.getWidth()) {
                while (rect.getWidth() >= renderArea.getWidth()) {
                    fontSize--;
                    font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = g.getFontMetrics(font);
                    rect = fm.getStringBounds(value, g);
                }
            } else {
                while (rect.getWidth() <= renderArea.getWidth()) {
                    fontSize++;
                    font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = g.getFontMetrics(font);
                    rect = fm.getStringBounds(value, g);
                }
            }

            while (rect.getWidth() >= renderArea.getWidth()) {
                fontSize--;
                font = new Font(fontFamily, Font.PLAIN, fontSize);
                fm = g.getFontMetrics(font);
                rect = fm.getStringBounds(value, g);
            }

            double x = renderArea.getX();
            if (xOrientation.equals(TextOrientation.CENTER))
                x += (renderArea.getWidth() - rect.getWidth()) / 2;
            else if (xOrientation.equals(TextOrientation.RIGHT))
                x += (renderArea.getWidth() - rect.getWidth());

            double y = renderArea.getY() + Math.abs(rect.getY());
            if (yOrientation.equals(TextOrientation.BOTTOM))
                y = renderArea.getY() + renderArea.getHeight() - (rect.getHeight() - Math.abs(rect.getY()));

            TSpan tspan = new TSpan(x, y, rect.getWidth(), rect.getHeight(),
                    fontFamily, fontSize, value);
            text.getTspans().add(tspan);
        }
        finally {
            g.dispose();
        }
    }
}
