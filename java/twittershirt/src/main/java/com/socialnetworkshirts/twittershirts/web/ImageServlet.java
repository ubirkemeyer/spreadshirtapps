package com.socialnetworkshirts.twittershirts.web;

import com.socialnetworkshirts.twittershirts.Constants;
import com.socialnetworkshirts.twittershirts.data.DataService;
import com.socialnetworkshirts.twittershirts.renderer.PNGRenderer;
import org.apache.batik.transcoder.TranscoderException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @author mbs
 */
public class ImageServlet extends BaseServlet {
    private PNGRenderer pngRenderer = new PNGRenderer();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String twitterUserId = request.getParameter(Constants.PARAM_TWITTER_USER_ID);
        String productTypeId = request.getParameter(Constants.PARAM_PRODUCT_TYPE_ID);
        String appearanceId = request.getParameter(Constants.PARAM_PRODUCT_TYPE_APPEARANCE_ID);
        String renderProductImage = request.getParameter(Constants.PARAM_RENDER_PRODUCT_IMAGE);
        if (renderProductImage == null)
            renderProductImage = "false";
        String useTwitterUserImages = request.getParameter(Constants.PARAM_USE_TWITTER_USER_IMAGES);
        if (useTwitterUserImages == null)
            useTwitterUserImages = Constants.USE_TWITTER_IMAGE;

        try {
            com.socialnetworkshirts.twittershirts.renderer.model.Svg svg =
                    DataService.getInstance().createProductImage(twitterUserId, productTypeId,
                            appearanceId, Boolean.valueOf(renderProductImage),
                            Boolean.valueOf(useTwitterUserImages));
            setContentType(response);
            setCacheHeader(response, Constants.DEFAULT_CACHE_TIME);
            pngRenderer.renderPNG(svg, response.getOutputStream());
        } catch (TranscoderException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (JAXBException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
