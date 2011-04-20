package net.sprd.sampleapps.common.dataaccess.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author mbs
 */
public class Article implements Serializable {
    private String id;
    private String productId;
    private String name;
    private String href;
    private String previewImageUrl;
    private String previewCompositionUrl;
    private String imageUrl;
    private String compositionUrl;
    private String price;
    private String currency;
    private String currencySymbol;
    private String typeName;
    private String typeDescription;
    private String typePhotoUrl;
    private String typeSizeUrl;
    private String defaultAppearanceId;
    private String defaultSizeId;
    private List<Size> sizes = new ArrayList<Size>();
    private List<View> views = new ArrayList<View>();
    private List<Appearance> appearances = new ArrayList<Appearance>();
    private List<Configuration> configurations = null;
    private List<PrintType> usedPrintTypes = null;
    private Map<StockStateKey, StockState> stockStates = new HashMap<StockStateKey, StockState>();

    public Article() {
    }

    public Article(String id, String productId, String name, String href,
                   String previewImageUrl, String previewCompositionUrl,
                   String imageUrl, String compositionUrl,
                   String price, String currency, String currencySymbol,
                   String typeName, String typeDescription,
                   String typePhotoUrl, String typeSizeUrl,
                   String defaultAppearanceId, String defaultSizeId) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.href = href;
        this.previewImageUrl = previewImageUrl;
        this.previewCompositionUrl = previewCompositionUrl;
        this.imageUrl = imageUrl;
        this.compositionUrl = compositionUrl;
        this.price = price;
        this.currency = currency;
        this.currencySymbol = currencySymbol;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
        this.typePhotoUrl = typePhotoUrl;
        this.typeSizeUrl = typeSizeUrl;
        this.defaultAppearanceId = defaultAppearanceId;
        this.defaultSizeId = defaultSizeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCompositionUrl() {
        return compositionUrl;
    }

