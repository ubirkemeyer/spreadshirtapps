package net.sprd.groovy.amazonas

import java.text.DecimalFormat
import org.apache.commons.io.IOUtils
import de.kabtain.groovy.sprd.shop.Article
import de.kabtain.groovy.sprd.product.DesignConfiguration
import de.kabtain.groovy.sprd.design.DesignContext

class ConverterService {
  def grailsApplication
  def spreadshirtAPIService
  def spreadshirtImageService
  private static final SEP = '\t'
  private static final LINE_SEP = '\n'
  private static final DecimalFormat DOT_FORMAT = new DecimalFormat("0.00")

  public ConverterService() {
  }

  def setProperties(AmazonParentProduct amazonProduct, Article spreadshirtArticle) {
    amazonProduct.setArticle(spreadshirtArticle)
    amazonProduct.setShop(spreadshirtArticle.shop)
    amazonProduct.setUser(spreadshirtArticle.shop.user)

    def primaryDesign = null
    spreadshirtArticle.product.configurations.each {configuration ->
      if (primaryDesign == null && configuration instanceof DesignConfiguration) {
        primaryDesign = spreadshirtAPIService.getDesign(spreadshirtArticle.shop.id, configuration.designId, DesignContext.AUTO)
      }
    }
    amazonProduct.primaryDesign = primaryDesign
    if (spreadshirtArticle?.product?.productType?.id) {
      amazonProduct.amazonProductType = AmazonProductType.findBySpreadshirtProductTypeId(spreadshirtArticle.product.productType.id)
    }
  }

  def exportAmazonProducts(def shopId, String fileNameStub, def operation, def brand) {
    def resultFiles = []
    if (!shopId) {
      return resultFiles
    }
    boolean shortExport = false

    if (operation == ExportController.MODE_PRICES) {
      shortExport = true
      operation = 'update'
    }

    byte[] headline = getHeadline(shortExport).getBytes()
    def cnf = AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_EXPORT_ENCODING);
    String encoding = cnf.configurationValue

    int fileIndex = 1;
    int fileSize = headline.length;
    File file = new File(fileNameStub + "_${fileIndex++}.csv")
    //FileWriter writer = new FileWriter(file)
    FileOutputStream fos = new FileOutputStream(file)
    IOUtils.write(getHeadline(shortExport), fos, encoding)

    //fos.write(headline)

