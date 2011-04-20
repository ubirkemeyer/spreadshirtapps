<?php
require_once 'RestRequest.php';

require_once 'ArticleCDO.php';
require_once 'ProductCDO.php';
require_once 'ProductTypeCDO.php';
require_once 'CheckoutController.php';

/**
 * The "Shirt of the Day" Widget supports only a single article.
 */
class ShirtOfDay  {

  // class variables
  private $article;
  private $product;
  private $productType;


  // set here your shop id
  private $shopID = "205909";

  // if the articleId is not set the first article from shop is taken
  private $articleID;

  private $useShopCheckout = false;

  // constructor
  public function __construct() {
    // assign class variables and load data from sprd API

    // grab the first product/article from shop if the articleID is not set...
    if (!isset ($this->articleID)) {
        $rest = new RestRequest();

        $requestUri='http://api.spreadshirt.net/api/v1/shops/'.$this->shopID.'/articles';
        $xml = simplexml_load_string( $rest->submit($requestUri) );
        //$this->articleID = (String) $xml->article->attributes()->id;
        $this->articleID = (String) $xml->article['id'];
    }

    $this->article = new ArticleCDO($this->shopID, $this->articleID);
    $this->product = new ProductCDO($this->article->getProductLink());
    $this->productType = new ProductTypeCDO( $this->product->getProductTypeLink());

  }

  /**
   * default preview image
   * @return <String> image tag
   */
  public function getHTMLThumbImage() {
        return '<img src="'.$this->article->getResourceLink().'"/>';
  }

  /** the price */
  public function getPriceString() {
    return $this->article->getPrice()."€";
  }

  /** a short product name */
  public function getShortName() {
    return $this->productType->getName();
  }

  /** the description of the product */
  public function getDescription() {
    return $this->productType->getDescription();
  }


  /**
   * construct a url for a article preview image
   * @param <String> $view the view 1=front, 2=back, 3=left, 4=right
   * @param <String> $width size
   * @param <String> $height size
   * @return <String> image url
   */
  public function getArticleImageUrl($view="1", $width="300", $height="300") {
    return "http://image.spreadshirt.net/image-server/v1/products/".
                $this->article->getProductID()."/views/".
                $view.",width=".$width.",height=".$height.".png";
  }

  /** generate a html <img> tag */
  public function getArticlePreviewImage($view="1", $width="300", $height="300") {
        return '<img src="'.$this->getArticleImageUrl($view, $width, $height).'"/>';
  }

  /** returns the url of the checkout or null if there is a error */
  public function goToCheckout($sizeID, $appearance) {
      $sizes=$this->productType->getSizes();
      if ( isset($sizes[$sizeID])) {
//         echo "size :".$sizes[$sizeID];
//         echo "appearance :".$this->product->getDefaultAppearanceID();

         $checkout = new CheckoutController();
         if ($this->useShopCheckout) {
           return $checkout->checkout($this->article, $this->shopID, $sizeID, $this->product->getDefaultAppearanceID());
         } else {
           return $checkout->checkout($this->article, NULL, $sizeID, $this->product->getDefaultAppearanceID());
         }
      }
      return NULL;
  }


  /** construct a html select element with all available sizes */
  public function getSizeElement() {
    $back = '<select id="size-select" name="sizeID">';
      foreach($this->productType->getSizes() as $key=>$val) {
        $back .= '<option value="'.$key.'">'.$val.'</option>' ;
      }
    return $back.'</select>';
  }

}

?>
