package com.socialnetworkshirts.twittershirts;


import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.TwitterDataService;
import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.SpreadshirtDataService;
import com.socialnetworkshirts.twittershirts.renderer.model.Svg;
import com.socialnetworkshirts.twittershirts.renderer.model.NamespacePrefixMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public class TwitterTester {
    private static final Logger log = LoggerFactory.getLogger(TwitterTester.class);

    public static void main(String[] args)
            throws Exception {
        /*File fileOut = new File("target", "sample.png");
   TwitterDataServiceBean twitterService = new TwitterDataServiceBean();
   twitterService.init();
   SpreadshirtDataServiceBean spreadshirtService = new SpreadshirtDataServiceBean();
   spreadshirtService.init();
   TwitterUser user = twitterService.getUser("14099632");
   List<TwitterUser> followers = twitterService.getFollowers("14099632");
   Svg svg = new SvgFactory().createSvg(user, followers);
   DesignDTO design = spreadshirtService.uploadDesign(svg);
   ProductDTO product = spreadshirtService.getDefaultProduct();
   product.getConfigurations().getConfiguration().get(0).getContent().getSvg().getImage().setDesignId("u" + design.getId());
   String location = spreadshirtService.createProduct(product);
   log.info("Product location is: " + location);*/

        /*TwitterDataService twitterService = new TwitterDataService();
        twitterService.init();
        SpreadshirtDataService spreadshirtService = new SpreadshirtDataService();
        spreadshirtService.init();
        User user = twitterService.getUser("14099632");
        List<User> followers = twitterService.getFollowers("14099632");
        Svg svg = new SvgFactory().createSvg(300, 5, user, followers);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance("com.socialnetworkshirts.twittershirts.renderer.model");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());
        marshaller.marshal(svg, System.out);*/


        /*long viewWidth = PixelToMMConverter.mmToPixel(686.468);
        long viewHeight = PixelToMMConverter.mmToPixel(686.468);
        long targetViewWidth = 600;
        long targetViewHeight = 600;

        double scaleX = (double)targetViewWidth/(double)viewWidth;
        double scaleY = (double)targetViewHeight/(double)viewHeight;
        log.info("scale: " + scaleX + " " + scaleY);

        long offsetX = PixelToMMConverter.mmToPixel(191.236);
        long offsetY = PixelToMMConverter.mmToPixel(68.6487 + 60);
        long printAreaWidth = PixelToMMConverter.mmToPixel(308.9189189148);
        long printAreaHeight = PixelToMMConverter.mmToPixel(566.3513513438);

        double translateX = (double)offsetX*scaleX;
        double translateY = (double)offsetY*scaleY;
        log.info("translateX: " + translateX);
        log.info("translateY: " + translateY); */
    }
}
