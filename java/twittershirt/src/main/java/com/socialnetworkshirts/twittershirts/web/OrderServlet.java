package com.socialnetworkshirts.twittershirts.web;

import com.socialnetworkshirts.twittershirts.Constants;
import com.socialnetworkshirts.twittershirts.data.DataService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mbs
 */
public class OrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String twitterUserId = request.getParameter(Constants.PARAM_TWITTER_USER_ID);
            String productTypeId = request.getParameter(Constants.PARAM_PRODUCT_TYPE_ID);
            String productTypeAppearanceId = request.getParameter(Constants.PARAM_PRODUCT_TYPE_APPEARANCE_ID);
            String productTypeSizeId = request.getParameter(Constants.PARAM_PRODUCT_TYPE_SIZE_ID);
            String quantity = request.getParameter(Constants.PARAM_QUANTITY);
            String useTwitterUserImages = request.getParameter(Constants.PARAM_USE_TWITTER_USER_IMAGES);
            if (useTwitterUserImages == null)
                useTwitterUserImages = Constants.USE_TWITTER_IMAGE;

            response.sendRedirect(DataService.getInstance().createProductAndCheckout(twitterUserId,
                    productTypeId, productTypeAppearanceId, productTypeSizeId, quantity,
                    Boolean.valueOf(useTwitterUserImages)));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
