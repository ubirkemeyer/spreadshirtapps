<?php
require_once 'RestRequest.php';



class ProductTypeCDO {

    private $requestUri = 'http://api.spreadshirt.net/api/v1/shops/40000/products/8318075';
    private $productTypeID;

    private $name;
    private $description;

    private $sizes;

    // constructor
    public function __construct($uri) {
        $rest = new RestRequest();
        $this->requestUri = $uri;

        $xml = simplexml_load_string( $rest->submit($uri) );

        $this->productTypeID = (String) $xml->attributes()->id;
        $this->name = (String) $xml->name;
        $this->description = (String) $xml->description;

        // transform sizes to a 2d array
        $tmp = array();
        $array = (array)$xml->sizes;
        if(!empty($array)) {
            foreach ($array['size'] as $key=>$values) {
                if(is_a($values,'SimpleXMLElement')) {
                    $tmp[(String)$values->attributes()->id]=(String)$values->name;
                }
            }
        }
        $this->sizes = $tmp;
    }

    public function getProductID() {
        return $this->productID;
    }

    public function getName() {
        return $this->name;
    }

    public function getDescription() {
        return $this->description;
    }

    /**
     *
     * @return <array> array[id, description] of available sizes
     */
    public function getSizes() {
        return $this->sizes;
    }

}

?>

