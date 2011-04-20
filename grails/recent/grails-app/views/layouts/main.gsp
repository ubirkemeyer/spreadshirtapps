<html>
<head>
  <title><g:layoutTitle default="Grails"/></title>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery-ui-1.8.4.custom.css')}"/>
  <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
  <g:layoutHead/>
  <g:javascript library="application"/>
  <script type="text/javascript">


    function updateCode(url, width, height) {
      var iframe = "<iframe src=\"" + url + "\" id=\"recent\" width=\"" + width + "\" height=\"" + height + "\"></iframe>"
      document.getElementById('code').value = iframe
    }
    function selectAllCode() {
      document.getElementById('code').select()
    }

    function updatePreview(evt) {
      if (evt != null) {
        if (evt.keyCode != 13 && evt.keyCode != 9) return;
      }

      var shopId = document.getElementById('shop').value
      var width = parseInt(document.getElementById('width').value)
      var fontFamily = document.getElementById('fontFamily').value
      var fontSize = parseInt(document.getElementById('fontSize').value)
      var headline = document.getElementById('headline').checked
      var name = document.getElementById('name').checked
      var price = document.getElementById('price').checked
      var colorAlternatives = document.getElementById('colorAlternatives').checked
      var availableSizes = document.getElementById('availableSizes').checked
      var backgroundColor = document.getElementById('backgroundColor').value
      var textColor = document.getElementById('textColor').value

      if (!isNaN(width)) {
        document.getElementById('recent').width = width
      } else {
        width = 300
      }
      var height = parseInt(document.getElementById('height').value)
      if (!isNaN(height)) {
        document.getElementById('recent').height = height
      } else {
        height = 300
      }
      if (isNaN(fontSize)) {
        fontSize = 14
        document.getElementById('fontSize').value = fontSize
      }
      document.getElementById('fontSize').value = fontSize


      var url = getURL(shopId, width, height, fontFamily, fontSize, headline, name, price, colorAlternatives, availableSizes, backgroundColor, textColor)

      document.getElementById('recent').src = url

      updateCode(url, width, height)
    }


    function getURL(shopId, width, height, fontFamily, fontSize, headline, name, price, colorAlternatives, availableSizes, backgroundColor, textColor) {
      var url = 'http://localhost:8080/recent/recent/index/?shop=' + shopId
      url += '&width=' + width + '&height=' + height + '&fontFamily=' + fontFamily
      url += '&fontSize=' + fontSize + '&headline=' + headline
      url += '&name=' + name + '&price=' + price
      url += '&backgroundColor=' + backgroundColor + '&textColor=' + textColor
      url += '&colorAlternatives=' + colorAlternatives + '&availableSizes=' + availableSizes
      return url
    }

  </script>
</head>
<body>

<g:layoutBody/>
</body>
</html>