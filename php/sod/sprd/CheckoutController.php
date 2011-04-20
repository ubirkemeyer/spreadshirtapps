<?php

require_once 'RestRequest.php';

class CheckoutController {

    private $apiBasketURL = 'http://api.spreadshirt.net/api/v1/baskets';

// ----------------------------------------------------------------------------
    // put YOUR api key and your secret here
    private $API_KEY= '';
    private $SECRET= '';
// ----------------------------------------------------------------------------

    // constructor
    public function __construct() {
      // you can remove this if you set the api key and the secret
      if(empty ($this->API_KEY) || empty ($this->SECRET)) {
        echo"<BR>-----------------------------------<BR>\n";
        echo "  NOTE: API key and/or secret are empty!<BR>\n";
        echo"-----------------------------------<BR>\n";
      }
    }

    /** create a basket, put the article in and return the spreadshirt checkout url */
    public function checkout($article, $shopId, $size, $appearance) {

        // create a new basketitem
        $payload='<?xml version="1.0" encoding="UTF-8" standalone="yes"?><basketItem  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://api.spreadshirt.net">';

        // if no shopId is set, use the marketplace checkout
        if (isset($shopId)) {
         $payload .= '<shop id="'.$shopId.'" xlink:href="http://api.spreadshirt.net/api/v1/shops/'.$shopId.'"/>';
        }

        // prepare the upload, which is a xml document
        // some elements are not necessary/ignored (see price element)
        $payload .= '<description>bla</description>'
         .'<quantity>1</quantity>'
         .'<element id="'.$article->getArticleID()
         .'" type="sprd:article" xlink:href="'.$article->getArticleUrl().'">'
         .'<properties>'
         .'<property key="appearance">'.$appearance.'</property>'
         .'<property key="size">'.$size.'</property>'
         .'</properties>'
         .'</element>'
         .'<price><vatIncluded>34.40</vatIncluded><currency id="1"/></price>'
         .'</basketItem>';

        $customBasket = $this->createBasket($shopId);

        // the basket url has some extra parameters, for security reasons
        // we never send our secret over the network. We send only a evidence
        // which is called signature
        $url= $customBasket."/items";
        $stamp = time()*1000;
        $signature=$this->getSignature('POST', $url, $stamp);

        // construct the basket url and add the required parameter
        $url=$url."?apiKey=".$this->API_KEY."&sig=".$signature.'&time='.$stamp;

        // call api, put item into the current basket
        $this->performPostRequest($url, $payload);

        //basket api url - debug
//        $url=$customBasket;
//        $signature=$this->getSignature('GET', $url, $stamp);
//        $url=$url."?apiKey=".$this->API_KEY."&sig=".$signature.'&time='.$stamp;
//        echo "basket url is :".$url."<br>";

        // request the checkout url for redirect
        $url = $customBasket."/checkout";
        $signature=$this->getSignature('GET', $url, $stamp);
        $url=$url."?apiKey=".$this->API_KEY."&sig=".$signature.'&time='.$stamp;

        $rest = new RestRequest();
        $xml = simplexml_load_string( $rest->doGetCall($url) );

        return (String)$xml->attributes('http://www.w3.org/1999/xlink');
    }
    /**
     *
     * @param <type> $method Http method: GET, POST, PUT, DELETE
     * @param <type> $url url without parameter (?paramt=x)
     * @param <type> $time timestamp in seconds
     * @return <type>
     */
    private function getSignature($method, $url, $time) {
        return sha1($method." ".$url." ".$time." ".$this->SECRET);
    }


    public function createBasket($shopId) {
        $token = "sprd-demo-".uniqid().'-'.uniqid();
        $payload = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><basket xmlns="http://api.spreadshirt.net"><token>'.$token
        .'</token>';

        // set the shopID, used for shop checkout
        if(isset ($shopId)) {
          $payload .= '<shop id="'.$shopId.'"/>';
        }
        $payload .= '</basket>';

        $stamp = time()*1000;
        $signature=$this->getSignature('POST', $this->apiBasketURL, $stamp);

        $url=$this->apiBasketURL."?apiKey=".$this->API_KEY."&sig=".$signature.'&time='.$stamp;
        $result=$this->performPostRequest($url, $payload);

        return $result;
    }


    /** create a basket and return its api-url  */
    private function performPostRequest($url, $data) {
        $ch = curl_init($url);
        curl_setopt($ch,CURLOPT_HEADER,true);
        curl_setopt($ch, CURLOPT_URL, $url); // set url to post to
        curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
        curl_setopt($ch, CURLOPT_POST, 1); // set POST method
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        $response = curl_exec( $ch );
        $location = $this->http_parse_headers($response, 'Location');
        return $location;
    }

    function http_parse_headers( $header, $headername ) {
        $retVal = array();
        $fields = explode("\r\n", preg_replace('/\x0D\x0A[\x09\x20]+/', ' ', $header));
        foreach( $fields as $field ) {
            if( preg_match('/('.$headername.'): (.+)/m', $field, $match) ) {
                return $match[2];
            }
        }
        return $retVal;
    }
}

?>