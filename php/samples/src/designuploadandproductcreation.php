<?php
    // The script shows how to upload a pixel design and create a product using a
    // digi print type.

    $productTypeId = "6";
    // digi print types 17 white 19 other colors
    $printTypeId = "17";
    $printColorIds = "";
    $printColorRGBS = "";
    $productTypeAppearanceId = "1";
    $productTypePrintAreaId = "4";

    // 1. Get shop data
    $shopUrl = "http://api.spreadshirt.net/api/v1/shops/40000";
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

    // 3. Get print type data
    $attributes = $shop->printTypes->attributes($namespaces['xlink']);
    $printTypeUrl = $attributes->href . "/" . $printTypeId;
    $ch = curl_init($printTypeUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $printType = new SimpleXMLElement($result);

    // 4. Create design entity via data api
    $xml = getFileData("design.xml");

    // Initialize handle and set options
    $attributes = $shop->designs->attributes($namespaces['xlink']);
    $header = array();
    $header[] = createSprdAuthHeader("POST", $attributes->href);
    $header[] = "Content-Type: application/xml";
    $ch = curl_init($attributes->href);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $xml);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, true);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $dataUrl = parseHttpHeaders($result, "Location");

    echo "Design URL: ".$dataUrl."\n";

    // 5. Fetch design data to retrieve upload url
    // caching workaround
    $ch = curl_init($dataUrl.'?nocache=123');
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $start = strpos($result, "resource xlink:href=\"")+21;
    $end = strpos($result, "\"", $start);
    $imageUrl = substr($result, $start, $end-$start);

    echo "Image URL: ".$imageUrl."\n";

    // 6. Upload design data via image API
    $imageData = getFileData("design.png");

    $header = array();
    $header[] = createSprdAuthHeader("PUT", $imageUrl);
    $header[] = "Content-Type: image/png";

    $ch = curl_init($imageUrl."?method=PUT");
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $imageData);
    curl_setopt($ch, CURLOPT_HEADER, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    curl_close($ch);

    // 7. Get design data
    $ch = curl_init($dataUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $design = new SimpleXMLElement($result);

    // 8. Prepare product
    // get positioning data for selected product type
    $printArea = null;
    foreach ($productType->printAreas->printArea as $current) {
        if ($current['id'] == $productTypePrintAreaId) {
            $printArea = $current;
        }
    }

    // calculate pixel to mm using print type dpi
    $dpi = doubleval($printType->dpi);
    $designWidth = doubleval($design->size->width) * 25.4 / $dpi;
    $designHeight = doubleval($design->size->height) * 25.4 / $dpi;

    $scaleFactor = 1.0;
    if ($designWidth > $printArea->boundary->size->width)
        $scaleFactor = $printArea->boundary->size->width / $designWidth;
    $designWidth = $designWidth * $scaleFactor;
    $designHeight = $designHeight * $scaleFactor;

    echo $scaleFactor.' '.$designWidth.' '.$designHeight;

    $product = new SimpleXMLElement(getFileData("product.xml"));
    $product->productType['id'] = $productTypeId;
    $product->appearance['id'] = $productTypeAppearanceId;
    $configuration = $product->configurations->configuration;
    $configuration->printArea['id'] = $productTypePrintAreaId;
    $configuration->printType['id'] = $printTypeId;
    $configuration->offset->x =
              ((doubleval($printArea->boundary->size->width) - doubleval($designWidth)) / 2);
    $configuration->offset->y =
              ((doubleval($printArea->boundary->size->height) - doubleval($designHeight)) / 4);
    $image = $product->configurations->configuration->content->svg->image;
    $image['width'] = $designWidth;
    $image['height'] = $designHeight;
    $image['designId'] = 'u'.$design['id'];
    $image['printColorIds'] = $printColorIds;

    // 9. Create product
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
        $apiKey = "...";
        $secret = "...";
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
