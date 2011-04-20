package com.socialnetworkshirts.twittershirts.dataaccess.twittershirts;

import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model.ValidPrintType;
import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model.ValidProductType;
import com.socialnetworkshirts.twittershirts.dataaccess.twittershirts.model.ValidProductTypeAppearance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mbs
 */
public class TwittershirtsDataService {
    private List<ValidProductType> validProductTypes;
    private List<ValidPrintType> validPrintTypes;

    private static TwittershirtsDataService instance = new TwittershirtsDataService();

    public static TwittershirtsDataService getInstance() {
        return instance;
    }

    private TwittershirtsDataService() {
        validProductTypes = new ArrayList<ValidProductType>();
        ValidProductType validProductType = new ValidProductType();
        validProductType.setId("6");
        ValidProductTypeAppearance validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("1");
        validAppearance.setColor1("black");
        validProductType.getProductTypeAppearances().put("1", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("7");
        validAppearance.setColor1("black");
        validProductType.getProductTypeAppearances().put("7", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("5");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("5", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("2");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("2", validAppearance);
        validProductTypes.add(validProductType);

        validProductType = new ValidProductType();
        validProductType.setId("23");
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("1");
        validAppearance.setColor1("black");
        validProductType.getProductTypeAppearances().put("1", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("5");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("5", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("2");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("2", validAppearance);
        validProductTypes.add(validProductType);

        validProductType = new ValidProductType();
        validProductType.setId("20");
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("1");
        validAppearance.setColor1("black");
        validProductType.getProductTypeAppearances().put("1", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("5");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("5", validAppearance);
        validAppearance = new ValidProductTypeAppearance();
        validAppearance.setId("2");
        validAppearance.setColor1("white");
        validProductType.getProductTypeAppearances().put("2", validAppearance);
        validProductTypes.add(validProductType);

        validPrintTypes = new ArrayList<ValidPrintType>();
        validPrintTypes.add(new ValidPrintType("17"));
        validPrintTypes.add(new ValidPrintType("19"));
    }

    public List<ValidProductType> getValidProductTypes() {
        return validProductTypes;
    }

    public List<ValidPrintType> getValidPrintTypes() {
        return validPrintTypes;
    }

    public ValidProductType getValidProductType(String productTypeId) {
        for (ValidProductType validProductType : validProductTypes) {
            if (validProductType.getId().equals(productTypeId)) {
                return validProductType;
            }
        }
        return null;
    }

    public ValidProductTypeAppearance getValidProductTypeAppearance(String productTypeId, String productTypeAppearanceId) {
        for (ValidProductType validProductType : validProductTypes) {
            if (validProductType.getId().equals(productTypeId)) {
                for (String validAppearanceId : validProductType.getProductTypeAppearances().keySet()) {
                    if (validAppearanceId.equals(productTypeAppearanceId)) {
                        return validProductType.getProductTypeAppearances().get(validAppearanceId);
                    }
                }
            }
        }
        return null;
    }

}
