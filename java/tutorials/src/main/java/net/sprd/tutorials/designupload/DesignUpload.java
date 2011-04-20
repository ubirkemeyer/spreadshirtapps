package net.sprd.tutorials.designupload;

import net.sprd.tutorials.common.http.HttpCallCommand;
import net.sprd.tutorials.common.http.HttpCallCommandFactory;
import net.sprd.tutorials.common.http.HttpMethod;
import net.sprd.tutorials.common.http.HttpUrlConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author mbs
 */
public class DesignUpload {
    private static final Logger log = LoggerFactory.getLogger(DesignUpload.class);

    public static final String API_KEY = "...";
    public static final String SECRET = "...";

    public static final String UPLOAD_URL = "http://api.spreadshirt.net/api/v1/shops/40000/designs";
    public static final String UPLOAD_XML = "./src/main/resources/designupload/design.xml";
    public static final String UPLOAD_IMAGE = "./src/main/resources/designupload/design.png";

    public static void main(String[] args)
            throws Exception {
        HttpUrlConnectionFactory urlConnectionFactory =
                new HttpUrlConnectionFactory(API_KEY, SECRET, null);
        HttpCallCommandFactory commandFactory =
                new HttpCallCommandFactory(urlConnectionFactory);

        // create design data using xml
        HttpCallCommand createCommand =
                commandFactory.createFileHttpCallCommand(UPLOAD_URL, HttpMethod.POST, null, new File(UPLOAD_XML), null);
        createCommand.setApiKeyProtected(true);
        createCommand.execute();
        if (createCommand.getStatus() >= 400) {
            throw new Exception("Could not create design xml!");
        }
        log.info("XML location is: " + createCommand.getUrl());

        // get created design xml
        HttpCallCommand getCommand =
                commandFactory.createPlainHttpCallCommand(createCommand.getLocation(), HttpMethod.GET, null);
        getCommand.execute();
        if (createCommand.getStatus() >= 400) {
            throw new Exception("Could not retrieve design xml from " + createCommand.getLocation() + "!");
        }
        String message = (String) getCommand.getOutput();

        // determine upload location
        String searchString = "resource xlink:href=\"";
        int index = message.indexOf(searchString);
        String uploadUrl = message.substring(index + searchString.length(),
                message.indexOf("\"", index + searchString.length() + 1));
        log.info("Upload location is: " + uploadUrl);

        // upload image
        HttpCallCommand uploadCommand =
                commandFactory.createFileHttpCallCommand(uploadUrl, HttpMethod.PUT, null, new File(UPLOAD_IMAGE), null);
        uploadCommand.setApiKeyProtected(true);
        uploadCommand.execute();
        if (uploadCommand.getStatus() >= 400) {
            log.error(uploadCommand.getErrorMessage());
            throw new Exception("Status above 400 expected but status was " + uploadCommand.getStatus() + "!");
        }
    }
}

