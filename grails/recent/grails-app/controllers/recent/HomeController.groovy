package recent

class HomeController {

  def index = {


    def model = [:]

    model.shopId = grailsApplication.config.de.kabtain.groovy.recent.default.shopId
    model.width = grailsApplication.config.de.kabtain.groovy.recent.default.width
    model.height = grailsApplication.config.de.kabtain.groovy.recent.default.height
    model.fontFamily = grailsApplication.config.de.kabtain.groovy.recent.default.fontFamily
    model.fontSize = grailsApplication.config.de.kabtain.groovy.recent.default.fontSize
    model.backgroundColor = 'FFAA00'
    model.textColor = '000000'
    model.headline = grailsApplication.config.de.kabtain.groovy.recent.default.headline
    model.name = grailsApplication.config.de.kabtain.groovy.recent.default.name
    model.price = grailsApplication.config.de.kabtain.groovy.recent.default.price
    model.colorAlternatives = grailsApplication.config.de.kabtain.groovy.recent.default.colorAlternatives
    model.availableSizes = grailsApplication.config.de.kabtain.groovy.recent.default.availableSizes
    model.fontFamilies = [
            'Helvetica', 'Arial', 'Verdana', 'Times Roman', 'Sans Serif', 'Courier'
    ]


    render 'view': 'index', 'model': model
  }
}
