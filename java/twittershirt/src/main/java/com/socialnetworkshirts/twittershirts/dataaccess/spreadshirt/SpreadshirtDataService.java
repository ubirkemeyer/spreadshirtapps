package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt;

import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http.HttpCallCommand;
import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http.HttpCallCommandFactory;
import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http.HttpMethod;
import com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http.HttpUrlConnectionFactory;
import com.socialnetworkshirts.twittershirts.renderer.model.Svg;
import com.socialnetworkshirts.twittershirts.Configuration;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.spreadshirt.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

/**
 * @author mbs
 */
public class SpreadshirtDataService {
    private static final Logger log = LoggerFactory.getLogger(SpreadshirtDataService.class);

    private static final String CACHE_SHOP = "shop";
    private static final String CACHE_CURRENCY = "currency";
    private static final String CACHE_PRODUCT_TYPE = "productType";
    private static final String CACHE_PRINT_TYPE = "printType";

    private static String apiUrl = Configuration.getInstance().getSpreadshirtAPI();
    private static String shopId = Configuration.getInstance().getSpreadshirtShop();
    private static String apiKey = Configuration.getInstance().getSpreadshirtAPIKey();
    private static  String secret = Configuration.getInstance().getSpreadshirtSecret();
    static {
        log.info("Spreadshirt account data is: " + apiKey + " " + secret);   
    }
    private String shopUrl = apiUrl + "shops";
    private String designUrl;
    private String productUrl;
    private String productTypeUrl;
    private String printTypeUrl;
    private String basketsUrl;
    private String currencyUrl;

    private HttpUrlConnectionFactory urlConnectionFactory;
    private HttpCallCommandFactory commandFactory;
    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    private CacheManager cacheManager;
    private Cache shopCache;
    private Cache currencyCache;
    private Cache productTypeCache;
    private Cache printTypeCache;

    private ShopDTO shop;
    private CurrencyDTO currency;

    private static final SpreadshirtDataService instance = new SpreadshirtDataService();

    public static SpreadshirtDataService getInstance() {
        return instance;
    }

