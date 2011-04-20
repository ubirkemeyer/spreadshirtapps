package com.socialnetworkshirts.twittershirts.web;

import com.socialnetworkshirts.twittershirts.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mbs
 */
public class BaseServlet extends HttpServlet {
    protected void setContentType(HttpServletResponse response) {
        response.setContentType(Constants.MEDIA_TYPE_IMAGE_PNG);
    }

    protected void setCacheHeader(HttpServletResponse response, long time) {
        response.setDateHeader(Constants.HEADER_DATE, System.currentTimeMillis());
        response.setDateHeader(Constants.HEADER_EXPIRES, System.currentTimeMillis() + (1000 * time));
        response.setHeader(Constants.HEADER_CACHE_CONTROL, Constants.HEADER_VALUE_MAX_AGE + (1000 * time));
    }
}
