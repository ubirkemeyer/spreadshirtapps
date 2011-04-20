package examples

import net.sprd.api.ApiClient

/**
 * This example load the currency 1, print Currency data and print a formatted price with the currency.
 * No apiKey and secret are necessary.
 */
object CurrencyExample extends Application {

  //configure APIClient
  ApiClient.baseUrl = "http://api.spreadshirt.net/api/v1"

  //no apiKey and secret necessary for currencies
  val currency = ApiClient.currencies("1")

  println("Currency: " + currency.plain + " " + currency.symbol)
  println("Pattern: " + currency.pattern)
  println("Formatted Amount: " + currency.format(145.23))

}
