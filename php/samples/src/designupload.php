<?php
    // The design upload script shows you how to create designs on the Spreadshirt platform
    // and upload the corresponding design data using Spreadshirt data and image API.


    // 1. Create design entity via data api
    $url = "http://api.spreadshirt.net/api/v1/shops/40000/designs";
    $header = array();
    $header[] = createSprdAuthHeader("POST", $url);
    $header[] = "Content-Type: application/xml";

    $xml = getFileData("design.xml");

    // Initialize handle and set options
    $ch = curl_init($url);
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


    // 2. Fetch design data to retrieve upload url
    $ch = curl_init($dataUrl);
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


    // 3. Upload design data via image API
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