    private SpreadshirtDataService() {
        try {
            urlConnectionFactory = new HttpUrlConnectionFactory(apiKey, secret, null);
            commandFactory = new HttpCallCommandFactory(urlConnectionFactory);
            objectFactory = new ObjectFactory();
            jaxbContext = JAXBContext.newInstance("net.spreadshirt.api");

            cacheManager = new CacheManager(this.getClass().getClassLoader().getResourceAsStream("/ehcache.xml"));
            shopCache = cacheManager.getCache(CACHE_SHOP);
            currencyCache = cacheManager.getCache(CACHE_CURRENCY);
            productTypeCache = cacheManager.getCache(CACHE_PRODUCT_TYPE);
            printTypeCache = cacheManager.getCache(CACHE_PRINT_TYPE);

            shop = getShop(shopId);
            currencyUrl = shop.getCurrencies().getHref();
            basketsUrl = shop.getBaskets().getHref();
            designUrl = shop.getDesigns().getHref();
            productUrl = shop.getProducts().getHref();
            productTypeUrl = shop.getProductTypes().getHref();
            printTypeUrl = shop.getPrintTypes().getHref();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ShopDTO getDefaultShop() {
        return shop;
    }

    public ShopDTO getShop(String id)
            throws Exception {
        Element elem = shopCache.get(id);
        if (elem == null) {
            HttpCallCommand command =
                    commandFactory.createJaxbHttpCallCommand(shopUrl + "/" + id, HttpMethod.GET, null);
            command.execute();
            if (command.getStatus() >= 400) {
                throw new Exception("Could fetch shop xml!");
            }
            ShopDTO shop =
                    (ShopDTO) ((JAXBElement) command.getOutput()).getValue();
            shopCache.put(new Element(id, shop));
            return shop;
        }
        return (ShopDTO) elem.getObjectValue();
    }

    public CurrencyDTO getCurrency(String id)
            throws Exception {
        Element elem = currencyCache.get(id);
        if (elem == null) {
            HttpCallCommand command =
                    commandFactory.createJaxbHttpCallCommand(currencyUrl + "/" + id, HttpMethod.GET, null);
            command.execute();
            if (command.getStatus() >= 400) {
                throw new Exception("Could fetch currency xml!");
            }
            CurrencyDTO currency =
                    (CurrencyDTO) ((JAXBElement) command.getOutput()).getValue();
            currencyCache.put(new Element(id, currency));
            return currency;
        }
        return (CurrencyDTO) elem.getObjectValue();
    }

    public ProductTypeDTO getProductType(String id)
            throws Exception {
        Element elem = productTypeCache.get(id);
        if (elem == null) {
            HttpCallCommand getProductTypeCommand =
                    commandFactory.createJaxbHttpCallCommand(productTypeUrl + "/" + id, HttpMethod.GET, null);
            getProductTypeCommand.execute();
            if (getProductTypeCommand.getStatus() >= 400) {
                throw new Exception("Could not create product type xml!");
            }
            ProductTypeDTO productType =
                    (ProductTypeDTO) ((JAXBElement) getProductTypeCommand.getOutput()).getValue();
            productTypeCache.put(new Element(id, productType));
            return productType;
        }
        return (ProductTypeDTO) elem.getObjectValue();

    }

    public PrintTypeDTO getPrintType(String id)
            throws Exception {
        Element elem = printTypeCache.get(id);
        if (elem == null) {
            HttpCallCommand getPrintTypeCommand =
                    commandFactory.createJaxbHttpCallCommand(printTypeUrl + "/" + id, HttpMethod.GET, null);
            getPrintTypeCommand.execute();
            if (getPrintTypeCommand.getStatus() >= 400) {
                throw new Exception("Could not create print type xml!");
            }
            PrintTypeDTO printType =
                    (PrintTypeDTO) ((JAXBElement) getPrintTypeCommand.getOutput()).getValue();
            printTypeCache.put(new Element(id, printType));
            return printType;
        }
        return (PrintTypeDTO) elem.getObjectValue();
    }

    public DesignDTO uploadDesign(Svg svg)
            throws Exception {
        Object obj = jaxbContext.createUnmarshaller()
                .unmarshal(this.getClass().getResourceAsStream("/design.xml"));
        // create design data using xml
        HttpCallCommand createDesignCommand =
                commandFactory.createJaxbHttpCallCommand(designUrl, HttpMethod.POST, null);
        createDesignCommand.setInput(obj);
        createDesignCommand.setApiKeyProtected(true);
        createDesignCommand.execute();
        if (createDesignCommand.getStatus() >= 400) {
            throw new Exception("Could not create design xml!");
        }

        // get created design xml
        HttpCallCommand getDesignCommand =
                commandFactory.createJaxbHttpCallCommand(createDesignCommand.getLocation(), HttpMethod.GET, null);
        getDesignCommand.execute();
        if (createDesignCommand.getStatus() >= 400) {
            throw new Exception("Could not retrieve design xml from " + createDesignCommand.getLocation() + "!");
        }
        DesignDTO design = (DesignDTO) ((JAXBElement) getDesignCommand.getOutput()).getValue();

        // determine upload location
        String uploadUrl = design.getResources().getResource().get(0).getHref();
        uploadUrl = uploadUrl.replace("3128", "2302");

        // upload image
        HttpCallCommand uploadDesignCommand =
                commandFactory.createSvgUploadCommand(uploadUrl, HttpMethod.PUT, null);
        uploadDesignCommand.setInput(svg);
        uploadDesignCommand.setApiKeyProtected(true);
        uploadDesignCommand.execute();
        if (uploadDesignCommand.getStatus() >= 400) {
            log.error(uploadDesignCommand.getErrorMessage());
            throw new Exception("Status above 400 expected but status was " + uploadDesignCommand.getStatus() + "!");
        }

        return design;
    }

    public ProductDTO getEmptyProduct() {
        ProductDTO product = objectFactory.createProductDTO();
        product.setProductType(new Reference());
        product.setAppearance(new Reference());
        product.setConfigurations(new ProductDTO.Configurations());
        ConfigurationDTO configuration = new ConfigurationDTO();
        product.getConfigurations().getConfiguration().add(configuration);
        configuration.setType(ConfigurationType.DESIGN);
        configuration.setPrintArea(new Reference());
        configuration.setPrintType(new Reference());
        configuration.setOffset(new Point());
        configuration.setContent(new ConfigurationContentDTO());
        configuration.getContent().setDpi(25.4);
        configuration.getContent().setUnit(Unit.MM);
        configuration.getContent().setSvg(new net.spreadshirt.api.Svg());
        configuration.getContent().getSvg().setImage(new Image());
        return product;
    }

    public BasketDTO createBasket()
            throws Exception {
        BasketDTO basket = objectFactory.createBasketDTO();
        /*Reference shopRef = objectFactory.createReference();
        shopRef.setId(shop.getId());
        basket.setShop(shopRef);*/
        HttpCallCommand command = commandFactory.createJaxbHttpCallCommand(basketsUrl, HttpMethod.POST, null);
        command.setApiKeyProtected(true);
        command.setInput(objectFactory.createBasket(basket));
        command.execute();
        log.info(command.getLocation());
        log.info("" + command.getStatus());
        log.info(command.getErrorMessage());
        if (command.getStatus() != 201)
            throw new IllegalArgumentException("Could not create Basket!");
        log.info("Basket location is: " + command.getLocation());
        return getBasket(command.getLocation().substring(command.getLocation().lastIndexOf("/") + 1));
    }

    public BasketDTO getBasket(String id)
            throws Exception {
        HttpCallCommand command =
                commandFactory.createJaxbHttpCallCommand(basketsUrl + "/" + id, HttpMethod.GET, null);
        command.setApiKeyProtected(true);
        command.execute();
        if (command.getStatus() != 200)
            throw new IllegalArgumentException("Could not retrieve basket!");
        return (BasketDTO) ((JAXBElement) command.getOutput()).getValue();
    }

    public void updateBasket(BasketDTO basket)
            throws Exception {
        HttpCallCommand command =
                commandFactory.createJaxbHttpCallCommand(basketsUrl + "/" + basket.getId(), HttpMethod.PUT, null);
        command.setApiKeyProtected(true);
        command.setInput(objectFactory.createBasket(basket));
        command.execute();
        if (command.getStatus() != 200)
            throw new IllegalArgumentException("Could not create Basket!");
    }

    public String getCheckoutUrl(String id)
            throws Exception {
        HttpCallCommand command =
                commandFactory.createJaxbHttpCallCommand(basketsUrl + "/" + id + "/checkout", HttpMethod.GET, null);
        command.setApiKeyProtected(true);
        command.execute();
        if (command.getStatus() != 200)
            throw new IllegalArgumentException("Could not retrieve checkout reference!");
        Reference reference = (Reference) ((JAXBElement) command.getOutput()).getValue();
        return reference.getHref();
    }

    public BasketItemDTO createBasketItem(String name, int quantity, String productUrl,
                                          String sizeId, String appearanceId)
            throws Exception {
        BasketItemDTO basketItem = objectFactory.createBasketItemDTO();
        basketItem.setDescription(name);
        basketItem.setQuantity(quantity);
        ElementDTO element = objectFactory.createElementDTO();
        element.setHref(productUrl);
        element.setType("sprd:product");
        ElementDTO.Properties properties = new ElementDTO.Properties();
        ElementPropertyDTO property = objectFactory.createElementPropertyDTO();
        property.setKey("size");
        property.setValue(sizeId);
        properties.getProperty().add(property);
        property = objectFactory.createElementPropertyDTO();
        property.setKey("appearance");
        property.setValue(appearanceId);
        properties.getProperty().add(property);
        element.setProperties(properties);
        basketItem.setElement(element);
        return basketItem;
    }

    public String createProduct(ProductDTO product)
            throws Exception {
        HttpCallCommand createProductCommand =
                commandFactory.createJaxbHttpCallCommand(productUrl, HttpMethod.POST, null);
        // use id from fetched design xml here -> my solution is only a hack
        createProductCommand.setInput(objectFactory.createProduct(product));
        createProductCommand.setApiKeyProtected(true);
        createProductCommand.execute();
        if (createProductCommand.getStatus() >= 400) {
            throw new Exception("Could not create product xml!");
        }
        return createProductCommand.getLocation();
    }
}
