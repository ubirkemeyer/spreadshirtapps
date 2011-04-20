<?php
// Get the REST call path from the AJAX application
// Is it a POST or a GET?
define('HOST', '//localhost');
define('SPREADSHIRT_API_KEY', '');
define('SPREADSHIRT_API_SECRET', '');

$method = $_SERVER['REQUEST_METHOD'];
//$referrer = $_SERVER['HTTP_REFERER'];
//$pos = stripos($referrer, HOST);
$pos = 1;

if ($pos != false) {
    switch ($method) {
      case 'PUT':
        $url = encodeUrl($_GET['url']);

        $data = @file_get_contents('php://input');
        if (strpos($url, 'image-server/v1') != 0 && strpos($data, 'xlink:href')) {
            $start = strpos($data, 'xlink:href="') + 12;
            $end = strpos($data, '"', $start);
            $imageUrl = substr($data, $start, $end-$start);

            $session = curl_init($imageUrl);
            curl_setopt($session, CURLOPT_HEADER, false);
            curl_setopt($session, CURLOPT_RETURNTRANSFER, true);
            $response = curl_exec($session);
            curl_close($session);

            /*$filename = tempnam(sys_get_temp_dir(), 'upload');
            $fh = fopen($filename, "w+");
            fwrite($fh, $response);
            fflush($fh);
            fclose($fh);
            $fh = fopen($filename, "r");*/

            $session = curl_init(secureUrl('PUT', $url)."&method=PUT");
            curl_setopt($session, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
            curl_setopt($session, CURLOPT_POST, true);
            curl_setopt($session, CURLOPT_POSTFIELDS, $response);
            curl_setopt($session, CURLOPT_HTTPHEADER, getRelevantRequestHeaders());
            curl_setopt($session, CURLOPT_HEADER, true);
            curl_setopt($session, CURLOPT_RETURNTRANSFER, true);
            /*curl_setopt($session, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
            curl_setopt($session, CURLOPT_CUSTOMREQUEST, 'PUT');            
            curl_setopt($session, CURLOPT_POSTFIELDS, $response);
            //curl_setopt($session, CURLOPT_PUT, true);
            //curl_setopt($session, CURLOPT_INFILE, $fh);
            //curl_setopt($session, CURLOPT_INFILESIZE, filesize($filename));
            curl_setopt($session, CURLOPT_HTTPHEADER, getRelevantRequestHeaders());
            curl_setopt($session, CURLOPT_HEADER, true);
            curl_setopt($session, CURLOPT_RETURNTRANSFER, true);*/

            // Make the call
            $response = curl_exec($session);

            // The web service returns XML. Set the Content-Type appropriately
            header(getStatusMessage("".curl_getinfo($session, CURLINFO_HTTP_CODE)));

            echo secureUrl('PUT', $url)."&method=PUT";
            echo curl_error($session);
            //fclose($fh);
            curl_close($session);
            echo $response;

        } /*else {
        }    */
        break;
      case 'POST': 
        $url = encodeUrl($_GET['url']);

        // Open the Curl session
        $session = curl_init(secureUrl('POST', $url));

        curl_setopt($session, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_setopt($session, CURLOPT_POST, true);
        curl_setopt($session, CURLOPT_POSTFIELDS, @file_get_contents('php://input'));
        curl_setopt($session, CURLOPT_HTTPHEADER, getRelevantRequestHeaders());
        curl_setopt($session, CURLOPT_HEADER, true);
        curl_setopt($session, CURLOPT_RETURNTRANSFER, true);

        // Make the call
        $response = curl_exec($session);

        // The web service returns XML. Set the Content-Type appropriately
        header("Content-Type: application/xml", true);
        header("Location: " . parseHttpHeaders($response, "Location"), true);
        header(getStatusMessage("".curl_getinfo($session, CURLINFO_HTTP_CODE)));

        curl_close($session);
        break;
      case 'GET':
        //echo $_GET['url'] . "#";
        $url = encodeUrl($_GET['url']);
        //echo $url;
        if (isset($_GET['secure']) && $_GET['secure'] == "true")
            $url = secureUrl("GET", $url);

        // Open the Curl session
        $session = curl_init($url);

        // fix this security leak
        curl_setopt($session, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_setopt($session, CURLOPT_HTTPHEADER, getRelevantRequestHeaders());
        curl_setopt($session, CURLOPT_HEADER, false);
        curl_setopt($session, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($session, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($session, CURLOPT_SSL_VERIFYHOST, 2);

        $response = curl_exec($session);
        header("Content-Type: application/xml");
        header(getStatusMessage("".curl_getinfo($session, CURLINFO_HTTP_CODE)));

        echo $response;
        curl_close($session);
        break;
      case 'HEAD':
        rest_head($request);
        break;
      case 'DELETE':
        rest_delete($request);
        break;
      case 'OPTIONS':
        rest_options($request);
        break;
      default:
        rest_error($request);
        break;
    }
} else {
   header("HTTP/1.0 400 Bad Request");
}

function secureUrl($method, $url) {
        $timestamp = time()*1000;
        $signature=sha1($method." ".$url." ".$timestamp." ".SPREADSHIRT_API_SECRET);
        $url.="?apiKey=".SPREADSHIRT_API_KEY."&sig=".$signature."&time=".$timestamp;
        return $url;
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

function encodeUrl($url) {
    $newUrl = "";
    $params = preg_split('/[?&]/', $url);
    for ($i = 0; $i < sizeof($params); $i++) {
       if ($i == 0) {
          $newUrl .= $params[$i];
       } else if ($i == 1) {
          $newUrl .= "?";
          $keyvalue = preg_split('/=/', $params[$i]);
          $newUrl .= $keyvalue[0]."=".(sizeof($keyvalue) == 2 ? rawurlencode($keyvalue[1]) : "");
       } else if ($i > 1) {
          $newUrl .= "&";
          $keyvalue = preg_split('/=/', $params[$i]);
          $newUrl .= $keyvalue[0]."=".(sizeof($keyvalue) == 2 ? rawurlencode($keyvalue[1]) : "");
       }
    }
    return $newUrl;
}

function getRelevantRequestHeaders() {
        $headers = getRequestHeaders();
        $relevantHeaders = array();
        foreach ($headers as $header => $value) {
            if ($header == "X-Authorization") {
                $relevantHeaders[] = "Authorization: " . rawurldecode($value);
            }
            else if ($header == "User-Agent" || $header == "Accept" || $header == "Accept-Language" ||
                    $header == "Accept-Charset" || $header == "Content-Type") {
                $relevantHeaders[] = $header. ": " . $value;
            }
        // || $header == "Accept-Encoding"
        }
        return $relevantHeaders;
}

function getRequestHeaders() {
    $headers = array();
    foreach($_SERVER as $key => $value) {
        //echo str_replace(' ', '-', ucwords(str_replace('_', ' ', strtolower(substr($key, 5)))))." ".$value."\n";
        if(strpos($key, 'HTTP_') === 0) {
            $headers[str_replace(' ', '-', ucwords(str_replace('_', ' ', strtolower(substr($key, 5)))))] = $value;
        }
    }
    return $headers;
}

function getStatusMessage($code) {
    $statusMessages = array();
    $statusMessages["100"] = "100 Continue";
    $statusMessages["101"] = "101 Switching Protocols";
    $statusMessages["200"] = "200 OK";
    $statusMessages["201"] = "201 Created";
    $statusMessages["202"] = "202 Accepted";
    $statusMessages["203"] = "203 Non-Authoritative Information";
    $statusMessages["204"] = "204 No Content";
    $statusMessages["205"] = "205 Reset Content";
    $statusMessages["206"] = "206 Partial Content";
    $statusMessages["300"] = "300 Multiple Choices";
    $statusMessages["301"] = "301 Moved Permanently";
    $statusMessages["302"] = "302 Found";
    $statusMessages["303"] = "303 See Other";
    $statusMessages["304"] = "304 Not Modified";
    $statusMessages["305"] = "305 Use Proxy";
    $statusMessages["306"] = "306 (Unused)";
    $statusMessages["307"] = "307 Temporary Redirect";
    $statusMessages["400"] = "400 Bad Request";
    $statusMessages["401"] = "401 Unauthorized";
    $statusMessages["402"] = "402 Payment Required";
    $statusMessages["403"] = "403 Forbidden";
    $statusMessages["404"] = "404 Not Found";
    $statusMessages["405"] = "405 Method Not Allowed";
    $statusMessages["406"] = "406 Not Acceptable";
    $statusMessages["407"] = "407 Proxy Authentication Required";
    $statusMessages["408"] = "408 Request Timeout";
    $statusMessages["409"] = "409 Conflict";
    $statusMessages["410"] = "410 Gone";
    $statusMessages["411"] = "411 Length Required";
    $statusMessages["412"] = "412 Precondition Failed";
    $statusMessages["413"] = "413 Request Entity Too Large";
    $statusMessages["414"] = "414 Request-URI Too Long";
    $statusMessages["415"] = "415 Unsupported Media Type";
    $statusMessages["416"] = "416 Requested Range Not Satisfiable";
    $statusMessages["417"] = "417 Expectation Failed";
    $statusMessages["500"] = "500 Internal Server Error";
    $statusMessages["501"] = "501 Not Implemented";
    $statusMessages["502"] = "502 Bad Gateway";
    $statusMessages["503"] = "503 Service Unavailable";
    $statusMessages["504"] = "504 Gateway Timeout";
    $statusMessages["505"] = "505 HTTP Version Not Supported";

    if (isset($statusMessages[$code]))
        return "HTTP/1.1 ".$statusMessages[$code];
    else
        return "HTTP/1.1 ".$code;
}
?>
