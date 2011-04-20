package com.socialnetworkshirts.twittershirts;

/**
 * @author mbs
 */
public interface Constants {
    String PARAM_PRODUCT_TYPE = "productType";
    String PARAM_TWITTER_USER_ID = "twitterUserId";
    String PARAM_PRODUCT_TYPE_ID = "productTypeId";
    String PARAM_PRODUCT_TYPE_APPEARANCE_ID = "productTypeAppearanceId";
    String PARAM_PRODUCT_TYPE_SIZE_ID = "productTypeSizeId";
    String PARAM_QUANTITY = "quantity";
    String PARAM_RENDER_PRODUCT_IMAGE = "renderProductImage";
    String PARAM_USE_TWITTER_USER_IMAGES = "useTwitterUserImages";
    String USE_TWITTER_IMAGE = "true";

    String MEDIA_TYPE_IMAGE_PNG = "image/png";
    String HEADER_DATE = "Date";
    String HEADER_EXPIRES = "Expires";
    String HEADER_CACHE_CONTROL = "Cache-Control";
    String HEADER_VALUE_MAX_AGE = "max-age=";
    int DEFAULT_CACHE_TIME = 60 * 60 * 24;

    long TARGET_VIEW_WIDTH = 600;
    long TARGET_VIEW_HEIGHT = 600;
    int TARGET_OFFSET_X = 10;
    int TARGET_OFFSET_Y = 60;
    int TWITTER_IMAGE_SIZE = 48;
}
