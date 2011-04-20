package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http;

import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public class PlainHttpCallCommand extends AbstractHttpCallCommand {
    public PlainHttpCallCommand(HttpUrlConnectionFactory connectionFactory, String url,
                                HttpMethod method, HttpMethod tunneledMethod,
                                boolean apiKeyProtected,
                                boolean sessionIdProtected, boolean headerAllowed) {
        super(connectionFactory, url, method, tunneledMethod, apiKeyProtected, sessionIdProtected, headerAllowed);
    }

    @Override
    protected void readOutput(HttpURLConnection connection)
            throws Exception {
        output = getResponseMessage(connection);
    }

    @Override
    protected void writeInput(HttpURLConnection connection)
            throws Exception {
        connection.getOutputStream().write(input.toString().getBytes());
    }
}