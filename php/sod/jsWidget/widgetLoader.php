    <!-- css for the widget -->
    <link href="jsWidget/sprd.css" rel="stylesheet" type="text/css" media="screen, projection" />

    <link href="jsWidget/jquery/style.css" rel="stylesheet" type="text/css" media="screen"/>

    <!-- jQuery libraries -->
    <script type="text/javascript" src="jsWidget/lib/jquery.min.js"></script>
    <script type="text/javascript" src="jsWidget/lib/jquery-ui.min.js"></script>
    <script type="text/javascript" src="test/interface.js"></script>
    <!-- Tooltip/Balloon -->
    <script type="text/javascript" src="jsWidget/lib/jquery.bt.min.js"></script>


    <script type="text/javascript">
      $(document).ready(function(){
//        $('#myGallery').spacegallery({loadingClass: 'loading'});

         // t-shirt details text slider
         $("a.sprdDetailsButton").click(function(){
             $("div.sprdArticleDetails").slideToggle("slow"); return false;
         });

         // dialog opener
         $("div.sprdArticleButton").click(function(){
             $("#dialog").dialog('open'); return false;
         });

         // t-shirt dialog
         $("#dialog").dialog({
                    autoOpen: false,
                    bgiframe: false,
                    modal: true,
                    width:500,
                    buttons: {
                        kaufen: function() {
                            $(this).dialog('close');
                            // read selected size and put the value into the form
                            document.goToCheckout.size.value= $("#size-select").val();
                            // basket stuff and redirect to spreadshirt
                            document.goToCheckout.submit();
                        }
                    }
         });

      
        $('#test').bt({
          cornerRadius: 20,
          padding: 20,
          strokeWidth: 2,
          strokeStyle: '#CC0',
          fill: 'rgba(0, 0, 0, .6)',
          cssStyles: {color: '#FFFFFF', fontWeight: 'bold'},

          shadow: true,
          shadowOffsetX: 3,
          shadowOffsetY: 3,
          shadowBlur: 8,
          shadowColor: 'rgba(0,0,0,.9)',
          shadowOverlap: false,
          noShadowOpts: {strokeStyle: '#999', strokeWidth: 2},
          positions: ['left', 'top']
        });

         $('#carousel').Carousel({
            itemWidth: 110,
            itemHeight: 62,
            itemMinWidth: 50,
            items: 'a',
            reflections: .1,
            rotationSpeed: 1.8,
                    slowOnHover: true,
                    slowOnOut: false
          });        

      });

      function changeImage(imageID, imageSource){
         document.getElementById(imageID).src = imageSource;
      }

  </script>
