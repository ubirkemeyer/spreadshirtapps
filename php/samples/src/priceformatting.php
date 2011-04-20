<?php
    // This script shows you how to format prices using Spreadshirt API v1 and
    // Spreadshirt's shop, currency and country data.

    // 1. Get shop
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

    // 2. Get currency
    $attributes = $shop->currency->attributes($namespaces['xlink']);
    $currencyUrl = $attributes->href;
    $ch = curl_init($currencyUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $currency = new SimpleXMLElement($result);

    // 3. Get country
    $attributes = $shop->country->attributes($namespaces['xlink']);
    $countryUrl = $attributes->href;
    $ch = curl_init($countryUrl);
    curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    $result = curl_exec($ch);
    // Close the handle
    curl_close($ch);

    $country = new SimpleXMLElement($result);

    // 4. calculate the price
    $price = formatPrice("19.3", $currency->symbol, $currency->decimalCount, $currency->pattern,
                    $country->thousandsSeparator, $country->decimalPoint);

    // 5. print the price
    echo '<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/></head><body>';
    echo $price;
    echo '</body></html>';
    
    function formatPrice($price, $symbol, $decimalCount, $pattern, $thousandsSeparator, $decimalPoint) {
        // formatting settings
        $price = "" . $price;

        // split integer from cents
        $centsVal = "";
        $integerVal = "0";
        if (strpos($price, '.') != -1) {
            $centsVal = "" .substr($price, strpos($price, '.') + 1, strlen($price) - strpos($price, '.') + 1);
            $integerVal = "" .substr($price, 0, strpos($price, '.'));
        } else {
            $integerVal = $price;
        }

        $formatted = "";

        $count = 0;
        for ($j = strlen($integerVal)-1; $j >= 0; $j--) {
            $character = $integerVal[$j];
            $count++;
            if ($count % 3 == 0)
                $formatted = ($thousandsSeparator . $character) . $formatted;
            else
                $formatted = $character . $formatted;
        }
        if ($formatted[0] == $thousandsSeparator)
            $formatted = substr($formatted, 1, strlen($formatted));

        $formatted .= $decimalPoint;

        for ($j = 0; $j < $decimalCount; $j++) {
            if($j < strlen($centsVal)) {
                $formatted .= "" . $centsVal[$j];
            } else {
                $formatted .= "0";
            }
        }

        $out = $pattern;
        $out = str_replace('%', $formatted, $out);
        $out = str_replace('$', $symbol, $out);
        return $out;
    };
?>