package net.sprd.tutorials.productcreation;

import net.sprd.tutorials.common.http.HttpCallCommand;
import net.sprd.tutorials.common.http.HttpCallCommandFactory;
import net.sprd.tutorials.common.http.HttpMethod;
import net.sprd.tutorials.common.http.HttpUrlConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author mbs
 */
public class ProductCreation {
    private static final Logger log = LoggerFactory.getLogger(ProductCreation.class);

    public static final String API_KEY = "62b80c59-0777-426f-b47c-a7bb872cdf09";
    public static final String SECRET = "a373fe3a-68b2-4f3b-93ae-a83090e20ce8";

    public static final String UPLOAD_URL = "http://api.spreadshirt.net/api/v1/shops/40000/designs";
    public static final String UPLOAD_XML = "./src/main/resources/productcreation/design.xml";
    public static final String UPLOAD_IMAGE = "./src/main/resources/productcreation/design.png";
    public static final String CREATION_URL = "http://api.spreadshirt.net/api/v1/shops/40000/products";
    public static final String PRODUCT_XML = "./src/main/resources/productcreation/product.xml";

    public static void main(String[] args)
            throws Exception {
        HttpUrlConnectionFactory urlConnectionFactory =
                new HttpUrlConnectionFactory(API_KEY, SECRET, null);
        HttpCallCommandFactory commandFactory =
                new HttpCallCommandFactory(urlConnectionFactory);

        // create design data using xml
        HttpCallCommand createDesignCommand =
                commandFactory.createPlainHttpCallCommand(UPLOAD_URL, HttpMethod.POST, null);
        createDesignCommand.setInput(getXMLData(UPLOAD_XML));
        createDesignCommand.setApiKeyProtected(true);
        createDesignCommand.execute();
        if (createDesignCommand.getStatus() >= 400) {
            throw new Exception("Could not create design xml!");
        }
        log.info("XML location is: " + createDesignCommand.getUrl());

        // get created design xml
        HttpCallCommand getDesignCommand =
                commandFactory.createPlainHttpCallCommand(createDesignCommand.getLocation(), HttpMethod.GET, null);
        getDesignCommand.execute();
        if (createDesignCommand.getStatus() >= 400) {
            throw new Exception("Could not retrieve design xml from " + createDesignCommand.getLocation() + "!");
        }
        String message = (String) getDesignCommand.getOutput();

        // determine upload location
        String searchString = "resource xlink:href=\"";
        int index = message.indexOf(searchString);
        String uploadUrl = message.substring(index + searchString.length(),
                message.indexOf("\"", index + searchString.length() + 1));
        log.info("Upload location is: " + uploadUrl);

        // upload image
        HttpCallCommand uploadDesignCommand =
                commandFactory.createFileHttpCallCommand(uploadUrl, HttpMethod.PUT, null, new File(UPLOAD_IMAGE), null);
        uploadDesignCommand.setApiKeyProtected(true);
        uploadDesignCommand.execute();
        if (uploadDesignCommand.getStatus() >= 400) {
            log.error(uploadDesignCommand.getErrorMessage());
            throw new Exception("Status above 400 expected but status was " + uploadDesignCommand.getStatus() + "!");
        }

        // create product data using xml
        HttpCallCommand createProductCommand =
                commandFactory.createPlainHttpCallCommand(CREATION_URL, HttpMethod.POST, null);
        String productData = getXMLData(PRODUCT_XML);
        // use id from fetched design xml here -> my solution is only a hack
        productData = productData.replace("THE_DESIGN_ID", "u" + uploadUrl.substring(uploadUrl.lastIndexOf('/') + 1));
        createProductCommand.setInput(productData);
        createProductCommand.setApiKeyProtected(true);
        createProductCommand.execute();
        if (createProductCommand.getStatus() >= 400) {
            throw new Exception("Could not create product xml!");
        }
        log.info("XML location is: " + createProductCommand.getLocation());
    }

    private static String getXMLData(String fileName)
            throws IOException {
        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            os = new ByteArrayOutputStream();
            is = new FileInputStream(new File(fileName));
            int length = 0;
            byte[] data = new byte[4096];
            while ((length = is.read(data)) != -1) {
                os.write(data, 0, length);
            }
            return os.toString();
        } finally {
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        }
    }
}
