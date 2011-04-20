<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License

Name       : EarthlingTwo
Description: A two-column, fixed-width design with dark color scheme.
Version    : 1.0
Released   : 20090918
-->
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>EarthlingTwo by Free CSS Templates</title>
    <link href="resources/style.css" rel="stylesheet" type="text/css" media="screen" />

    <!-- widget initialization, javascript loader -->
    <?php include_once 'jsWidget/widgetLoader.php';?>

  </head>
  <body>
    <div id="wrapper">
      <div id="header">
        <div id="logo">
          <h1><a href="#">Shirt-of-Day Demo</a></h1>
          <p>by <a href="http://www.spreadshirt.net">spreadshirt-dev</a></p>
        </div>
      </div>
      <!-- end #header -->
      <div id="menu">
        <ul>
          <li class="current_page_item"><a href="#">Home</a></li>
          <li><a href="#">Services</a></li>
          <li><a href="#">Portfolio</a></li>
          <li><a href="#">About</a></li>
          <li><a href="#">Contact</a></li>
        </ul>
      </div>
      <!-- end #menu -->
      <div id="page">
        <div id="content">
          <div id="banner"><img src="resources/images/img07.jpg" alt=""/></div>
          
          <div class="post">
            <h2 class="title"><a href="#">Welcome</a></h2>
            <p class="meta">A small description of our php demo and how to use it!</p>
            <div class="entry">
                <p>This demo site contains a small description of our Shirt-Of-Day
                   PHP-Widget and some technical details. 
                   You can see the widget in action at the right side. There is
                   a <strong>DONATE US!</strong> part which contains all
                   informations about a featured T-Shirt. You can expand these
                   section and retrieve more informations. If you want to buy
                   the shirt you can click the button (more informations) or
                   simple click on the shirt.
                   In such cases a small window pops up where you can select
                   your T-Shirt size and go to the Spreadshirt checkout....
                   <BR/>
                   By the way, most of other content stuff at this demo site
                   doesn't work (e.g. links), this is not a bug its a f.......
                </p>
                <h4>Requirements</h4>
                <p>You need PHP with curl package for the core functionality
                    (see <a href="demo.php">raw demo</a>).
                    This demo site
                    is driven by PHP and some additional javascript
                    (<a href="http://www.jquery.org">jQuery</a>) to get a
                    smoother user handling. All you need is to put
                    <strong>2 lines of code</strong> into your site, copy
                    the widget source files and setup the widget.
                </p>
                <h3>Getting started</h3>
                <p>The PHP-Widget is pre-configured for using out-of-the box.
                  For embedding the widget in your website,
                   copy the PHP-widget source files into the
                   root of your web folder and put the following lines of code:
                </p>
                  <pre class="coding"><code>
  &lt;head>
    ...
    &lt;!-- widget initialization, javascript loader -->
    &lt;?php include_once 'jsWidget/widgetLoader.php';?>
    ...
  &lt;/head>
  &lt;body>
    ...
  &lt;!-- place the widget somewhere-->
  &lt;?php include("jsWidget/widget.php"); ?>
    ...
  &lt;/body>
                </code></pre>
                <p>into the php source files of your website. Thats all! Now
                you can see a nice shirt from our marketplace.</p>
                <h4>Configure the shirt</h4>
                <p>
                You need the <strong>shop-id</strong> of your shop and
                (optional) the <strong>article-id</strong> of an article of 
                this shop.
                Put these informations into the <code>sprd/ShirtOfDay.php</code> file.
                If you set only the <strong>shop-id</strong> the widget loads
                and displays the first article from this shop.
                </p>
                <h4>Setup checkout</h4>
                <p>
                  For placing orders and getting a working checkout you need a
                  Spreadshirt API key. This is a security feature for
                  shop and partner accounts.
                  Please request at Spreadshirt customer support for your
                  own key. Put your API key and your secrect into
                  the <code>sprd/CheckoutController.php</code> file. Now a visitor
                  of your site can buy your shirts.
                </p>
            </div>
          </div>
          <div style="clear: both;">&nbsp;</div>

          <div class="post">
            <h2 class="title"><a href="#">Template</a></h2>
            <p class="meta">About the html template</p>
            <div class="entry">
              <p>This is <strong>EarthlingTwo</strong>, a free template by <a href="http://www.nodethirtythree.com">nodethirtythree</a> and <a href="http://www.freecsstemplates.org">Free CSS Templates</a>.  It’s kind of a sequel to an older template I did for FCT a long time ago named <a href="http://www.freecsstemplates.org/preview/earthling">Earthling</a>.</p>
              <p>This template is released under the <a href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution</a> license, so use it however you like, just keep the links back to our sites. The scenic photo above is a public domain work from <a href="http://www.pdphoto.org">PDPhoto.org</a>. Be sure to check out my other work at <a href="http://www.nodethirtythree.com/">my site</a> or follow me on <a href="http://twitter.com/nodethirtythree/">Twitter</a> for news and updates. Have fun.</p>
              <p class="links"><a href="#" class="more">Read More</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="comments">Comments (33)</a></p>
            </div>
          </div>




        </div>
        <!-- end #content -->
        <div id="sidebar">
          <ul>
            <li>
              <h2>Veroeros sit dolore</h2>
              <p><strong>Donec turpis orci</strong> facilisis et ornare eget, sagittis eu massa. Quisque dui diam, euismod et lobortis sed etiam lorem ipsum dolor etiam nullam et faucibus. <a href="#">More&#8230;</a></p>
            </li>
            <li>
              <!-- place the widget -->
              <?php include("jsWidget/widget.php"); ?></li>
            <li>
              <h2>Categories</h2>
              <ul>
                <li><span>09.13.09</span><a href="#">Vestibulum risus vitae</a></li>
                <li><span>09.13.09</span><a href="#">Condimentum et molestie</a></li>
                <li><span>09.13.09</span><a href="#">Facilisis sed vestibulum</a></li>
                <li><span>09.13.09</span><a href="#">Ipsum primis et sed luctus </a></li>
                <li><span>09.13.09</span><a href="#">Ultrices posuere nulla </a></li>
                <li><span>09.13.09</span><a href="#">Accumsan lorem sodales </a></li>
                <li><span>09.13.09</span><a href="#">Scelerisque consectetur </a></li>
                <li><span>09.13.09</span><a href="#">Maecenas quam aliquet</a></li>
              </ul>
            </li>
          </ul>
        </div>
        <!-- end #sidebar -->
        <div style="clear: both;">&nbsp;</div>
      </div>
      <!-- end #page -->
    </div>
    <div id="footer-content">
      <div class="column1">
        <h2>Volutpat quisque sed et aliquam</h2>
        <p><strong>Maecenas ut ante</strong> eu velit laoreet tempor accumsan vitae nibh. Aenean commodo, tortor eu porta convolutpat elementum. Proin fermentum molestie erat eget vehicula. Aenean eget tellus mi. Fusce scelerisque odio quis ante bibendum sollicitudin. Suspendisse potenti. Vivamus quam odio, facilisis at ultrices nec, sollicitudin ac risus. Donec ut odio ipsum, sed tincidunt. <a href="#">Learn more&#8230;</a></p>
      </div>
      <div class="column2">
        <ul class="list">
          <li><a href="#">Tempor accumsan vitae sed nibh dolore</a></li>
          <li><a href="#">Aenean commodo, tortor eu porta veroeros</a></li>
          <li><a href="#">Fermentum molestie erat eget consequat</a></li>
          <li><a href="#">Donec vestibulum interdum diam etiam</a></li>
          <li><a href="#">Vehicula aenean eget sed tellus blandit</a></li>
        </ul>
      </div>
    </div>

<?php

  require_once 'sprd/ShirtOfDay.php';
  // initialize spreadshirt library (load data)
  $aod = new ShirtOfDay();
?>


    <div id="footer">
      <p> (c) 2009 Sitename.com. Design by <a href="http://www.nodethirtythree.com">nodethirtythree</a> and <a href="http://www.freecsstemplates.org">Free CSS Templates</a>.</p>
    </div>
    <!-- end #footer -->

  </body>

</html>
