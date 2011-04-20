package examples

import net.sprd.api.ApiClient
import net.sprd.api.ImageServer

/**
 * This example loads the articles of a shop, an output some data and available appearances and sizes of the first ones.
 */
object ArticleExample extends Application {
  //configure APIClient
  ApiClient.baseUrl = "http://api.spreadshirt.net/api/v1"


  //shopId for this example, could be exchanged with any other shopId
  val shopId = "401688";
  //load shop from API
  val shop = ApiClient.shops(shopId)
  //print count of articles in this shop
  println("ArticleCount= " + shop.articles.size)
  //Load shop currency to format prices (all Articles of the Shop use this currency,
  //but each article has also an attribute currencyId)
  val currency = ApiClient.currencies(shop.currencyId)

  shop.articles.slice(0, 4, false).foreach(article => {
    println("Article:" + article.id + "\nName:" + article.name+ "\nPrice: "+currency.format(article.price))
    println("ImageUrl of front view:" + ImageServer.getProductUrl(article.productId, "1"))

    println("Available Appearances:");
    article.product.possibleAppearances.foreach(app => {
      print("Appearance " + app.name + "  Color:")
      app.colors.foreach(color => print("" + color._2 + ", "))

      print("  Sizes in stock: ");
      //filter all sizes of the used productType if they are inStock with the appearance and output sizes,
      // which are in stock.
      article.product.productType.sizes.filter(
        size => article.product.productType.inStock(app.id, size.id)
      ).foreach(
        size => print(size.name + ", ")
      )
      println();
    })
    println("\n");
  });

}
