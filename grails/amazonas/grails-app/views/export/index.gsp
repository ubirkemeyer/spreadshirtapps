<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title>Amazon Flat File Export</title>
</head>
<body>
<div class="nav">

  <g:render template="/navigation"/>
</div>
<g:javascript library="prototype"/>

<script type="text/javascript">
  function startAnimation() {
    document.getElementById('submit').style.display = 'none'
    document.getElementById('download').innerHTML = ''
    document.getElementById('animation').style.display = 'block'
  }
  function stopAnimation() {
    document.getElementById('submit').style.display = 'block'
    document.getElementById('animation').style.display = 'none'
  }
</script>

<div class="body">
  <div style="margin-top:10px"><h2>Create a flat file from your shop to import in Amazon Seller Central.</h2></div>
  <g:formRemote url="[controller: 'export', action: 'export']" update="download" name="export" onLoading="startAnimation();" onLoaded="stopAnimation();">
    <table style="margin-top:20px;width:50%">
      <tbody>
      <tr>
        <td><strong>Shop&nbsp;Id</strong></td><td><g:textField name="shopId" value=""/></td>
        <td><strong>Brand</strong></td><td><g:textField name="brand"/></td>
        <td><strong>Operation</strong></td><td><g:select name="operation" from="${operationsAvailable}"/></td>
        <td>
          <g:submitButton name="next" value="Next" id="submit"/>
          <img src="${resource(dir: 'images', file: 'ajax-loader.gif')}" alt="Loading" id="animation" style="display:none" style="padding-right:100px"/>
        </td>
      </tr>
      </tbody>
    </table>
    <div>
    </div>
  </g:formRemote>


  <div id="download" style="margin-top:25px">${listing}</div>
</div>
</body>
</html>