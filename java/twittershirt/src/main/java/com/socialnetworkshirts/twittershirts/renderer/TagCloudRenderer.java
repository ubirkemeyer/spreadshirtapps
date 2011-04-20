package com.socialnetworkshirts.twittershirts.renderer;

import com.socialnetworkshirts.twittershirts.renderer.model.TSpan;
import com.socialnetworkshirts.twittershirts.renderer.model.Tag;
import com.socialnetworkshirts.twittershirts.renderer.model.Text;
import com.socialnetworkshirts.twittershirts.renderer.model.TextLine;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public class TagCloudRenderer {
    // y value is baseline value
    // arial min size value 42    
    public void renderTagCloud(List<Tag> tags, Text text, Rectangle2D renderArea) {
        List<TextLine> textLines = new ArrayList<TextLine>();

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        String fontFamily = text.getFontFamily();
        int fontSize = (int) text.getFontSize();
        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
        FontMetrics fm = g.getFontMetrics(font);
        Point2D currentPosition = new Point2D.Double(renderArea.getX(), renderArea.getY());
        TextLine textLine = null;
        double minPadding = fm.getHeight();

        try {
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                String value = tag.getValue();
                Rectangle2D rect = fm.getStringBounds(value, g);
                // create new textline if not exists
                if (i == 0) {
                    textLine = new TextLine();
                    textLines.add(textLine);
                }
                if (currentPosition.getX() + rect.getWidth() + minPadding >= (renderArea.getX() + renderArea.getWidth())) {
                    currentPosition.setLocation(renderArea.getX(),
                            currentPosition.getY() + fm.getHeight() * 1.2);
                    if ((currentPosition.getY() + fm.getHeight()) >= (renderArea.getY() + renderArea.getHeight()))
                        break;
                    textLine = new TextLine();
                    textLines.add(textLine);
                }
                TSpan tspan = new TSpan(currentPosition.getX(),
                        currentPosition.getY() + Math.abs(rect.getY()),
                        rect.getWidth(),
                        rect.getHeight(),
                        fontFamily, fontSize, value);
                currentPosition.setLocation(currentPosition.getX() + rect.getWidth() + minPadding,
                        currentPosition.getY());
                textLine.getTspans().add(tspan);
                text.getTspans().add(tspan);
            }

            for (TextLine line : textLines) {
                if (line.getTspans().size() > 1) {
                    TSpan span = line.getTspans().get(line.getTspans().size() - 1);
                    double additionalXPadding = ((renderArea.getX() + renderArea.getWidth()) - (span.getX() + span.getWidth())) / (double) (line.getTspans().size());
                    for (int i = 0; i < line.getTspans().size(); i++) {
                        TSpan tSpan = line.getTspans().get(i);
                        if (i != 0) {
                            tSpan.setX(tSpan.getX() + ((i + 1) * additionalXPadding));
                        }
                    }
                }
            }
            // remove single entry text line
            if (textLines.get(textLines.size() - 1).getTspans().size() == 1) {
                text.getTspans().remove(textLines.get(textLines.size() - 1).getTspans().get(0));
                textLines.remove(textLines.size() - 1);
            }

        }
        finally {
            g.dispose();
        }
    }
}
