package net.sprd.apps

import scala.xml._
import scala.swing._
import scala.swing.event._
import org.apache.batik.dom.svg.SAXSVGDocumentFactory
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.svg.SVGDocument
import java.io._
import java.awt.Graphics
import java.awt.geom._
import java.awt.RenderingHints 
import org.apache.batik.gvt._
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import net.sprd.api._
import net.sprd.api.model._
import net.sprd.api.model.product._
import net.sprd.api.model.inventory._
import net.sprd.api.svg.SvgImageGenerator
import scala.collection._
import scala.actors.Actor._


object MyBatik extends SimpleGUIApplication {
  
  //var initialShopId = "264433"
  var initialShopId = "401688"
  val top = new Frame {
    title = "Shop Articles Viewer"
    
    //current used data
    var shopId: String = null
    var shop: Shop = null
    var article: Article = null
    var articles: List[Article] = null
    var views: List[View] = null
    
    
    var currentArticleBox: ComboBox[String] = null
    
    val articleLabel = new Label("Article:")
    val articlePanel = new FlowPanel() {
      contents += articleLabel
    }
    
    val shopField = new TextField(initialShopId, 15);
    val shopLabel = new Label("Shop ID:")
    
    val shopPanel = new FlowPanel() {
      contents += shopLabel
      contents += shopField
    }
    
    val menuPanel = new GridPanel(2,1){
      contents += shopPanel
      contents += articlePanel
    }
    
    val imageViewer = new ImageViewer()
    
    val mainPanel = new BorderPanel {
      add(menuPanel,BorderPanel.Position.North)
      add(imageViewer,BorderPanel.Position.Center)
    }
    //update shop
    updateShop(initialShopId)
    //handle swing events
    listenTo(shopField)
    val frame = this;
    reactions += {
      case WindowClosing(frame) =>
        System.exit(0)
      case EditDone(shopField) =>
        updateShop(shopField.text)
    }
    //create article combo box
    def articleBox ( articles:List[Article]) = 
      new ComboBox(List.empty ++ articles map (a => a.id + ": " +a.name))
      
    //update article list box
    def updateArticles(articles: List[Article]) {
      if (articlePanel.contents.size > 1) articlePanel.contents.remove(1)
      this.articles = articles
      currentArticleBox = articleBox(articles)
      articlePanel.contents += currentArticleBox
      articlePanel.repaint()
      contents = mainPanel
      currentArticleBox.listenTo(currentArticleBox.selection)
      currentArticleBox.reactions += {
        case SelectionChanged(comp) =>
          if (comp == currentArticleBox)
            updateViews()
      }
      actor {
        updateViews()
        Thread.sleep(100)
        //preloadSvgs(articles)
      }
    }
    
    def preloadSvgs(articles: Seq[Article]) {
      val currentShopId = shopId
      //prepare batik to render svg
      val builder = new GVTBuilder()
      val bridgeContext = new BridgeContext(new UserAgentAdapter())
      val saxSVGDocumentFactory = new SAXSVGDocumentFactory(
        XMLResourceDescriptor.getXMLParserClassName())
      articles.slice(1,articles.size) foreach (article => {
        val usedViews = getUsedViews(article)
        usedViews foreach (view => {
          if (currentShopId != shopId) return;
          builder.build(bridgeContext, 
            saxSVGDocumentFactory.createSVGDocument(null, new StringReader(
              SvgImageGenerator.generateProductViewImage(article.product, view.id).toString()
            ))
          )
        })
        println("preloaded "+article.id+":"+article.name)
      })
    }
    
    def getUsedViews(article:Article): Seq[View] = {
      val usedPrintAreas = article.product.configurations map (a=> a.printAreaId)
      if (usedPrintAreas.isEmpty) article.product.productType.views.values.next::Nil
          else Nil ++ article.product.productType.views.values.filter(v => usedPrintAreas.foldLeft(false)(_ || v.viewMaps.contains(_)))
    }
    
    //update view list
    def updateViews () {
      val article = articles(currentArticleBox.selection.index)
      if (this.article != article) {
        this.article = article
        this.views = views
        val usedViews = getUsedViews(article)
        val panel = new GridPanel(usedViews.size/2, if (usedViews.size==1) 1 else 2)
        actor {
          imageViewer.setSvgs(usedViews map (a=> updateViewImage(article, a)))
          mainPanel.revalidate()
          contents = mainPanel
          repaint
        }
      }
    }
    //update shop
    def updateShop(shopId : String) {
      if (this.shopId != shopId) {
        this.shopId = shopId
        shop = ApiClient.shops(shopId)
        shop.fetchArticles(true)
        val articles = Nil ++ shop.articles.values
        updateArticles(articles)
        repaint()
      }
    }
    // update images
    def updateViewImage(article: Article, view: View) = {
      val product = article.product
      SvgImageGenerator.generateProductViewImage(product, view.id)
    }
    
    // view component class
    class ImageViewer extends Component{
      //prepare batik to render svg
      val builder = new GVTBuilder()
      val bridgeContext = new BridgeContext(new UserAgentAdapter())
      val saxSVGDocumentFactory = new SAXSVGDocumentFactory(
        XMLResourceDescriptor.getXMLParserClassName()
      )
    
      var rows = 3
      var columns = 3
      var cgNodes: Seq[GraphicsNode] = Nil
      preferredSize = (500, 500)
      
      def setSvgs (svgs: Seq[Elem]) {
        cgNodes = svgs map (a =>  
          builder.synchronized {builder.build(bridgeContext, saxSVGDocumentFactory.createSVGDocument(null, new StringReader(a.toString())))}
        )
        rows = Math.ceil(cgNodes.size/3.0f).toInt
        columns = if (cgNodes.size<=3) cgNodes.size else 3
      }
      
      override def paintComponent(g: Graphics2D) {
        preferredSize = size
        val internalCgNodes = cgNodes
        if (internalCgNodes.size > 0) {
          val width = Math.min(size.getWidth / columns, size.getHeight / rows)
          val height = width
          val xOffset = (size.getWidth - (width * columns)) / 2
          val yOffset = (size.getHeight - (height * rows)) / 2
          
          0 until internalCgNodes.size foreach (i => {
            val cgNode = internalCgNodes(i)
            val posX = i % columns
            val posY = i / columns
            val bounds = cgNode.getBounds
            if (bounds != null) {
              val scaleX = width / bounds.getWidth
              val scaleY = height / bounds.getHeight
              val transform = new AffineTransform()
              transform.translate(posX*width+xOffset, posY*height+yOffset)
              transform.scale(scaleX,scaleY)
              
              cgNode.setTransform(transform);
              g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
              cgNode.paint(g)
            }})
          
        }
      }
    }
  
  
  }
  
  
}
