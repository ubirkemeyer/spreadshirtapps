<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  </head>
  <body>

      This site contains no javascript or css! Only pure PHP/HTML with static
      T-Shirt content. Have fun!

<?php
    require_once 'sprd/ArticleCDO.php';
    require_once 'sprd/ProductCDO.php';
    require_once 'sprd/ProductTypeCDO.php';

    // assign class variables and load data from sprd API
    // e.g. use shop 11 and article 1494210 or only the shop 205909
    // for a random shirt
    $article = new ArticleCDO("11", "1494210");
    //$article = new ArticleCDO("205909", NULL);
    $product = new ProductCDO($article->getProductLink());
    $productType = new ProductTypeCDO( $product->getProductTypeLink());


    echo '<div align="center">';
    echo '<h1>'.$productType->getName().'</h1>';
    echo '<img src="http://image.spreadshirt.net/image-server/v1/products/'.
          $article->getProductID().
          '/views/1,width=200,height=200.png"/>';
    echo '<BR/><strong>Preis:'.$article->getPrice().'€</strong>';

    echo '<BR/>';
    echo $productType->getDescription();
    echo '/<div>';
?>

    <div>
      <FORM action="sprd/checkout.php" method=post name="goToCheckout">
         <input name="appearance" type="hidden" value="not-used-yet"/>
         Size: 
         <?php
          echo '<select id="size-select" name="size">';
          foreach($productType->getSizes() as $key=>$val) {
            echo '<option value="'.$key.'">'.$val.'</option>' ;
          }
          echo '</select>';
         ?>
        <input type="submit" value="kaufen"/>
      </FORM>
    </div>

  </body>
</html>
