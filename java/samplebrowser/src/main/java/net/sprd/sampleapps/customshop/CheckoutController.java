package net.sprd.sampleapps.customshop;

import net.sprd.sampleapps.common.dataaccess.DataService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mbs
 */
public class CheckoutController extends HttpServlet {
    private DataService dataAccess = DataService.getInstance();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        String basketId = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(BasketController.COOKIE_SPRD_BASKET))
                basketId = cookie.getValue();
        }

        String sizeId = httpServletRequest.getParameter(BasketController.PARAM_SIZE_ID);

        if (sizeId != null && !sizeId.trim().equals("")) {
            basketId = dataAccess.checkout(httpServletRequest.getParameter(BasketController.PARAM_ARTICLE_ID),
                    httpServletRequest.getParameter(BasketController.PARAM_APPEARANCE_ID),
                    sizeId,
                    httpServletRequest.getParameter(BasketController.PARAM_QUANTITY),
                    basketId);
            httpServletResponse.addCookie(new Cookie(BasketController.COOKIE_SPRD_BASKET, basketId));
            httpServletResponse.sendRedirect(dataAccess.getCheckoutUrl(basketId));
        } else {
            if (basketId == null) {
                httpServletResponse.sendRedirect("basket");
            } else {
                httpServletResponse.sendRedirect(dataAccess.getCheckoutUrl(basketId));
            }
        }
    }
}
