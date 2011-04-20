<?php
    // This basket creation and checkout script shows you how to create a basket on the
    // Spreadshirt platform using Spreadshirt API v1 and forward customers to the
    // Spreadshirt checkout.

    // 1. Get shop data
    $shopUrl = "http://api.spreadshirt.net/api/v1/shops/205909";
    $ch = curl_init($shopUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $shop = new SimpleXMLElement($result);
    $namespaces = $shop->getNamespaces(true);

    // 2. Get article data
    $attributes = $shop->articles->attributes($namespaces['xlink']);
    $articlesUrl = $attributes->href;
    $ch = curl_init($articlesUrl . "?query=&fullData=true");
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $articles = new SimpleXMLElement($result);
    $article = $articles->article[0];
    $attributes = $article->attributes($namespaces['xlink']);
    $articleUrl = $attributes->href;
    $appearanceId = "".$article->product->appearance['id'];
    $sizeId = null;

    // 3. Get product type data
    $attributes = $article->product->productType->attributes($namespaces['xlink']);
    $productTypeUrl = $attributes->href;
    $ch = curl_init($productTypeUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    // 4. Find valid size
    $productType = new SimpleXMLElement($result);
    foreach ($productType->stockStates->stockState as $stockState) {
        if ($stockState->appearance['id'] == $appearanceId &&
            $stockState->available == "true") {
            $sizeId = $stockState->size['id'];
            break;
        }
    }

    // 5. Create basket
    $basket = new SimpleXMLElement(getFileData("basket.xml"));
    $basketItem = new SimpleXMLElement(getFileData("basketitem.xml"));
    $itemAttributes = $basketItem->element->attributes($namespaces['xlink']);
    $itemAttributes->href = $articleUrl;
    $basketItem->element->properties->property[0] = $appearanceId;
    $basketItem->element->properties->property[1] = $sizeId;

    $attributes = $shop->baskets->attributes($namespaces['xlink']);
    $basketsUrl = $attributes->href;

    $header = array();
    $header[] = createSprdAuthHeader("POST", $basketsUrl);
    $header[] = "Content-Type: application/xml";

    $ch = curl_init($basketsUrl);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $basket->asXML());
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, true);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $basketUrl = parseHttpHeaders($result, "Location");

    // 6. Create basket item
    $basketItemsUrl = $basketUrl."/items";

    $header = array();
    $header[] = createSprdAuthHeader("POST", $basketItemsUrl);
    $header[] = "Content-Type: application/xml";

    $ch = curl_init($basketItemsUrl);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $basketItem->asXML());
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, true);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    // 7. Get checkout url
    $basketCheckoutUrl = $basketUrl."/checkout";

    $header = array();
    $header[] = createSprdAuthHeader("GET", $basketCheckoutUrl);
    $header[] = "Content-Type: application/xml";

    $ch = curl_init($basketCheckoutUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $checkoutRef = new SimpleXMLElement($result);
    $refAttributes = $checkoutRef->attributes($namespaces['xlink']);
    $checkoutUrl = $refAttributes->href;

    echo '<html><body>';
    echo 'Article is: <a href="' . $articleUrl . '">' . $articleUrl . '</a></br>';
    echo 'Basket available at: <a href="' . $basketUrl . '">' . $basketUrl . '</a></br>';
    echo 'Spreadshirt checkout URL is: <a href="' . $checkoutUrl . '">' . $checkoutUrl . '</a></br>';
    echo '</body></html>';

    function createSprdAuthHeader($method, $url) {
        $apiKey = x"your key ...";
        $secret = x"your secret ...";
        $time = time()*1000;

        $data = "$method $url $time";
        $sig = sha1("$data $secret");

        return "Authorization: SprdAuth apiKey=\"$apiKey\", data=\"$data\", sig=\"$sig\"";
    }

    function parseHttpHeaders( $header, $headername ) {
        $retVal = array();
        $fields = explode("\r\n", preg_replace('/\x0D\x0A[\x09\x20]+/', ' ', $header));
        foreach( $fields as $field ) {
            if( preg_match('/('.$headername.'): (.+)/m', $field, $match) ) {
                return $match[2];
            }
        }
        return $retVal;
    }

    function getFileData($file) {
        $fp = fopen($file, "r");
        $data = "";
        while(!feof($fp)) {
            $data .= fgets($fp, 1024);
        }
        fclose($fp);
        return $data;
    }
?>