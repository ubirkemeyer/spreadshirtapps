<?php
    // This product creation script shows you how to create new products with existing
    // vector designs on the Spreadshirt platform using Spreadshirt API v1.

    $productTypeId = "6";
    $printTypeId = "14";
    $printColorIds = "13,20";
    $productTypeAppearanceId = "1";
    $productTypePrintAreaId = "4";
    $designId = "m2484607-1";

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

    // 2. Get product type data
    $attributes = $shop->productTypes->attributes($namespaces['xlink']);
    $productTypeUrl = $attributes->href . "/" . $productTypeId;
    $ch = curl_init($productTypeUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);        

    $productType = new SimpleXMLElement($result);

    // 3. Get design data
    $attributes = $shop->designs->attributes($namespaces['xlink']);
    $designUrl = $attributes->href . "/" . $designId;
    $ch = curl_init($designUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $design = new SimpleXMLElement($result);

    // 4. Prepare product
    // get positioning data for selected product type
    $printArea = null;
    foreach ($productType->printAreas->printArea as $current) {
        if ($current['id'] == $productTypePrintAreaId) {
            $printArea = $current;
        }
    }

    $product = new SimpleXMLElement(getFileData("product.xml"));
    $product->productType['id'] = $productTypeId;
    $product->appearance['id'] = $productTypeAppearanceId;
    $configuration = $product->configurations->configuration;
    $configuration->printArea['id'] = $productTypePrintAreaId;
    $configuration->printType['id'] = $printTypeId;
    $configuration->offset->x =
              ((doubleval($printArea->boundary->size->width) - doubleval($design->size->width)) / 2);
    $configuration->offset->y =
              ((doubleval($printArea->boundary->size->height) - doubleval($design->size->height)) / 4);
    $image = $product->configurations->configuration->content->svg->image;   
    $image['width'] = $design->size->width;
    $image['height'] = $design->size->height;
    $image['designId'] = $designId;
    $image['printColorIds'] = $printColorIds;

    // 5. Create product
    $attributes = $shop->products->attributes($namespaces['xlink']);
    $productsUrl = $attributes->href;

    $header = array();
    $header[] = createSprdAuthHeader("POST", $productsUrl);
    $header[] = "Content-Type: application/xml";

    $ch = curl_init($productsUrl);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $product->asXML());
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, true);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $productUrl = parseHttpHeaders($result, "Location");

    $ch = curl_init($productUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $product = new SimpleXMLElement($result);
    $resource = $product->resources->resource[0];
    $attributes = $resource->attributes($namespaces['xlink']);

    echo '<html><body>';
    echo 'Product available at: <a href="' . $productUrl . '">' . $productUrl . '</a></br>';
    echo 'Product image is available at: <a href="' . $attributes->href . '">' . $attributes->href . '</a></br>';
    echo '<img src="' . $attributes->href . '?width=1000"/>';
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