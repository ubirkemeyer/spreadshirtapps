<?php
require_once 'RestRequest.php';



class ProductCDO {

  private $requestUri = 'http://api.spreadshirt.net/api/v1/shops/40000/products/8318075';

  private $productID;
  private $productTypeLink;
  private $productImageLink;
  private $defaultAppearanceID;

  // constructor
  public function __construct($uri) {
        $rest = new RestRequest();
        $this->requestUri = $uri;

        $xml = simplexml_load_string( $rest->submit($uri) );

        $this->productID = (String) $xml->attributes()->id;
        $this->productTypeLink = (String) $xml->productType->attributes('http://www.w3.org/1999/xlink');
        $this->resourceLink = (String) $xml->resources->resource[1]->attributes('http://www.w3.org/1999/xlink');
        $this->defaultAppearanceID = (String) $xml->appearance['id'];

  }

  public function getProductID() {
    return $this->productID;
  }

  public function getProductTypeLink() {
    return $this->productTypeLink;
  }

  public function getProductImageLink() {
    return $this->productImageLink;
  }

  public function getDefaultAppearanceID() {
      return $this->defaultAppearanceID;
  }


}

?>