    def articleCount = Integer.parseInt(spreadshirtAPIService.getArticlesCount(shopId))
    def offset = 0;
    def limit = Integer.parseInt(AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_API_LIMIT).configurationValue)
    def maxFilesize = Integer.parseInt(AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_MAX_FILESIZE_BYTES).configurationValue)

    //def spreadshirtArticles = [spreadshirtAPIService.getArticle(135477, 12063414)]

    while (offset < articleCount) {
      def spreadshirtArticles = spreadshirtAPIService.getArticles(shopId, limit, offset, 'asc', 'created')
      offset += limit

      spreadshirtArticles.each { article ->

        def amazonParentProduct = new AmazonParentProduct()
        amazonParentProduct.brand = brand
        def articles = []
        setProperties(amazonParentProduct, article)

        if (amazonParentProduct.amazonProductType == null) {
          log.warn("Skip spreadshirt article id ${article.id} - no amazon producttype available")
          return
        }

        def primaryImage = null
        def secondaryImages = []
        article.product.productType.views.each {view ->
          if (view.id == article.product.defaultViewId || article.product.defaultViewId == null && primaryImage == null) {
            primaryImage = spreadshirtImageService.getProductImageURL(article.product, view.id)
          } else {
            secondaryImages << spreadshirtImageService.getProductImageURL(article.product, view.id)
          }
        }

        amazonParentProduct.primaryImageURL = primaryImage
        amazonParentProduct.secondaryImageURLs = secondaryImages

        articles << amazonParentProduct

        article.product.productType.appearances.each {appearance ->
          def availableSizes = article.product.productType.getAvailableSizes(appearance.id)

          article.product.productType.sizes.each {size ->

            def amazonProduct = new AmazonProduct()
            amazonProduct.inStock = availableSizes.find {it.id == size.id} != null
            amazonProduct.brand = brand
            setProperties(amazonProduct, article)
            amazonProduct.parentProduct = amazonParentProduct
            amazonProduct.apperance = appearance
            amazonProduct.size = size
            amazonProduct.colorMapping = AmazonColorMapping.findBySpreadshirtColorId(appearance.id)

            if (amazonProduct.colorMapping == null) {
              log.warn("Skip spreadshirt article id ${article.id} appearance ${appearance.id} - no amazon color mapping available")
              return
            }

            primaryImage = null
            secondaryImages = []
            article.product.productType.views.each {view ->
              if (view.id == article.product.defaultViewId || article.product.defaultViewId == null && primaryImage == null) {
                primaryImage = spreadshirtImageService.getProductImageURL(article.product, view.id, appearance.id)
              } else {
                secondaryImages << spreadshirtImageService.getProductImageURL(article.product, view.id, appearance.id)
              }
            }
            amazonProduct.primaryImageURL = primaryImage
            amazonProduct.secondaryImageURLs = secondaryImages

            articles << amazonProduct
          }
        }
        String serializedArticles
        if (shortExport) {
          serializedArticles = serializeShort(articles, operation)
        } else {
          serializedArticles = serializeArticles(articles, operation)
        }
        byte[] bytesArticles = serializedArticles.getBytes()
        if (bytesArticles.length + fileSize > maxFilesize) {
          fos.flush()
          fos.close()
          resultFiles << file
          file = new File(fileNameStub + "_${fileIndex++}.csv")
          fos = new FileOutputStream(file)
          IOUtils.write(getHeadline(shortExport), fos, encoding)
          //fos.write(headline)
          fileSize = headline.length
        }
        //fos.write(bytesArticles)
        IOUtils.write(serializedArticles, fos, encoding)
        fileSize += bytesArticles.length
      }
    }

    if (fileSize > 0) {
      fos.flush()
      fos.close()
      resultFiles << file
    }

    return resultFiles
  }

  private String serializeShort(def articles, def operation) {
    StringBuilder sb = new StringBuilder()
    articles.each {article ->
      log.info("short " + article.sku)
      sb.append(article.sku).append(SEP)
      sb.append(article.currency).append(SEP)

      if (article.itemPrice) {
        sb.append(DOT_FORMAT.format(article.itemPrice))
      }
      sb.append(SEP)
      sb.append(article.quantity).append(SEP)
      sb.append(operation).append(LINE_SEP)
    }
    return sb.toString()
  }

  private String serializeArticles(def articles, def operation) {
    StringBuilder sb = new StringBuilder()
    articles.each {article ->
      //AmazonParentProduct article
      log.info(article.sku)
      //sku
      sb.append(article.sku).append(SEP)
      //product-id
      sb.append(SEP)
      //product-id-type
      sb.append(SEP)
      //product-name
      sb.append(article.productName).append(SEP)
      //brand
      sb.append(article.brand).append(SEP)
      //style-keyword1
      sb.append(article.styleKeyword).append(SEP)
      sb.append(article.clothingType).append(SEP)
      //currency
      sb.append(article.currency).append(SEP)
      //department1
      sb.append(article.department1).append(SEP)
      //department2
      sb.append(article.department2).append(SEP)
      //department3
      sb.append(article.department3).append(SEP)
      //department4
      sb.append(article.department4).append(SEP)
      //department5
      sb.append(article.department5).append(SEP)
      //recommended-browse-node1
      sb.append(article.recommendedBrowseNode).append(SEP)
      //recommended-browse-node2
      sb.append(SEP)
      //model-number
      sb.append(SEP)
      //model-name
      sb.append(SEP)
      //model-year
      sb.append(SEP)
      //relationship-type
      sb.append(article.relationshipType).append(SEP)
      //variation-theme
      sb.append(article.variationTheme).append(SEP)
      //parent-child
      sb.append(article.parentChild).append(SEP)
      //parent-sku
      sb.append(article.parentSku).append(SEP)
      //color
      sb.append(article.color).append(SEP)
      //color-map
      sb.append(article.colorMap).append(SEP)
      //size
      sb.append(article.size).append(SEP)
      //size-map
      sb.append(article.sizeMap).append(SEP)
      //material-composition
      sb.append(article.materialComposition).append(SEP)
      //care-instructions
      sb.append(article.careInstructions).append(SEP)
      //outer-material
      sb.append(article.outerMaterial).append(SEP)
      //inner-material
      sb.append(article.innerMaterial).append(SEP)
      //product-description
      sb.append(article.productDescription).append(SEP)
      //main-image-url
      sb.append(article.mainImageURL).append(SEP)
      //swatch-image-url
      sb.append(article.swatchImageURL).append(SEP)
      //other-image-url1
      sb.append(article.otherImageURL1).append(SEP)
      //other-image-url2
      sb.append(article.otherImageURL2).append(SEP)
      //other-image-url3
      sb.append(article.otherImageURL3).append(SEP)
      //other-image-url4
      sb.append(article.otherImageURL4).append(SEP)
      //other-image-url5
      sb.append(article.otherImageURL5).append(SEP)
      //other-image-url6
      sb.append(article.otherImageURL6).append(SEP)
      //other-image-url7
      sb.append(article.otherImageURL7).append(SEP)
      //other-image-url8
      sb.append(article.otherImageURL8).append(SEP)
      //search-terms1
      sb.append(article.searchTerms1).append(SEP)
      //search-terms2
      sb.append(article.searchTerms2).append(SEP)
      //search-terms3
      sb.append(article.searchTerms3).append(SEP)
      //search-terms4
      sb.append(article.searchTerms4).append(SEP)
      //search-terms5
      sb.append(article.searchTerms5).append(SEP)
      //season
      sb.append(article.season).append(SEP)
      //occasion-lifestyle1
      sb.append(SEP)
      //occasion-lifestyle2
      sb.append(SEP)
      //occasion-lifestyle3
      sb.append(SEP)
      //occasion-lifestyle4
      sb.append(SEP)
      //occasion-lifestyle5
      sb.append(SEP)
      //size-modifier
      sb.append(article.sizeModifier)
      //style-name
      sb.append(SEP)
      sb.append(SEP)
      //apparel-closure-type
      sb.append(SEP)
      //sleeve-type
      sb.append(article.sleeveType)
      //neck-size-unit-of-measure
      sb.append(SEP)
      sb.append(SEP)
      //neck-size
      sb.append(SEP)
      //waist-style
      sb.append(SEP)
      //collar-type
      sb.append(article.collarType).append(SEP)
      //waist-size-unit-of-measure
      sb.append(SEP)
      //waist-size
      sb.append(SEP)
      //inseam-unit-of-measure
      sb.append(SEP)
      //inseam-length
      sb.append(SEP)
      //chest-size-unit-of-measure
      sb.append(SEP)
      //chest-size
      sb.append(SEP)
      //cup-size
      sb.append(SEP)
      //material-opacity
      sb.append(SEP)
      //sole-material
      sb.append(SEP)
      //shoe-closure-type
      sb.append(SEP)
      //shoe-width
      sb.append(SEP)
      //heel-height-unit-of-measure
      sb.append(SEP)
      //heel-height
      sb.append(SEP)
      //heel-type
      sb.append(SEP)
      //shaft-height
      sb.append(SEP)
      //shaft-diameter
      sb.append(SEP)
      //item-price
      if (article.itemPrice) {
        sb.append(DOT_FORMAT.format(article.itemPrice))
      }
      sb.append(SEP)
      //quantity
      sb.append(article.quantity).append(SEP)
      //sale-price
      sb.append(SEP)
      //sale-from-date
      sb.append(SEP)
      //sale-through-date
      sb.append(SEP)
      //leadtime-to-ship
      sb.append(SEP)
      //launch-date
      sb.append(SEP)
      //country-of-origin
      sb.append(article.getCountryOfOrigin()).append(SEP)
      //is-discontinued-by-manufacturer
      sb.append(SEP)
      //is-gift-message-available
      sb.append(SEP)
      //is-gift-wrap-available
      sb.append(SEP)
      //platinum-keywords1
      sb.append(article.platinumKeywords1).append(SEP)
      //platinum-keywords2
      sb.append(article.platinumKeywords2).append(SEP)
      //platinum-keywords3
      sb.append(article.platinumKeywords3).append(SEP)
      //platinum-keywords4
      sb.append(article.platinumKeywords4).append(SEP)
      //platinum-keywords5
      sb.append(article.platinumKeywords5).append(SEP)
      //registered-parameter
      sb.append(article.getRegisteredParameter()).append(SEP)
      //release-date
      sb.append(SEP)
      //restock-date
      sb.append(SEP)
      //shipping-weight-unit-of-measure
      sb.append(SEP)
      //shipping-weight
      sb.append(SEP)
      //fulfillment-center-id
      sb.append(SEP)
      //package-length
      sb.append(SEP)
      //package-width
      sb.append(SEP)
      //package-height
      sb.append(SEP)
      //package-length-unit-of-measure
      sb.append(SEP)
      //package-weight
      sb.append(SEP)
      //package-weight-unit-of-measure
      sb.append(SEP)
      //max-aggregate-ship-quantity
      sb.append(SEP)
      //update-delete
      sb.append(operation).append(LINE_SEP)
    }
    sb.toString()
  }

  private static def getHeadline(def shortLine = false) {
    StringBuilder sb = new StringBuilder()

    sb.append(AmazonasConfig.findByConfigurationKey(AmazonasConfig.KEY_AMAZON_FILE_HEADER)?.configurationValue)
    sb.append(LINE_SEP)
    sb.append("sku").append(SEP)
    if (!shortLine) {
      sb.append("product-id").append(SEP)
      sb.append("product-id-type").append(SEP)
      sb.append("product-name").append(SEP)
      sb.append("brand").append(SEP)
      sb.append("style-keyword1").append(SEP)
      sb.append("clothing-type").append(SEP)
    }
    sb.append("currency").append(SEP)

    if (!shortLine) {
      sb.append("department1").append(SEP)
      sb.append("department2").append(SEP)
      sb.append("department3").append(SEP)
      sb.append("department4").append(SEP)
      sb.append("department5").append(SEP)
      sb.append("recommended-browse-node1").append(SEP)
      sb.append("recommended-browse-node2").append(SEP)
      sb.append("model-number").append(SEP)
      sb.append("model-name").append(SEP)
      sb.append("model-year").append(SEP)
      sb.append("relationship-type").append(SEP)
      sb.append("variation-theme").append(SEP)
      sb.append("parent-child").append(SEP)
      sb.append("parent-sku").append(SEP)
      sb.append("color").append(SEP)
      sb.append("color-map").append(SEP)
      sb.append("size").append(SEP)
      sb.append("size-map").append(SEP)
      sb.append("material-composition").append(SEP)
      sb.append("care-instructions").append(SEP)
      sb.append("outer-material").append(SEP)
      sb.append("inner-material").append(SEP)
      sb.append("product-description").append(SEP)
      sb.append("main-image-url").append(SEP)
      sb.append("swatch-image-url").append(SEP)
      sb.append("other-image-url1").append(SEP)
      sb.append("other-image-url2").append(SEP)
      sb.append("other-image-url3").append(SEP)
      sb.append("other-image-url4").append(SEP)
      sb.append("other-image-url5").append(SEP)
      sb.append("other-image-url6").append(SEP)
      sb.append("other-image-url7").append(SEP)
      sb.append("other-image-url8").append(SEP)
      sb.append("search-terms1").append(SEP)
      sb.append("search-terms2").append(SEP)
      sb.append("search-terms3").append(SEP)
      sb.append("search-terms4").append(SEP)
      sb.append("search-terms5").append(SEP)
      sb.append("season").append(SEP)
      sb.append("occasion-lifestyle1").append(SEP)
      sb.append("occasion-lifestyle2").append(SEP)
      sb.append("occasion-lifestyle3").append(SEP)
      sb.append("occasion-lifestyle4").append(SEP)
      sb.append("occasion-lifestyle5").append(SEP)
      sb.append("size-modifier").append(SEP)
      sb.append("style-name").append(SEP)
      sb.append("apparel-closure-type").append(SEP)
      sb.append("sleeve-type").append(SEP)
      sb.append("neck-size-unit-of-measure").append(SEP)
      sb.append("neck-size").append(SEP)
      sb.append("waist-style").append(SEP)
      sb.append("collar-type").append(SEP)
      sb.append("waist-size-unit-of-measure").append(SEP)
      sb.append("waist-size").append(SEP)
      sb.append("inseam-length-unit-of-measure").append(SEP)
      sb.append("inseam-length").append(SEP)
      sb.append("chest-size-unit-of-measure").append(SEP)
      sb.append("chest-size").append(SEP)
      sb.append("cup-size").append(SEP)
      sb.append("material-opacity").append(SEP)
      sb.append("sole-material").append(SEP)
      sb.append("shoe-closure-type").append(SEP)
      sb.append("shoe-width").append(SEP)
      sb.append("heel-height-unit-of-measure").append(SEP)
      sb.append("heel-height").append(SEP)
      sb.append("heel-type").append(SEP)
      sb.append("shaft-height").append(SEP)
      sb.append("shaft-diameter").append(SEP)
    }
    sb.append("item-price").append(SEP)
    sb.append("quantity").append(SEP)
    if (!shortLine) {
      sb.append("sale-price").append(SEP)
      sb.append("sale-from-date").append(SEP)
      sb.append("sale-through-date").append(SEP)
      sb.append("leadtime-to-ship").append(SEP)
      sb.append("launch-date").append(SEP)
      sb.append("country-of-origin").append(SEP)
      sb.append("is-discontinued-by-manufacturer").append(SEP)
      sb.append("is-gift-message-available").append(SEP)
      sb.append("is-gift-wrap-available").append(SEP)
      sb.append("platinum-keywords1").append(SEP)
      sb.append("platinum-keywords2").append(SEP)
      sb.append("platinum-keywords3").append(SEP)
      sb.append("platinum-keywords4").append(SEP)
      sb.append("platinum-keywords5").append(SEP)
      sb.append("registered-parameter").append(SEP)
      sb.append("release-date").append(SEP)
      sb.append("restock-date").append(SEP)
      sb.append("shipping-weight-unit-measure").append(SEP)
      sb.append("shipping-weight").append(SEP)
      sb.append("fulfillment-center-id").append(SEP)
      sb.append("package-length").append(SEP)
      sb.append("package-width").append(SEP)
      sb.append("package-height").append(SEP)
      sb.append("package-length-unit-of-measure").append(SEP)
      sb.append("package-weight").append(SEP)
      sb.append("package-weight-unit-of-measure").append(SEP)
      sb.append("max-aggregate-ship-quantity").append(SEP)
    }
    sb.append("update-delete").append(LINE_SEP)
    sb.toString()
  }
}
