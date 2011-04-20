package net.sprd.sampleapps.common.dataaccess.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * @author mbs
 */
public class Basket implements Serializable{
    private String id;
    private String noItems;
    private String total;
    private String vat;
    private String currencySymbol;
    private List<BasketItem> basketItems = new ArrayList<BasketItem>();

    public Basket() {
    }

    public Basket(String id, String noItems, String total, String vat, String currencySymbol) {
        this.id = id;
        this.noItems = noItems;
        this.total = total;
        this.vat = vat;
        this.currencySymbol = currencySymbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoItems() {
        return noItems;
    }

    public void setNoItems(String noItems) {
        this.noItems = noItems;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<BasketItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Basket basket = (Basket) o;

        if (basketItems != null ? !basketItems.equals(basket.basketItems) : basket.basketItems != null)
            return false;
        if (currencySymbol != null ? !currencySymbol.equals(basket.currencySymbol) : basket.currencySymbol != null)
            return false;
        if (id != null ? !id.equals(basket.id) : basket.id != null) return false;
        if (noItems != null ? !noItems.equals(basket.noItems) : basket.noItems != null)
            return false;
        if (total != null ? !total.equals(basket.total) : basket.total != null)
            return false;
        if (vat != null ? !vat.equals(basket.vat) : basket.vat != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (noItems != null ? noItems.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (vat != null ? vat.hashCode() : 0);
        result = 31 * result + (currencySymbol != null ? currencySymbol.hashCode() : 0);
        result = 31 * result + (basketItems != null ? basketItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Basket");
        sb.append("{id='").append(id).append('\'');
        sb.append(", noItems='").append(noItems).append('\'');
        sb.append(", total='").append(total).append('\'');
        sb.append(", vat='").append(vat).append('\'');
        sb.append(", currencySymbol='").append(currencySymbol).append('\'');
        sb.append(", basketItems=").append(basketItems);
        sb.append('}');
        return sb.toString();
    }

    public static final class BasketItem implements Serializable {
        private String id;
        private String name;
        private Article.Size size;
        private Article.Appearance appearance;
        private String quantity;
        private String price;        
        private String total;
        private String currencySymbol;
        private Article article;

        public BasketItem() {
        }

        public BasketItem(String id, String name, String quantity,
                          String price, String total, String currencySymbol) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.total = total;
            this.currencySymbol = currencySymbol;
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

        public Article.Size getSize() {
            return size;
        }

        public void setSize(Article.Size size) {
            this.size = size;
        }

        public Article.Appearance getAppearance() {
            return appearance;
        }

        public void setAppearance(Article.Appearance appearance) {
            this.appearance = appearance;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        public Article getArticle() {
            return article;
        }

        public void setArticle(Article article) {
            this.article = article;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BasketItem that = (BasketItem) o;

            if (appearance != null ? !appearance.equals(that.appearance) : that.appearance != null)
                return false;
            if (article != null ? !article.equals(that.article) : that.article != null)
                return false;
            if (currencySymbol != null ? !currencySymbol.equals(that.currencySymbol) : that.currencySymbol != null)
                return false;
            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (price != null ? !price.equals(that.price) : that.price != null)
                return false;
            if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null)
                return false;
            if (size != null ? !size.equals(that.size) : that.size != null) return false;
            if (total != null ? !total.equals(that.total) : that.total != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (size != null ? size.hashCode() : 0);
            result = 31 * result + (appearance != null ? appearance.hashCode() : 0);
            result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
            result = 31 * result + (price != null ? price.hashCode() : 0);
            result = 31 * result + (total != null ? total.hashCode() : 0);
            result = 31 * result + (currencySymbol != null ? currencySymbol.hashCode() : 0);
            result = 31 * result + (article != null ? article.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("BasketItem");
            sb.append("{id='").append(id).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", size=").append(size);
            sb.append(", appearance=").append(appearance);
            sb.append(", quantity='").append(quantity).append('\'');
            sb.append(", price='").append(price).append('\'');
            sb.append(", total='").append(total).append('\'');
            sb.append(", currencySymbol='").append(currencySymbol).append('\'');
            sb.append(", article=").append(article);
            sb.append('}');
            return sb.toString();
        }
    }
}
