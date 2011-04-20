package com.socialnetworkshirts.twittershirts.data;

import com.socialnetworkshirts.twittershirts.Constants;
import com.socialnetworkshirts.twittershirts.data.model.*;
import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.SpreadshirtDataService;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.TwitterDataService;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions.RetrievalException;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.TwittershirtsDataService;
import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model.ValidPrintType;
import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model.ValidProductType;
import com.socialnetworkshirts.twittershirts.renderer.PNGRenderer;
import com.socialnetworkshirts.twittershirts.renderer.converter.PixelToMMConverter;
import net.spreadshirt.api.*;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.transcoder.TranscoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mbs
 */
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    private static final DataService instance = new DataService();
    private static List<String> blacklistedUrls = new ArrayList<String>();

    private PNGRenderer renderer = new PNGRenderer();
    private SpreadshirtDataService spreadshirtService = SpreadshirtDataService.getInstance();
    private TwittershirtsDataService twitterShirtService = TwittershirtsDataService.getInstance();
    private TwitterDataService twitterService = TwitterDataService.getInstance();
    private SvgFactory svgFactory = new SvgFactory();

    public static DataService getInstance() {
        return instance;
    }

    private DataService() {
    }

    public List<ProductType> getProductTypes() {
        List<ProductType> productTypes = new ArrayList<ProductType>();
        productTypes.add(getProductType(twitterShirtService.getValidProductTypes().get(0).getId()));
        return productTypes;
    }

    public ProductType getProductType(String id) {
        List<ValidProductType> validProductTypes = twitterShirtService.getValidProductTypes();
        ValidProductType validProductType = null;
        for (ValidProductType productType : validProductTypes) {
            if (productType.getId().equals(id))
                validProductType = productType;
        }
        if (validProductType == null)
            return null;

        try {
            ShopDTO shop = spreadshirtService.getDefaultShop();
            CurrencyDTO currency = spreadshirtService.getCurrency(shop.getCurrency().getId());
            ProductTypeDTO productTypeData = spreadshirtService.getProductType(id);
            List<ValidPrintType> validPrintTypes = TwittershirtsDataService.getInstance().getValidPrintTypes();

            ProductType productType = new ProductType(productTypeData.getId(), productTypeData.getName(),
                    productTypeData.getResources().getResource().get(0).getHref(),
                    productTypeData.getPrice().getVatIncluded(), currency.getSymbol());

            for (ProductTypeSizeDTO sizeData : productTypeData.getSizes().getSize()) {
                productType.getSizes().add(new ProductTypeSize(sizeData.getId(), sizeData.getName()));
            }

            for (ProductTypeAppearanceDTO appearanceData : productTypeData.getAppearances().getAppearance()) {
                if (validProductType.getProductTypeAppearances().containsKey(appearanceData.getId())) {
                    ProductTypeAppearance theAppearance = new ProductTypeAppearance(
                            appearanceData.getId(), appearanceData.getName(),
                            validProductType.getProductTypeAppearances().get(appearanceData.getId()).getFontColor(),
                            appearanceData.getResources().getResource().get(0).getHref());
                    String printTypeId = null;
                    for (Reference printTypeRef : appearanceData.getPrintTypes().getPrintType()) {
                        for (ValidPrintType validPrintType : validPrintTypes) {
                            if (printTypeRef.getId().equals(validPrintType.getId())) {
                                printTypeId = printTypeRef.getId();
                                break;
                            }
                        }
                        if (printTypeId != null)
                            break;
                    }
                    if (printTypeId == null) {
                        log.warn("No valid print type found for appearance " + appearanceData.getId() + "!");
                    } else {
                        PrintTypeDTO printType = spreadshirtService.getPrintType(printTypeId);
                        theAppearance.setPrintType(new PrintType(printType.getId(),
                                printType.getPrice().getVatIncluded(), currency.getSymbol(), printType.getDpi()));
                    }
                    productType.getAppearances().add(theAppearance);
                }
            }

            for (ProductTypeStockStateDTO stockState : productTypeData.getStockStates().getStockState()) {
                productType.getStockStates().add(new ProductTypeStockState(stockState.getAppearance().getId(),
                        stockState.getSize().getId(), stockState.isAvailable()));
            }

            ProductTypeViewDTO view = null;
            for (ProductTypeViewDTO viewData : productTypeData.getViews().getView()) {
                if (viewData.getPerspective().equals(Perspective.FRONT)) {
                    view = viewData;
                }
            }
            if (view != null) {
                ProductTypeViewMapDTO viewMap = view.getViewMaps().getViewMap().get(0);
                for (PrintAreaDTO printArea : productTypeData.getPrintAreas().getPrintArea()) {
                    if (printArea.getId().equals(viewMap.getPrintArea().getId())) {
                        productType.setPrintArea(new PrintArea(printArea.getId(),
                                view.getId(), viewMap.getOffset(), view.getSize(),
                                printArea.getBoundary().getSize()));
                    }
                }
            }

            return productType;
        } catch (Exception e) {
            return null;
        }

    }

    public String getDefaultTwitterUserId() {
        // spreadshirt
        return "14099632";
    }

    public TwitterUser getTwitterUser(String id) {
        try {
            User user = twitterService.getUser(id);
            List<User> followers = twitterService.getFollowers(id);
            List<TwitterUser> users = new ArrayList<TwitterUser>();
            for (User follower : followers) {
                users.add(new TwitterUser(follower.getId(), follower.getName(),
                        follower.getScreenName(), follower.getFollowersCount(),
                        Collections.EMPTY_LIST, follower.getProfileImageUrl()));
            }
            return new TwitterUser(user.getId(), user.getName(),
                    user.getScreenName(), user.getFollowersCount(),
                    users, user.getProfileImageUrl());
        } catch (RetrievalException e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.socialnetworkshirts.twittershirts.renderer.model.Svg createProductImage(
            String userId, String productTypeId, String appearanceId, boolean renderProductImage,
            boolean useTwitterUserImages) {
        if (userId == null)
            throw new IllegalArgumentException("Twitter user id is missing!");
        if (productTypeId == null)
            throw new IllegalArgumentException("Product type id is missing!");
        if (appearanceId == null)
            throw new IllegalArgumentException("Product type appearance id is missing!");

        TwitterUser user = DataService.getInstance().getTwitterUser(userId);
        if (user == null)
            throw new IllegalArgumentException("Twitter user not found!");
        ProductType productType = DataService.getInstance().getProductType(productTypeId);
        if (productType == null)
            throw new IllegalArgumentException("Product type not found!");
        ProductTypeAppearance appearance = productType.getAppearance(appearanceId);
        if (appearance == null)
            throw new IllegalArgumentException("Product type appearance not found!");
        if (appearance.getPrintType() == null)
            throw new IllegalArgumentException("Product type appearance print type not found!");
        if (productType.getPrintArea() == null)
            throw new IllegalArgumentException("Product type print area not found!");

        com.socialnetworkshirts.twittershirts.renderer.model.Svg svg =
                createDesign(user, productType, appearance, useTwitterUserImages);

        if (renderProductImage) {
            long viewWidth = PixelToMMConverter.mmToPixel(productType.getPrintArea().getViewSize().getWidth(),
                    appearance.getPrintType().getDpi());
            long viewHeight = PixelToMMConverter.mmToPixel(productType.getPrintArea().getViewSize().getHeight(),
                    appearance.getPrintType().getDpi());
            double scaleX = ((double) com.socialnetworkshirts.twittershirts.Constants.TARGET_VIEW_WIDTH) / ((double) viewWidth);
            double scaleY = ((double) com.socialnetworkshirts.twittershirts.Constants.TARGET_VIEW_HEIGHT) / (double) viewHeight;

            long offsetX = PixelToMMConverter.mmToPixel(productType.getPrintArea().getViewOffset().getX() + Constants.TARGET_OFFSET_X,
                    appearance.getPrintType().getDpi());
            long offsetY = PixelToMMConverter.mmToPixel(productType.getPrintArea().getViewOffset().getY() + Constants.TARGET_OFFSET_Y,
                    appearance.getPrintType().getDpi());

            double translateX = (double) offsetX * scaleX;
            double translateY = (double) offsetY * scaleY;
            svg.getG().setTransform("translate(" + translateX + " , " + translateY + ") " +
                    "scale(" + scaleX + "," + scaleY + ")");
            svg.setHeight(com.socialnetworkshirts.twittershirts.Constants.TARGET_VIEW_HEIGHT);
            svg.setWidth(com.socialnetworkshirts.twittershirts.Constants.TARGET_VIEW_WIDTH);
        }
        return svg;
    }

    public String createProductAndCheckout(String userId, String productTypeId,
                                           String appearanceId, String sizeId, String quantity,
                                           boolean useTwitterUserImages)
            throws Exception {
        if (userId == null)
            throw new IllegalArgumentException("Twitter user id is missing!");
        if (productTypeId == null)
            throw new IllegalArgumentException("Product type id is missing!");
        if (appearanceId == null)
            throw new IllegalArgumentException("Product type appearance id is missing!");
        if (sizeId == null)
            throw new IllegalArgumentException("Product type size id is missing!");
        if (quantity == null)
            throw new IllegalArgumentException("Product type size id is missing!");

        TwitterUser user = DataService.getInstance().getTwitterUser(userId);
        if (user == null)
            throw new IllegalArgumentException("Twitter user not found!");
        ProductType productType = DataService.getInstance().getProductType(productTypeId);
        if (productType == null)
            throw new IllegalArgumentException("Product type not found!");
        ProductTypeAppearance appearance = productType.getAppearance(appearanceId);
        if (appearance == null)
            throw new IllegalArgumentException("Product type appearance not found!");
        if (appearance.getPrintType() == null)
            throw new IllegalArgumentException("Product type appearance print type not found!");
        if (productType.getPrintArea() == null)
            throw new IllegalArgumentException("Product type print area not found!");

        // create svg
        com.socialnetworkshirts.twittershirts.renderer.model.Svg svg =
                createDesign(user, productType, appearance, useTwitterUserImages);

        // upload design
        DesignDTO design = spreadshirtService.uploadDesign(createDesign(user, productType, appearance, useTwitterUserImages));

        // create product
        ProductDTO product = spreadshirtService.getEmptyProduct();
        product.setName("Created by twittershirt.com for twitter user id " + user.getId() + " ...");
        product.getProductType().setId(productType.getId());
        product.getAppearance().setId(appearance.getId());
        ConfigurationDTO configuration = product.getConfigurations().getConfiguration().get(0);
        configuration.getPrintArea().setId(productType.getPrintArea().getId());
        configuration.getPrintType().setId(appearance.getPrintType().getId());
        configuration.getOffset().setX(Constants.TARGET_OFFSET_X);
        configuration.getOffset().setY(Constants.TARGET_OFFSET_Y);
        configuration.getContent().getSvg().getImage().setDesignId(design.getId());
        configuration.getContent().getSvg().getImage().setWidth("" +
                (PixelToMMConverter.pixelToMM(svg.getWidth(), appearance.getPrintType().getDpi()) - 1));
        configuration.getContent().getSvg().getImage().setHeight("" +
                PixelToMMConverter.pixelToMM(svg.getHeight(), appearance.getPrintType().getDpi()));
        String location = spreadshirtService.createProduct(product);
        log.info("Location: " + location);

        // create basket
        BasketDTO basket = spreadshirtService.createBasket();
        log.info("Basket is: " + basket.getId());
        basket.getBasketItems().getBasketItem()
                .add(spreadshirtService.createBasketItem(product.getName(),
                        Integer.parseInt(quantity), location, sizeId,
                        appearance.getId()));
        spreadshirtService.updateBasket(basket);

        // go to checkout
        String checkoutUrl = spreadshirtService.getCheckoutUrl(basket.getId());
        log.info("Checkout Url is: " + checkoutUrl);
        return checkoutUrl;
    }

    private com.socialnetworkshirts.twittershirts.renderer.model.Svg createDesign(
            TwitterUser user, ProductType productType, ProductTypeAppearance appearance,
            boolean useTwitterUserImages) {
        if (user != null) {
            if (useTwitterUserImages) {
                com.socialnetworkshirts.twittershirts.renderer.model.Svg svg = null;
                do {
                    log.info("blacklisted: " + blacklistedUrls);
                    try {
                        double imageSize = com.socialnetworkshirts.twittershirts.Constants.TWITTER_IMAGE_SIZE;
                        double printAreaWidth = PixelToMMConverter.mmToPixel(productType.getPrintArea().getSize().getWidth() - (Constants.TARGET_OFFSET_X * 2), 72);
                        double width = PixelToMMConverter.pixelToMM(printAreaWidth - (printAreaWidth % imageSize), 72);
                        log.info("width " + width + " image size " + imageSize);
                        svg = svgFactory.createSvgWithImageCloud(Math.round(width),
                                user, appearance.getPrintType().getDpi(), appearance.getFontColor(),
                                blacklistedUrls);
                        renderer.renderPNG(svg, new OutputStream() {
                            @Override
                            public void write(int b) throws IOException {
                            }

                            @Override
                            public void write(byte[] b)
                                    throws IOException {

                            }

                            @Override
                            public void write(byte[] b, int off, int len)
                                    throws IOException {
                            }

                            @Override
                            public void flush()
                                    throws IOException {
                            }

                            @Override
                            public void close()
                                    throws IOException {
                            }
                        });
                        return svg;
                    } catch (TranscoderException e) {
                        if (e.getException() != null && e.getException() instanceof BridgeException &&
                                ((BridgeException) e.getException()).getCode().equals("uri.image.broken")) {
                            String message = e.getException().getMessage();
                            int start = message.indexOf("\"");
                            int end = message.indexOf("\"", start + 1);
                            log.info("Problem uri is " + message.substring(start + 1, end));
                            blacklistedUrls.add(message.substring(start + 1, end));
                        }
                    } catch (JAXBException e) {
                        return svg;
                    } catch (IOException e) {
                        return svg;
                    }
                } while (true);
            } else
                return svgFactory.createSvgWithTextCloud(
                        Math.round(productType.getPrintArea().getSize().getWidth()) -
                                (Constants.TARGET_OFFSET_X * 2), user,
                        appearance.getPrintType().getDpi(), appearance.getFontColor());
        } else {
            return svgFactory.createErrorSvg(Math.round(productType.getPrintArea().getSize().getWidth()) -
                    (Constants.TARGET_OFFSET_X * 2),
                    appearance.getPrintType().getDpi());
        }
    }
}