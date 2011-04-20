package net.sprd.sampleapps.common.dataaccess;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sprd.sampleapps.common.dataaccess.model.*;
import net.spreadshirt.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author mbs
 */
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    private static final String CACHE_ARTICLES = "articles";
    private static final String CACHE_ARTICLE = "article";
    private static final String CACHE_DESIGNS = "designs";
    private static final String CACHE_PRODUCT_TYPES = "productTypes";
    private static final String CACHE_PRINT_TYPES = "printTypes";
    private static final String CACHE_FONT_FAMILIES = "fontFamilies";
    private static final String CACHE_CURRENCIES = "currencies";
    private static final String TYPE_SPRD_ARTICLE = "sprd:article";
    private static final String TYPE_PARAM_SIZE = "size";
    private static final String TYPE_PARAM_APPEARANCE = "appearance";

    private static final DataService instance = new DataService();

    private CacheManager cacheManager;
    private Cache articlesCache;
    private Cache articleCache;
    private Cache designsCache;
    private Cache productTypesCache;
    private Cache printTypesCache;
    private Cache fontFamiliesCache;
    private Cache currenciesCache;
    private SpreadshirtAPI api = SpreadshirtAPI.getInstance();
    private ObjectFactory objectFactory = new ObjectFactory();

    public static DataService getInstance() {
        return instance;
    }

    private DataService() {

    }

    public void init() {
        cacheManager = new CacheManager(this.getClass().getClassLoader().getResourceAsStream("/ehcache.xml"));
        articlesCache = cacheManager.getCache(CACHE_ARTICLES);
        articleCache = cacheManager.getCache(CACHE_ARTICLE);
        designsCache = cacheManager.getCache(CACHE_DESIGNS);
        productTypesCache = cacheManager.getCache(CACHE_PRODUCT_TYPES);
        printTypesCache = cacheManager.getCache(CACHE_PRINT_TYPES);
        fontFamiliesCache = cacheManager.getCache(CACHE_FONT_FAMILIES);
        currenciesCache = cacheManager.getCache(CACHE_CURRENCIES);
    }

    public void destroy() {
        cacheManager.removeCache(CACHE_DESIGNS);
        cacheManager.removeCache(CACHE_ARTICLES);
        cacheManager.removeCache(CACHE_ARTICLE);
        cacheManager.removeCache(CACHE_PRODUCT_TYPES);
        cacheManager.removeCache(CACHE_PRINT_TYPES);
        cacheManager.removeCache(CACHE_FONT_FAMILIES);
        cacheManager.removeCache(CACHE_CURRENCIES);
        cacheManager.shutdown();
    }

    public Article getArticle(String articleId) {
        Article myArticle = null;
        Element elem = articleCache.get(articleId);
        if (elem == null) {
            ArticleDTO article = api.getArticle(articleId);
            ShopDTO shop = api.getShop();
            // shop articles have all the same currency
            CurrencyDTO currency = api.getShopCurrency();
            myArticle = createArticle(article, shop, currency);
            if (myArticle != null)
                articleCache.put(new Element(myArticle.getId(), myArticle));
        } else {
            myArticle = (Article) elem.getValue();
        }
        if (myArticle.getConfigurations() == null) {
            Map<String, PrintTypeDTO> printTypes = getPrintTypes();
            Set<PrintType> usedPrintTypes = new HashSet<PrintType>();
            List<Article.Configuration> configurations = new ArrayList<Article.Configuration>();
            ProductDTO product = api.getProduct(myArticle.getProductId());
            for (ConfigurationDTO configuration : product.getConfigurations().getConfiguration()) {
                PrintTypeDTO printType = printTypes.get(configuration.getPrintType().getId());
                PrintType myPrintType = new PrintType(printType.getId(), printType.getName(), printType.getDescription());
                if (configuration.getType().equals(ConfigurationType.DESIGN)) {
                    Article.Configuration myConfiguration = new Article.Configuration(configuration.getId(), "design",
                            configuration.getResources().getResource().get(0).getHref() + ",width=50,height=50",
                            "" + Math.round(Double.valueOf(configuration.getContent().getSvg().getImage().getWidth())),
                            "" + Math.round(Double.valueOf(configuration.getContent().getSvg().getImage().getHeight())),
                            configuration.getDesigns().getDesign().get(0).getId(),
                            myPrintType);

                    String colorIdsString = configuration.getContent().getSvg().getImage().getPrintColorIds();
                    if (colorIdsString != null && !colorIdsString.trim().isEmpty()) {
                        for (String id : colorIdsString.split(" ")) {
                            for (PrintColorDTO printColor : printType.getColors().getColor()) {
                                if (printColor.getId().equals(id)) {
                                    myConfiguration.getPrintColors().add(
                                            new PrintType.PrintColor(printColor.getId(), printColor.getName()));
                                }
                            }
                        }
                    }

                    configurations.add(myConfiguration);
                } else if (configuration.getType().equals(ConfigurationType.TEXT)) {
                    Article.Configuration myConfiguration = new Article.Configuration(configuration.getId(), "text",
                            configuration.getResources().getResource().get(0).getHref() + ",width=50,height=50",
                            "" + Math.round(Double.valueOf(configuration.getContent().getSvg().getText().getWidth())),
                            "" + Math.round(Double.valueOf(configuration.getContent().getSvg().getText().getHeight())),
                            "",
                            new HashSet<FontFamily>(),
                            myPrintType);

                    String colorIdsString = configuration.getContent().getSvg().getText().getPrintColorId();
                    for (Serializable s : configuration.getContent().getSvg().getText().getContent()) {
                        if (s instanceof Tspan) {
                            colorIdsString += " " + ((Tspan) s).getPrintColorId();
                        }
                    }

                    if (colorIdsString != null && !colorIdsString.trim().isEmpty()) {
                        for (String id : colorIdsString.split(" ")) {
                            for (PrintColorDTO printColor : printType.getColors().getColor()) {
                                if (printColor.getId().equals(id)) {
                                    myConfiguration.getPrintColors().add(
                                            new PrintType.PrintColor(printColor.getId(), printColor.getName()));
                                }
                            }
                        }
                    }

                    String text = "";
                    for (Serializable s : configuration.getContent().getSvg().getText().getContent()) {
                        if (s instanceof Tspan) {
                            for (Serializable s1 : ((Tspan) s).getContent()) {
                                if (s1 instanceof String) {
                                    text += " " + s1;
                                }
                            }
                        } else if (s instanceof String) {
                            text += s;
                        }
                    }
                    myConfiguration.setText(text);

                    Map<String, FontFamilyDTO> fontFamilies = getFontFamilies();
                    for (Reference ref : configuration.getFontFamilies().getFontFamily()) {
                        FontFamilyDTO fontFamily = fontFamilies.get(ref.getId());
                        myConfiguration.getFontFamilies().add(new FontFamily(fontFamily.getId(), fontFamily.getName()));
                    }

                    configurations.add(myConfiguration);
                }
                usedPrintTypes.add(myPrintType);
            }
            myArticle.setUsedPrintTypes(new ArrayList<PrintType>(usedPrintTypes));
            myArticle.setConfigurations(configurations);
        }
        return myArticle;
    }

    public List<Article> getArticles() {
        long start = System.currentTimeMillis();
        Element elem = articlesCache.get("pageOne");
        if (elem == null) {
            List<Article> myArticles = new ArrayList<Article>();
            ArticleDTOList articles = api.getArticles();
            ShopDTO shop = api.getShop();
            // shop articles have all the same currency
            CurrencyDTO currency = api.getShopCurrency();
            for (ArticleDTO article : articles.getArticle()) {
                Article myArticle = createArticle(article, shop, currency);
                if (myArticle != null) {
                    myArticles.add(myArticle);
                    articleCache.put(new Element(myArticle.getId(), myArticle));
                }
            }
            articlesCache.put(new Element("pageOne", myArticles));
            long end = System.currentTimeMillis();
            System.out.println("time: " + (end - start) + "s");
            return myArticles;
        }
        return (List<Article>) elem.getValue();
    }

    private Article createArticle(ArticleDTO article, ShopDTO shop, CurrencyDTO currency) {
        Article myArticle = null;
        ProductDTO product = article.getProduct();
        ProductTypeDTO productType = getProductTypes().get(product.getProductType().getId());
        if (productType != null &&
                productType.getStockStates().getStockState().size() > 0) {
            String defaultAppearanceId =
                    productType.getAppearances().getAppearance().get(0).getId();
            String defaultSizeId =
                    productType.getSizes().getSize().get(0).getId();

            String baseImageUrl = article.getResources().getResource().get(0).getHref();
            int index = baseImageUrl.indexOf("?");
            if (index == -1)
                index = baseImageUrl.indexOf(",");
            if (index != -1)
                baseImageUrl = baseImageUrl.substring(0, index);
            String previewImageUrl = baseImageUrl + ",width=190,height=190,appearanceId=" + defaultAppearanceId;
            String previewCompositionUrl = previewImageUrl.replaceAll("products", "compositions");
            String imageUrl = baseImageUrl + ",width=400,height=400,appearanceId=" + defaultAppearanceId;
            String compositionUrl = imageUrl.replaceAll("products", "compositions");

            String typePhotoUrl = null;
            String typeSizeUrl = null;
            for (Resource resource : productType.getResources().getResource()) {
                if (resource.getType().equals(ResourceType.DETAIL)) {
                    typePhotoUrl = resource.getHref() + ",width=560,height=150";
                } else if (resource.getType().equals(ResourceType.SIZE)) {
                    typeSizeUrl = resource.getHref();
                }
            }

            myArticle = new Article(article.getId(), article.getProduct().getId(),
                    article.getName() == null || article.getName().trim().isEmpty()
                            ? productType.getName() : article.getName(),
                    shop.getArticles().getHref() + "/" + article.getId(),
                    previewImageUrl, previewCompositionUrl,
                    imageUrl, compositionUrl,
                    article.getPrice().getVatIncluded().toString(),
                    currency.getId(),
                    currency.getSymbol(),
                    productType.getName(),
                    productType.getDescription(),
                    typePhotoUrl, typeSizeUrl,
                    defaultAppearanceId, defaultSizeId);
            for (ProductTypeSizeDTO size : productType.getSizes().getSize()) {
                myArticle.getSizes().add(new Article.Size(size.getId(), size.getName()));
            }
            for (ProductTypeAppearanceDTO appearance : productType.getAppearances().getAppearance()) {
                myArticle.getAppearances().add(
                        new Article.Appearance(appearance.getId(), appearance.getName(),
                                // needs to be fixed
                                "http://image.spreadshirt.net/image-server/v1/appearances/" + appearance.getId()));
            }
            for (ProductTypeStockStateDTO stockState : productType.getStockStates().getStockState()) {
                Article.StockStateKey key =
                        new Article.StockStateKey(stockState.getAppearance().getId(),
                                stockState.getSize().getId());
                myArticle.getStockStates().put(key, new Article.StockState(key, stockState.isAvailable()));
            }

            for (ProductTypeViewDTO view : productType.getViews().getView()) {
                baseImageUrl = baseImageUrl.substring(0, baseImageUrl.lastIndexOf("/") + 1) + view.getId();
                previewImageUrl = baseImageUrl + ",width=50,height=50,appearanceId=" + defaultAppearanceId;
                imageUrl = baseImageUrl + ",width=400,height=400,appearanceId=" + defaultAppearanceId;
                compositionUrl = imageUrl.replaceAll("products", "compositions");
                myArticle.getViews().add(new Article.View(view.getId(), view.getName(),
                        previewImageUrl, imageUrl, compositionUrl));
            }
        }
        return myArticle;
    }

    public List<Design> getDesigns() {
        Element elem = designsCache.get("pageOne");
        if (elem == null) {
            List<Design> myDesigns = new ArrayList<Design>();
            DesignDTOList articles = api.getDesigns();
            Map<String, CurrencyDTO> currencies = getCurrencies();
            for (DesignDTO designDTO : articles.getDesign()) {
                String imageUrl = designDTO.getResources().getResource().get(0).getHref();
                int index = imageUrl.indexOf("?");
                if (index == -1)
                    index = imageUrl.indexOf(",");
                if (index != -1)
                    imageUrl = imageUrl.substring(0, index);
                imageUrl = imageUrl + ",width=200,height=200";
                CurrencyDTO currency = currencies.get(designDTO.getPrice().getCurrency().getId());
                String name = (designDTO.getName().length() > 15)
                        ? designDTO.getName().substring(0, 15) + "..." : designDTO.getName();
                String description = (designDTO.getDescription().length() > 100)
                        ? designDTO.getDescription().substring(0, 100) + "..." : designDTO.getDescription();
                myDesigns.add(new Design(designDTO.getId(), name, description, null, imageUrl,
                        designDTO.getPrice().getVatIncluded().toString(),
                        currency.getId(), currency.getSymbol()));
            }
            designsCache.put(new Element("pageOne", myDesigns));
            return myDesigns;
        }
        return (List<Design>) elem.getValue();
    }

    public Map<String, ProductTypeDTO> getProductTypes() {
        Element elem = productTypesCache.get("pageOne");
        if (elem == null) {
            Map<String, ProductTypeDTO> myProductTypes = new HashMap<String, ProductTypeDTO>();
            ProductTypeDTOList productTypes = api.getProductTypes();
            for (ProductTypeDTO productType : productTypes.getProductType()) {
                myProductTypes.put(productType.getId(), productType);
            }
            productTypesCache.put(new Element("pageOne", myProductTypes));
            return myProductTypes;
        }
        return (Map<String, ProductTypeDTO>) elem.getValue();
    }

    public Map<String, PrintTypeDTO> getPrintTypes() {
        Element elem = printTypesCache.get("pageOne");
        if (elem == null) {
            Map<String, PrintTypeDTO> myPrintTypes = new HashMap<String, PrintTypeDTO>();
            PrintTypeDTOList printTypes = api.getPrintTypes();
            for (PrintTypeDTO printType : printTypes.getPrintType()) {
                myPrintTypes.put(printType.getId(), printType);
            }
            printTypesCache.put(new Element("pageOne", myPrintTypes));
            return myPrintTypes;
        }
        return (Map<String, PrintTypeDTO>) elem.getValue();
    }

    public Map<String, FontFamilyDTO> getFontFamilies() {
        Element elem = fontFamiliesCache.get("pageOne");
        if (elem == null) {
            Map<String, FontFamilyDTO> myFontFamilies = new HashMap<String, FontFamilyDTO>();
            FontFamilyDTOList fontFamilies = api.getFontFamilies();
            for (FontFamilyDTO fontFamily : fontFamilies.getFontFamily()) {
                myFontFamilies.put(fontFamily.getId(), fontFamily);
            }
            fontFamiliesCache.put(new Element("pageOne", myFontFamilies));
            return myFontFamilies;
        }
        return (Map<String, FontFamilyDTO>) elem.getValue();
    }

    public Map<String, CurrencyDTO> getCurrencies() {
        Element elem = currenciesCache.get("pageOne");
        if (elem == null) {
            Map<String, CurrencyDTO> myCurrencies = new HashMap<String, CurrencyDTO>();
            CurrencyDTOList currencies = api.getCurrencies();
            for (CurrencyDTO currency : currencies.getCurrency()) {
                myCurrencies.put(currency.getId(), currency);
            }
            currenciesCache.put(new Element("pageOne", myCurrencies));
            return myCurrencies;
        }
        return (Map<String, CurrencyDTO>) elem.getValue();
    }


    public Basket getBasket(String basketId, boolean resolveItems) {
        BasketDTO dto = api.getBasket(basketId);
        if (dto == null)
            return null;
        else {
            Basket basket = new Basket();
            basket.setId(dto.getId());

            CurrencyDTO currency = null;
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal vat = BigDecimal.ZERO;
            int noItems = 0;

            for (BasketItemDTO itemDTO : dto.getBasketItems().getBasketItem()) {
                total = total.add(itemDTO.getPrice().getVatIncluded().multiply(new BigDecimal(itemDTO.getQuantity())));
                vat = vat.add(itemDTO.getPrice().getVat().multiply(new BigDecimal(itemDTO.getQuantity())));
                noItems += 1 * itemDTO.getQuantity();
                if (currency == null) {
                    currency = getCurrencies().get(itemDTO.getPrice().getCurrency().getId());
                }
                Basket.BasketItem basketItem = new Basket.BasketItem(itemDTO.getId(),
                        itemDTO.getDescription(), "" + itemDTO.getQuantity(),
                        "" + itemDTO.getPrice().getVatIncluded().doubleValue(),
                        "" + itemDTO.getPrice().getVatIncluded().multiply(new BigDecimal(itemDTO.getQuantity())).doubleValue(),
                        currency == null ? "" : currency.getSymbol());
                if (resolveItems) {
                    basketItem.setArticle(getArticle(itemDTO.getElement().getId()));
                    String sizeId = "";
                    String appearanceId = "";
                    for (ElementPropertyDTO property : itemDTO.getElement().getProperties().getProperty()) {
                        if (property.getKey().equals(TYPE_PARAM_SIZE)) {
                            sizeId = property.getValue();
                        } else if (property.getKey().equals(TYPE_PARAM_APPEARANCE)) {
                            appearanceId = property.getValue();
                        }
                    }
                    for (Article.Size size : basketItem.getArticle().getSizes()) {
                        if (size.getId().equals(sizeId)) {
                            basketItem.setSize(size);
                        }
                    }
                    for (Article.Appearance appearance : basketItem.getArticle().getAppearances()) {
                        if (appearance.getId().equals(appearanceId)) {
                            basketItem.setAppearance(appearance);
                        }
                    }
                }
                basket.getBasketItems().add(basketItem);
            }

            basket.setNoItems("" + noItems);
            basket.setTotal("" + total.doubleValue());
            basket.setVat("" + vat.doubleValue());
            basket.setCurrencySymbol(currency == null ? "" : currency.getSymbol());
            return basket;
        }
    }

    public void removeBasketItem(String basketId, String basketItemId) {
        log.info("Removing basket item " + basketItemId + " from basket " + basketId + " ...");
        BasketDTO basket = api.getBasket(basketId);
        if (basket != null) {
            Iterator<BasketItemDTO> iter = basket.getBasketItems().getBasketItem().iterator();
            while (iter.hasNext()) {
                BasketItemDTO basketItem = iter.next();
                log.info("Checking basket item " + basketItemId);
                if (basketItem.getId().equals(basketItemId)) {
                    iter.remove();
                    break;
                }
            }
        }
        api.updateBasket(basket);
    }

    public String checkout(String articleId, String appearanceId, String sizeId,
                           String quantity, String basketId) {
        ArticleDTO article = api.getArticle(articleId);

        BasketDTO basket = null;
        if (basketId != null) {
            log.info("Looking up existing basket " + basketId + " ...");
            basket = api.getBasket(basketId);
        }
        if (basketId == null || basket == null) {
            log.info("Creating new basket ...");
            basket = api.createBasket();
        }

        log.info("Basket is: " + basket.getId());

        int actualQuantity = Integer.parseInt(quantity);
        if (actualQuantity < 1)
            actualQuantity = 1;

        BasketItemDTO basketItem = null;

        for (BasketItemDTO iterBasketItem : basket.getBasketItems().getBasketItem()) {
            if (iterBasketItem.getElement().getId().equals(articleId)) {
                for (ElementPropertyDTO property : iterBasketItem.getElement().getProperties().getProperty()) {
                    if (property.getKey().equals(TYPE_PARAM_SIZE) && property.getValue().equals(sizeId)) {
                        for (ElementPropertyDTO property1 : iterBasketItem.getElement().getProperties().getProperty()) {
                            if (property1.getKey().equals(TYPE_PARAM_APPEARANCE) && property1.getValue().equals(appearanceId)) {
                                basketItem = iterBasketItem;
                            }
                        }
                    }
                }
            }
        }

        if (basketItem == null) {
            basketItem = objectFactory.createBasketItemDTO();
            basketItem.setDescription("the description");
            basketItem.setQuantity(actualQuantity);
            ElementPrice price = objectFactory.createElementPrice();
            Reference currency = objectFactory.createReference();
            currency.setId(article.getPrice().getCurrency().getId());
            price.setCurrency(currency);
            price.setVatIncluded(article.getPrice().getVatIncluded());
            basketItem.setPrice(price);
            ElementDTO element = objectFactory.createElementDTO();
            element.setId(article.getId());
            element.setHref(api.getShop().getArticles().getHref() + "/" + articleId);
            element.setType(TYPE_SPRD_ARTICLE);
            ElementDTO.Properties properties = new ElementDTO.Properties();
            ElementPropertyDTO property = objectFactory.createElementPropertyDTO();
            property.setKey(TYPE_PARAM_SIZE);
            property.setValue(sizeId);
            properties.getProperty().add(property);
            property = objectFactory.createElementPropertyDTO();
            property.setKey(TYPE_PARAM_APPEARANCE);
            property.setValue(appearanceId);
            properties.getProperty().add(property);
            element.setProperties(properties);
            basketItem.setElement(element);
            basket.getBasketItems().getBasketItem().add(basketItem);
        } else {
            basketItem.setQuantity(basketItem.getQuantity() + actualQuantity);
        }

        api.updateBasket(basket);
        return basket.getId();
    }

    public String getCheckoutUrl(String basketId) {
        String checkoutUrl = api.getBasketCheckoutUrl(basketId);
        log.info("Basket checkout url is: " + checkoutUrl);
        return checkoutUrl;
    }
}
