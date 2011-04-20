package recent

class RecentController {
  def spreadshirtAPIService
  def spreadshirtImageService

  def index = {
    def model = [:]
    def x = 0.8


    def shop = spreadshirtAPIService.getShop(params.shop)
    if (shop == null) {
      render 'view': 'error', 'model': ['msg': "Shop ${params.shop} does not exist."]
      return
    }

    def width
    def height

    int imageWidth
    int imageHeight

    def fontFamily = params.fontFamily
    def fontSize = params.fontSize
    def headline = params.headline
    def name = params.name
    def price = params.price

    model.headline = headline == 'true'
    model.name = name == 'true'
    model.price = price == 'true'
    model.colorAlternatives = params.colorAlternatives == 'true'
    model.availableSizes = params.availableSizes == 'true'
    model.backgroundColor = params.backgroundColor
    model.textColor = params.textColor

    if (fontFamily == null) fontFamily = grailsApplication.config.de.kabain.groovy.recent.default.fontFamily
    if (fontSize == null) fontSize = grailsApplication.config.de.kabain.groovy.recent.default.fontSize
    if (headline == null) headline = grailsApplication.config.de.kabain.groovy.recent.default.headline


    if (!model.headline) {
      x += 0.1
    }
    if (!(model.name && model.price)) {
      x += 0.05
    }
    try {
      width = params.width
      height = params.height
      imageWidth = x * Double.parseDouble(width).toInteger()
      imageHeight = (x * Double.parseDouble(height)).toInteger()
    } catch (Exception ex) {
      width = 250
      height = 250
      imageWidth = (x * width).toInteger()
      imageHeight = (x * height).toInteger()
    }

    model.shopName = shop.name
    model.fontFamily = fontFamily
    model.fontSize = fontSize


    def articles = spreadshirtAPIService.getArticles(shop.id, 1, 0)
    if (articles.size() == 0) {
      render 'view': 'error', 'model': ['msg': "Shop ${params.shop} has no articles."]
      return
    }
    model.article = articles[0]
    def attributes = ['width': imageWidth, 'height': imageHeight]
    model.imageURL = spreadshirtImageService.getArticleURL(model.article, attributes)

    render 'view': 'recent', 'model': model
  }
}