    public void setCompositionUrl(String compositionUrl) {
        this.compositionUrl = compositionUrl;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public String getPreviewImageUrl(String appearanceId) {
        return previewImageUrl.substring(0, previewImageUrl.lastIndexOf("=") + 1) + appearanceId;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public String getPreviewCompositionUrl() {
        return previewCompositionUrl;
    }

    public void setPreviewCompositionUrl(String previewCompositionUrl) {
        this.previewCompositionUrl = previewCompositionUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypePhotoUrl() {
        return typePhotoUrl;
    }

    public void setTypePhotoUrl(String typePhotoUrl) {
        this.typePhotoUrl = typePhotoUrl;
    }


    public String getTypeSizeUrl() {
        return typeSizeUrl;
    }

    public void setTypeSizeUrl(String typeSizeUrl) {
        this.typeSizeUrl = typeSizeUrl;
    }

    public String getDefaultAppearanceId() {
        return defaultAppearanceId;
    }

    public void setDefaultAppearanceId(String defaultAppearanceId) {
        this.defaultAppearanceId = defaultAppearanceId;
    }

    public String getDefaultSizeId() {
        return defaultSizeId;
    }

    public void setDefaultSizeId(String defaultSizeId) {
        this.defaultSizeId = defaultSizeId;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public List<Appearance> getAppearances() {
        return appearances;
    }

    public void setAppearances(List<Appearance> appearances) {
        this.appearances = appearances;
    }

    public Map<StockStateKey, StockState> getStockStates() {
        return stockStates;
    }

    public void setStockStates(Map<StockStateKey, StockState> stockStates) {
        this.stockStates = stockStates;
    }

    public boolean isAvailable(String appearanceId, String sizeId) {
        Article.StockState stockState = stockStates.get(new StockStateKey(appearanceId, sizeId));
        if (stockState == null)
            return false;
        else
            return stockState.isAvailable();
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public List<PrintType> getUsedPrintTypes() {
        return usedPrintTypes;
    }

    public void setUsedPrintTypes(List<PrintType> usedPrintTypes) {
        this.usedPrintTypes = usedPrintTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (appearances != null ? !appearances.equals(article.appearances) : article.appearances != null)
            return false;
        if (currency != null ? !currency.equals(article.currency) : article.currency != null)
            return false;
        if (currencySymbol != null ? !currencySymbol.equals(article.currencySymbol) : article.currencySymbol != null)
            return false;
        if (defaultAppearanceId != null ? !defaultAppearanceId.equals(article.defaultAppearanceId) : article.defaultAppearanceId != null)
            return false;
        if (defaultSizeId != null ? !defaultSizeId.equals(article.defaultSizeId) : article.defaultSizeId != null)
            return false;
        if (href != null ? !href.equals(article.href) : article.href != null)
            return false;
        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (productId != null ? !productId.equals(article.productId) : article.productId != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(article.imageUrl) : article.imageUrl != null)
            return false;
        if (compositionUrl != null ? !compositionUrl.equals(article.compositionUrl) : article.compositionUrl != null)
            return false;
        if (previewImageUrl != null ? !previewImageUrl.equals(article.previewImageUrl) : article.previewImageUrl != null)
            return false;
        if (previewCompositionUrl != null ? !previewCompositionUrl.equals(article.previewCompositionUrl) : article.previewCompositionUrl != null)
            return false;
        if (name != null ? !name.equals(article.name) : article.name != null)
            return false;
        if (price != null ? !price.equals(article.price) : article.price != null)
            return false;
        if (sizes != null ? !sizes.equals(article.sizes) : article.sizes != null)
            return false;
        if (views != null ? !views.equals(article.views) : article.views != null)
            return false;
        if (stockStates != null ? !stockStates.equals(article.stockStates) : article.stockStates != null)
            return false;
        if (typeName != null ? !typeName.equals(article.typeName) : article.typeName != null)
            return false;
        if (typeDescription != null ? !typeDescription.equals(article.typeDescription) : article.typeDescription != null)
            return false;
        if (typePhotoUrl != null ? !typePhotoUrl.equals(article.typePhotoUrl) : article.typePhotoUrl != null)
            return false;
        if (typeSizeUrl != null ? !typeSizeUrl.equals(article.typeSizeUrl) : article.typeSizeUrl != null)
            return false;
        if (configurations != null ? !configurations.equals(article.configurations) : article.configurations != null)
            return false;
        if (usedPrintTypes != null ? !usedPrintTypes.equals(article.usedPrintTypes) : article.usedPrintTypes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (compositionUrl != null ? compositionUrl.hashCode() : 0);
        result = 31 * result + (previewImageUrl != null ? previewImageUrl.hashCode() : 0);
        result = 31 * result + (previewCompositionUrl != null ? previewCompositionUrl.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (currencySymbol != null ? currencySymbol.hashCode() : 0);
        result = 31 * result + (defaultAppearanceId != null ? defaultAppearanceId.hashCode() : 0);
        result = 31 * result + (defaultSizeId != null ? defaultSizeId.hashCode() : 0);
        result = 31 * result + (sizes != null ? sizes.hashCode() : 0);
        result = 31 * result + (views != null ? views.hashCode() : 0);
        result = 31 * result + (appearances != null ? appearances.hashCode() : 0);
        result = 31 * result + (stockStates != null ? stockStates.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (typeDescription != null ? typeDescription.hashCode() : 0);
        result = 31 * result + (typePhotoUrl != null ? typePhotoUrl.hashCode() : 0);
        result = 31 * result + (typeSizeUrl != null ? typeSizeUrl.hashCode() : 0);
        result = 31 * result + (configurations != null ? configurations.hashCode() : 0);
        result = 31 * result + (usedPrintTypes != null ? usedPrintTypes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Article");
        sb.append("{id='").append(id).append('\'');
        sb.append(", productId='").append(productId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", href='").append(href).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", compositionUrl='").append(compositionUrl).append('\'');
        sb.append(", previewImageUrl='").append(previewImageUrl).append('\'');
        sb.append(", previewCompositionUrl='").append(previewCompositionUrl).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", currencySymbol='").append(currencySymbol).append('\'');
        sb.append(", typeName='").append(typeName).append('\'');
        sb.append(", typeDescription='").append(typeDescription).append('\'');
        sb.append(", typePhotoUrl='").append(typePhotoUrl).append('\'');
        sb.append(", typeSizeUrl='").append(typeSizeUrl).append('\'');
        sb.append(", defaultAppearanceId='").append(defaultAppearanceId).append('\'');
        sb.append(", defaultSizeId='").append(defaultSizeId).append('\'');
        sb.append(", sizes=").append(sizes);
        sb.append(", views=").append(views);
        sb.append(", appearances=").append(appearances);
        sb.append(", stockStates=").append(stockStates);
        sb.append(", configurations=").append(configurations);
        sb.append(", usedPrintTypes=").append(usedPrintTypes);
        sb.append('}');
        return sb.toString();
    }

    /**
     * @author mbs
     */
    public static class View implements Serializable {
        private String id;
        private String name;
        private String previewImageUrl;
        private String imageUrl;
        private String compositionUrl;

        public View() {
        }

        public View(String id, String name, String previewImageUrl, String imageUrl, String compositionUrl) {
            this.id = id;
            this.name = name;
            this.previewImageUrl = previewImageUrl;
            this.imageUrl = imageUrl;
            this.compositionUrl = compositionUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPreviewImageUrl() {
            return previewImageUrl;
        }

        public String getPreviewImageUrl(String appearanceId) {
            return previewImageUrl.substring(0, previewImageUrl.lastIndexOf("=") + 1) + appearanceId;
        }

        public void setPreviewImageUrl(String previewImageUrl) {
            this.previewImageUrl = previewImageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCompositionUrl() {
            return compositionUrl;
        }

        public void setCompositionUrl(String compositionUrl) {
            this.compositionUrl = compositionUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            View view = (View) o;

            if (compositionUrl != null ? !compositionUrl.equals(view.compositionUrl) : view.compositionUrl != null)
                return false;
            if (id != null ? !id.equals(view.id) : view.id != null) return false;
            if (imageUrl != null ? !imageUrl.equals(view.imageUrl) : view.imageUrl != null)
                return false;
            if (name != null ? !name.equals(view.name) : view.name != null) return false;
            if (previewImageUrl != null ? !previewImageUrl.equals(view.previewImageUrl) : view.previewImageUrl != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (previewImageUrl != null ? previewImageUrl.hashCode() : 0);
            result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
            result = 31 * result + (compositionUrl != null ? compositionUrl.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("View");
            sb.append("{id='").append(id).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", previewImageUrl='").append(previewImageUrl).append('\'');
            sb.append(", imageUrl='").append(imageUrl).append('\'');
            sb.append(", compositionUrl='").append(compositionUrl).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * @author mbs
     */
    public static class Size implements Serializable {
        private String id;
        private String name;

        public Size() {
        }

        public Size(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Size size = (Size) o;

            if (id != null ? !id.equals(size.id) : size.id != null) return false;
            if (name != null ? !name.equals(size.name) : size.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("Size");
            sb.append("{id='").append(id).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Appearance implements Serializable {
        private String id;
        private String name;
        private String imageUrl;

        public Appearance() {
        }

        public Appearance(String id, String name, String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Appearance that = (Appearance) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
                return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("Appearance");
            sb.append("{id='").append(id).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", imageUrl='").append(imageUrl).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class StockState implements Serializable {
        private StockStateKey key;
        private boolean available;

        public StockState() {
        }

        public StockState(StockStateKey key, boolean available) {
            this.key = key;
            this.available = available;
        }

        public StockStateKey getKey() {
            return key;
        }

        public void setKey(StockStateKey key) {
            this.key = key;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StockState that = (StockState) o;

            if (available != that.available) return false;
            if (key != null ? !key.equals(that.key) : that.key != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (available ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("StockState");
            sb.append("{key=").append(key);
            sb.append(", available=").append(available);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class StockStateKey implements Serializable {
        private String appearanceId;
        private String sizeId;

        public StockStateKey() {
        }

        public StockStateKey(String appearanceId, String sizeId) {
            this.appearanceId = appearanceId;
            this.sizeId = sizeId;
        }

        public String getAppearanceId() {
            return appearanceId;
        }

        public void setAppearanceId(String appearanceId) {
            this.appearanceId = appearanceId;
        }

        public String getSizeId() {
            return sizeId;
        }

        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StockStateKey that = (StockStateKey) o;

            if (appearanceId != null ? !appearanceId.equals(that.appearanceId) : that.appearanceId != null)
                return false;
            if (sizeId != null ? !sizeId.equals(that.sizeId) : that.sizeId != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = appearanceId != null ? appearanceId.hashCode() : 0;
            result = 31 * result + (sizeId != null ? sizeId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("StockStateKey");
            sb.append("{appearanceId='").append(appearanceId).append('\'');
            sb.append(", sizeId='").append(sizeId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Configuration implements Serializable {
        private String id;
        private String type;
        private String width;
        private String height;
        private String text;
        private String designId;
        private String imageUrl;
        private PrintType printType;
        private Set<PrintType.PrintColor> printColors = new HashSet<PrintType.PrintColor>();
        private Set<FontFamily> fontFamilies = new HashSet<FontFamily>();

        public Configuration() {
        }

        public Configuration(String id, String type, String imageUrl, String width, String height, String designId, PrintType printType) {
            this.id = id;
            this.type = type;
            this.imageUrl = imageUrl;
            this.width = width;
            this.height = height;
            this.designId = designId;
            this.printType = printType;
        }

        public Configuration(String id, String type, String imageUrl, String width, String height, String text, Set<FontFamily> fontFamilies, PrintType printType) {
            this.id = id;
            this.type = type;
            this.imageUrl = imageUrl;
            this.width = width;
            this.height = height;
            this.text = text;
            this.printType = printType;
            this.fontFamilies = fontFamilies;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public PrintType getPrintType() {
            return printType;
        }

        public void setPrintType(PrintType printType) {
            this.printType = printType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDesignId() {
            return designId;
        }

        public void setDesignId(String designId) {
            this.designId = designId;
        }

        public Set<PrintType.PrintColor> getPrintColors() {
            return printColors;
        }

        public void setPrintColors(Set<PrintType.PrintColor> printColors) {
            this.printColors = printColors;
        }

        public Set<FontFamily> getFontFamilies() {
            return fontFamilies;
        }

        public void setFontFamilies(Set<FontFamily> fontFamilies) {
            this.fontFamilies = fontFamilies;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Configuration that = (Configuration) o;

            if (designId != null ? !designId.equals(that.designId) : that.designId != null)
                return false;
            if (fontFamilies != null ? !fontFamilies.equals(that.fontFamilies) : that.fontFamilies != null)
                return false;
            if (height != null ? !height.equals(that.height) : that.height != null)
                return false;
            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
                return false;
            if (printColors != null ? !printColors.equals(that.printColors) : that.printColors != null)
                return false;
            if (printType != null ? !printType.equals(that.printType) : that.printType != null)
                return false;
            if (text != null ? !text.equals(that.text) : that.text != null) return false;
            if (type != null ? !type.equals(that.type) : that.type != null) return false;
            if (width != null ? !width.equals(that.width) : that.width != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (width != null ? width.hashCode() : 0);
            result = 31 * result + (height != null ? height.hashCode() : 0);
            result = 31 * result + (text != null ? text.hashCode() : 0);
            result = 31 * result + (fontFamilies != null ? fontFamilies.hashCode() : 0);
            result = 31 * result + (designId != null ? designId.hashCode() : 0);
            result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
            result = 31 * result + (printType != null ? printType.hashCode() : 0);
            result = 31 * result + (printColors != null ? printColors.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("Configuration");
            sb.append("{id='").append(id).append('\'');
            sb.append(", type='").append(type).append('\'');
            sb.append(", width='").append(width).append('\'');
            sb.append(", height='").append(height).append('\'');
            sb.append(", text='").append(text).append('\'');
            sb.append(", fontFamilies='").append(fontFamilies).append('\'');
            sb.append(", designId='").append(designId).append('\'');
            sb.append(", imageUrl='").append(imageUrl).append('\'');
            sb.append(", printType=").append(printType);
            sb.append(", printColors=").append(printColors);
            sb.append('}');
            return sb.toString();
        }
    }
}
