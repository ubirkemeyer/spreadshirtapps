<html>
<head>
  <title>Recently added Spreadshirt Article in an iframe</title>
  <meta name="layout" content="main"/>
  <style type="text/css" media="screen">

  #nav {
    margin-top: 20px;
    margin-left: 30px;
    width: 228px;
    float: left;

  }

  .homePagePanel * {
    margin: 0px;
  }

  .homePagePanel .panelBody ul {
    list-style-type: none;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody h1 {
    text-transform: uppercase;
    font-size: 1.1em;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody {
    background: url(images/leftnav_midstretch.png) repeat-y top;
    margin: 0px;
    padding: 15px;
  }

  .homePagePanel .panelBtm {
    background: url(images/leftnav_btm.png) no-repeat top;
    height: 20px;
    margin: 0px;
  }

  .homePagePanel .panelTop {
    background: url(images/leftnav_top.png) no-repeat top;
    height: 11px;
    margin: 0px;
  }

  h2 {
    margin-top: 15px;
    margin-bottom: 15px;
    font-size: 1.2em;
  }

  #pageBody {
    margin-left: 20px;
    margin-right: 20px;
  }
  </style>
</head>

<body>
<div id="pageBody">
  <h1>Create a "Recent Article" iframe for your shop</h1>
  <table border="0">
    <tr>
      <td width="320px">
        <table>
          <tr height="8px">
            <td width="125px">
              <strong>Shop ID</strong>
            </td>
            <td>
              <input type="text" name="shop" id="shop" value="${shopId}" onkeydown="updatePreview(event)"/>
            </td>

          </tr>
          <tr height="8px">
            <td><strong>Width</strong></td>
            <td>
              <input type="text" size="5" name="Width" id="width" value="${width}" onkeydown="updatePreview(event)"/>px
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Height</strong></td>
            <td>
              <input type="text" size="5" name="Width" id="height" value="${height}" onkeydown="updatePreview(event)"/>px
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Font Family</strong></td>
            <td>
              <select name="fontFamily" id="fontFamily" size="1" onchange="updatePreview()">
                <g:each in="${fontFamilies}">
                  <option value="${it}" selected="${it == fontFamily ? 'SELECTED' : ''}">${it}</option>
                </g:each>
              </select>
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Font Size</strong></td>
            <td>
              <input type="text" maxlength="3" size="4" name="fontSize" id="fontSize" value="${fontSize}" onkeydown="updatePreview(event)"/>
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Background Color</strong></td>
            <td>
              <input type="text" name="backgroundColor" id="backgroundColor" value="${backgroundColor}" maxlength="7" size="7" onkeydown="updatePreview(event)"/>
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Text Color</strong></td>
            <td>
              <input type="text" name="textColor" id="textColor" value="${textColor}" maxlength="7" size="7" onkeydown="updatePreview(event)"/>
            </td>
          </tr>

          <tr height="8px">
            <td><strong>Headline</strong></td>
            <td>
              <input type="checkbox" name="headline" id="headline" <g:if test="${headline}">checked="checked"</g:if> onchange="updatePreview()">
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Name</strong></td>
            <td>
              <input type="checkbox" name="name" id="name" <g:if test="${name}">checked="checked"</g:if> onchange="updatePreview()">
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Price</strong></td>
            <td>
              <input type="checkbox" name="price" id="price" <g:if test="${price}">checked="checked"</g:if> onchange="updatePreview()">
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Color Alternatives</strong></td>
            <td>
              <input type="checkbox" name="colorAlternatives" id="colorAlternatives" <g:if test="${colorAlternatives}">checked="checked"</g:if> onchange="updatePreview()">
            </td>
          </tr>
          <tr height="8px">
            <td><strong>Available Sizes</strong></td>
            <td>
              <input type="checkbox" name="availableSizes" id="availableSizes" <g:if test="${availableSizes}">checked="checked"</g:if> onchange="updatePreview()">
            </td>
          </tr>
          <tr height="8px">
            <td>&nbsp;</td>
            <td>
              <input type="button" name="Preview" value="Preview" onclick="updatePreview()"/>
            </td>
          </tr>

        </table>
        <table>
          <tr>
            <td>
              <div><strong>Code</strong></div>
              <textarea rows="200" cols="2000" style="width:100%" id="code" onfocus="selectAllCode()" readonly="readonly"></textarea>
            </td>
          </tr>
        </table>
      </td>
      <td>
        <iframe src="" id="recent" width="300" height="300">
        </iframe>
      </td>
    </tr>

  </table>
</div>
<script type="text/javascript">
  updatePreview()
</script>
</body>

</html>
