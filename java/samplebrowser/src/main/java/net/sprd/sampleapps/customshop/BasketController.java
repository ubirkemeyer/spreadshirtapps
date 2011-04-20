package net.sprd.sampleapps.customshop;

import net.sprd.sampleapps.common.dataaccess.DataService;
import net.sprd.sampleapps.common.dataaccess.model.Article;
import net.sprd.sampleapps.common.dataaccess.model.Basket;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mbs
 */
public class BasketController extends HttpServlet {
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
            httpServletRequest.setAttribute("basket", dataAccess.getBasket(basketId, true));                    
        httpServletRequest.getRequestDispatcher("/basket.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        String basketId = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(BasketController.COOKIE_SPRD_BASKET))
                basketId = cookie.getValue();
        }

        if (basketId == null)
            httpServletRequest.setAttribute("basket", new Basket("0", "0", "0", "0", "-"));
        else {
            System.out.println("action: " + httpServletRequest.getParameter("action") + " id " +  httpServletRequest.getParameter("itemId"));
            if (httpServletRequest.getParameter("action") != null &&
                    httpServletRequest.getParameter("action").equalsIgnoreCase("delete") &&
                    httpServletRequest.getParameter("itemId") != null) {
                dataAccess.removeBasketItem(basketId, httpServletRequest.getParameter("itemId"));
            }

            httpServletRequest.setAttribute("basket", dataAccess.getBasket(basketId, true));
        }
        httpServletRequest.getRequestDispatcher("/basket.jsp").forward(httpServletRequest, httpServletResponse);
    }
}