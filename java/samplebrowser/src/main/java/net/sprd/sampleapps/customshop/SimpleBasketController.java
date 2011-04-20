package net.sprd.sampleapps.customshop;

import net.sprd.sampleapps.common.dataaccess.DataService;
import net.sprd.sampleapps.common.dataaccess.model.Basket;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author mbs
 */
public class SimpleBasketController extends HttpServlet {
    public static final String PARAM_ARTICLE_ID = "articleId";
    public static final String PARAM_APPEARANCE_ID = "appearanceId";
    public static final String PARAM_SIZE_ID = "sizeId";
    public static final String PARAM_QUANTITY = "quantity";
    public static final String COOKIE_SPRD_BASKET = "sprd_basket";

    private DataService dataAccess = DataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        String basketId = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(BasketController.COOKIE_SPRD_BASKET))
                basketId = cookie.getValue();
        }

        if (basketId == null)
            httpServletRequest.setAttribute("basket", new Basket("0", "0", "0", "0", "-"));
        else
            httpServletRequest.setAttribute("basket", dataAccess.getBasket(basketId, false));
        httpServletRequest.getRequestDispatcher("/simplebasket.jsp").forward(httpServletRequest, httpServletResponse);
    }
}