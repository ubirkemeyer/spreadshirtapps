package com.socialnetworkshirts.twittershirts.data;

import com.socialnetworkshirts.twittershirts.data.model.TwitterUser;
import com.socialnetworkshirts.twittershirts.renderer.TagCloudRenderer;
import com.socialnetworkshirts.twittershirts.renderer.TextRenderer;
import com.socialnetworkshirts.twittershirts.renderer.converter.PixelToMMConverter;
import com.socialnetworkshirts.twittershirts.renderer.model.*;
import com.socialnetworkshirts.twittershirts.*;

import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 */
class SvgFactory {
    private TagCloudRenderer tagCloudRenderer = null;
    private TextRenderer textRenderer = null;

    public SvgFactory() {
        tagCloudRenderer = new TagCloudRenderer();
        textRenderer = new TextRenderer();
    }

    public Svg createSvgWithImageCloud(long width, TwitterUser user, double dpi, String fontColor, List<String> blacklistedUrls) {
        Svg svg = new Svg(PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(330, dpi));
        svg.setG(new G());
        Text text = new Text(0, 0, "Arial", 46);
        Rectangle2D renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(0, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(35, dpi));
        textRenderer.renderText(MessageFormat.format("{0} people follow us", user.getFollowersCount()),
                text, renderArea, 108, TextOrientation.CENTER, TextOrientation.BOTTOM);

        renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(37, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(280, dpi));

        double posX = renderArea.getX();
        double posY = renderArea.getY();
        double imageSize =
                PixelToMMConverter.mmToPixel(PixelToMMConverter.pixelToMM(com.socialnetworkshirts.twittershirts.Constants.TWITTER_IMAGE_SIZE, 72), dpi);
        for (TwitterUser follower : user.getFollowers()) {
            if (!blacklistedUrls.contains(follower.getProfileImageUrl()) &&
                    !follower.getProfileImageUrl().endsWith("bmp")) {
                svg.getG().getImages().add(new Image(posX, posY, imageSize, imageSize, follower.getProfileImageUrl()));
                if ((posX + 2*imageSize) < renderArea.getWidth()) {
                    posX += imageSize;
                } else {
                    posX = renderArea.getX();
                    if (posY + imageSize < renderArea.getHeight()) {
                        posY += imageSize;
                    } else {
                        break;
                    }
                }
            }
        }

        renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(290, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(55, dpi));
        textRenderer.renderText(MessageFormat.format("on twitter.com/{0}.", user.getScreenName()),
                text, renderArea, 108, TextOrientation.CENTER, TextOrientation.TOP);
        text.setFill(fontColor);
        svg.getG().setText(text);


        return svg;
    }

    public Svg createSvgWithTextCloud(long width, TwitterUser user, double dpi, String fontColor) {
        List<Tag> tags = transformFollowersToTags(user.getFollowers());
        Svg svg = new Svg(PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(330, dpi));
        Text text = new Text(0, 0, "Arial", 46);
        Rectangle2D renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(0, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(35, dpi));
        textRenderer.renderText(MessageFormat.format("{0} people follow us", user.getFollowersCount()),
                text, renderArea, 108, TextOrientation.CENTER, TextOrientation.BOTTOM);

        renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(40, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(255, dpi));
        tagCloudRenderer.renderTagCloud(tags, text, renderArea);

        renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(285, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(55, dpi));
        textRenderer.renderText(MessageFormat.format("on twitter.com/{0}.", user.getScreenName()),
                text, renderArea, 108, TextOrientation.CENTER, TextOrientation.TOP);
        text.setFill(fontColor);
        svg.setG(new G());
        svg.getG().setText(text);

        return svg;
    }

    public Svg createErrorSvg(long width, double dpi) {
        Svg svg = new Svg(PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(320, dpi));
        Text text = new Text(0, 0, "Arial", 36);
        Rectangle2D renderArea = new Rectangle2D.Double(
                PixelToMMConverter.mmToPixel(0, dpi), PixelToMMConverter.mmToPixel(60, dpi),
                PixelToMMConverter.mmToPixel(width, dpi), PixelToMMConverter.mmToPixel(35, dpi));
        textRenderer.renderText("Not Available",
                text, renderArea, 108, TextOrientation.CENTER, TextOrientation.BOTTOM);
        svg.setG(new G());
        svg.getG().setText(text);

        return svg;
    }

    private List<Tag> transformFollowersToTags(List<TwitterUser> followers) {
        List<Tag> tags = new ArrayList<Tag>();
        for (TwitterUser follower : followers) {
            tags.add(new Tag(follower.getScreenName(), follower.getFollowersCount()));
        }
        return tags;
    }
}
