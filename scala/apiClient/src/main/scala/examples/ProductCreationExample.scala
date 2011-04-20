package examples

import net.sprd.api.ApiClient
import net.sprd.api.creator.ProductReplacer
import net.sprd.api.svg.SvgImageGenerator
import net.sprd.api.model.product.ProductUpdate
import net.sprd.api.model.basket.BasketItemElement
import java.net.URI

object ProductCreationExample extends Application {

  //configure APIClient
  ApiClient.baseUrl = "http://api.spreadshirt.net/api/v1"
  //set apiKey and secret for create Product in API
  //you need the rights for the shop (it should be your own)
  ApiClient.apiKey = "123456789"
  ApiClient.secret = "987654321"

  //configure data
  val shopId = "533633";
  //Template Product
  val productId = "18086832"
  //Front View
  val viewId = "1"

  //load Shop from API
  val shop = ApiClient.shops(shopId)

  //set new text
  val text = "Exchanged new Text!";

  //load Template Product from API
  val product = shop.products(productId)

  //Products from API are read only, so
  //copy product to a new one which can be manipulated
  val newProduct = new ProductUpdate(product)

  //now the new product can be changed. Here we exchange the template Text
  //replace TemplateText with given Text in Configurations
  //here "!TEXT0xxxxxxx!" on template shirt is replaced with variable text
  newProduct.configurations = newProduct.configurations.map(
    ProductReplacer.replaceTemplateConfiguration(_, Array(text))
  )
  //Copy Product and exchange templates can also be done in one step:
  //val newProduct = ProductReplacer.replaceTemplate(product, Array(text))

  //get svg for image of new product (front view)
  val svg = SvgImageGenerator.generateProductViewImage(newProduct, viewId)
  println(svg)

  //create Product in API and load it from API
  val newApiProduct = ApiClient.createProduct(shop, newProduct)

  //create a Basket for the shop
  //if a Basket with the Token already exists, it is loaded
  var basket = ApiClient.createBasket(shop, "My Session Token")
  println("BasketId: "+basket.id)

  //create basketItem with new product, appearance and size (3 = M)
  val element = BasketItemElement(newApiProduct, "3", newApiProduct.appearanceId)
  //add it to the basket
  basket.add(shopId, element, "MY new Product", 1)
  //update Basket on API
  val responseCode = ApiClient.updateBasket(basket)
  println("ResponseCode= "+responseCode)

  //get Checkout Url
  val checkoutUrl = ApiClient.getBasketCheckoutUrl(basket.id)
  println("CheckoutUrl:"+checkoutUrl)

  //if Desktop Mode: open Browser with Basket in Checkout
  if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported( java.awt.Desktop.Action.BROWSE ))
    java.awt.Desktop.getDesktop().browse(new URI(checkoutUrl))

}
