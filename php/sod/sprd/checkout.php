<?php
  // helper routine: collect the checkout data (size, appearance) and redirect
  // to index.[html, php] or to the spreadshirt checkout

  require_once 'ShirtOfDay.php';
  // initialize spreadshirt library (load data)
  $aod = new ShirtOfDay();

  $checkoutURL = $aod->goToCheckout($_POST['size'], $_POST['appearance']);

  // redirect to your website root document (index.php) because
  // the checkout url could not resolved
  if ($checkoutURL === NULL) {
    echo "Sorry, the parameters are not valid!";
    echo '<meta http-equiv="refresh" content="20; URL=index.php">';
  } else {
      // redirect to the spreadshirt checkouta
      header('Location: '.$checkoutURL);

      // you can use this stuff too
//    echo "redirecting to checkout, please wait";
//    echo '<meta http-equiv="refresh" content="0; URL='.$checkoutURL.'">';
  }
?>
