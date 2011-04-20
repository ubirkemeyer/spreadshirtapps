<?php

require_once 'sprd/ShirtOfDay.php';
// initialize spreadshirt library (load data)
$aod = new ShirtOfDay();
?>
    <div class="sprdShirtArea">
      <h2>Donate us!</h2>
      <div id="test" title="Donate us and buy our shirt!<BR/> Click here for more informations.">
        <div id="testee" class="sprdHeadline">Buy our shirt !</div>
        <div id="article-button" class="sprdArticleButton" align="center">
          <?php echo $aod->getArticlePreviewImage("1", "200", "200"); ?>
        </div>
      </div>
      <div>
        Untersützt uns mit dem Kauf eines T-Shirts zum Preis von <?php echo $aod->getPriceString();?>
        <a title="Details" class="sprdDetailsButton" href="#"><em>mehr Infos...</em></a>
      </div>
      <div class="sprdArticleDetails">
        Mit jedem verkauften
        <?php echo "<strong>".$aod->getShortName()." </strong>"?> unterstützt Ihr
        die Weiterentwicklung unserer Website.
        <div id="buynow" class="sprdArticleButton">jetzt kaufen</div>
      </div>
    <br>

  </div>





<div id="dialog" title="Article Details">

    <div>
        <FORM action="sprd/checkout.php" method=post name="goToCheckout">
            <input name="appearance" type="hidden" value="not-used-yet">
            <input name="size" type="hidden" value="2">
        </FORM>
    </div>
    <h2 align="center"><?php echo $aod->getShortName(); ?></h2>

    <div>
        <div align="right" style="height: 150px;">
            <img id="article-image" src="<?php echo $aod->getArticleImageUrl("1", "250", "250"); ?>" alt="Front" />
        </div>
        <div id="carousel"align="left" style="height: 150px;">
          <a href=""><img src="<?php echo $aod->getArticleImageUrl("1", "100", "100"); ?>" width="100%" alt="front"
                          onclick="'article-image', changeImage('<?php echo $aod->getArticleImageUrl("1", "250", "250"); ?>')"/></a>
          <a href=""><img src="<?php echo $aod->getArticleImageUrl("2", "100", "100"); ?>" width="100%" alt="back"
                 onclick="'article-image', changeImage('<?php echo $aod->getArticleImageUrl("2", "250", "250"); ?>')"/></a>
        </div>
    </div>

    <p><?php echo $aod->getDescription()?></p>
    <div class="dialogSizeElement">Größe <?php echo $aod->getSizeElement()?></div>
    <div class="dialogDescription">
        <span class="product-price"><?php echo $aod->getPriceString()?></span>
        <span style="font-size: smaller;display: block;">zzgl.Versand</span>
    </div>
</div>





