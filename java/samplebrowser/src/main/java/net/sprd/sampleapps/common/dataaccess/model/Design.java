package net.sprd.sampleapps.common.dataaccess.model;

/**
 * @author mbs
 */
public class Design {
    private String id;
    private String name;
    private String description;
    private String href;
    private String imageUrl;
    private String price;
    private String currency;
    private String currencySymbol;

    public Design() {
    }

    public Design(String id, String name, String description, String href, String imageUrl,
                   String price, String currency, String currencySymbol) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.href = href;
        this.imageUrl = imageUrl;
        this.price = price;
        this.currency = currency;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Design article = (Design) o;

        if (currency != null ? !currency.equals(article.currency) : article.currency != null)
            return false;
        if (currencySymbol != null ? !currencySymbol.equals(article.currencySymbol) : article.currencySymbol != null)
            return false;
        if (href != null ? !href.equals(article.href) : article.href != null)
            return false;
        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (imageUrl != null ? !imageUrl.equals(article.imageUrl) : article.imageUrl != null)
            return false;
        if (name != null ? !name.equals(article.name) : article.name != null)
            return false;
        if (description != null ? !description.equals(article.description) : article.description != null)
            return false;
        if (price != null ? !price.equals(article.price) : article.price != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (href != null ? href.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (currencySymbol != null ? currencySymbol.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Design");
        sb.append("{id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", href='").append(href).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", currencySymbol='").append(currencySymbol).append('\'');
        sb.append('}');
        return sb.toString();
    }
}