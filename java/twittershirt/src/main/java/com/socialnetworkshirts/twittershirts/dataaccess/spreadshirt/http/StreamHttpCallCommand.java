package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public class StreamHttpCallCommand extends AbstractHttpCallCommand {
    private static final Logger log = LoggerFactory.getLogger(StreamHttpCallCommand.class);

    public StreamHttpCallCommand(HttpUrlConnectionFactory connectionFactory, String url,
                                 HttpMethod method, HttpMethod tunneledMethod, boolean apiKeyProtected,
                                 boolean sessionIdProtected, boolean headerAllowed) {
        super(connectionFactory, url, method, tunneledMethod, apiKeyProtected, sessionIdProtected, headerAllowed);
    }

    @Override
    protected void readOutput(HttpURLConnection connection)
            throws Exception {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            is = connection.getInputStream();
            byte[] buffer = new byte[2048];
            int size = 0;
            while ((size = is.read(buffer)) != -1) {
                bos.write(buffer, 0, size - 1);
            }
            output = new ByteArrayInputStream(bos.toByteArray());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("Could not close http connection input stream!", e);
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("Could not close temporary stream for copying data.", e);
            }
        }
    }

    @Override
    protected void writeInput(HttpURLConnection connection)
            throws Exception {
        BufferedOutputStream bos = null;
        InputStream is = null;
        try {
            bos = new BufferedOutputStream(connection.getOutputStream());
            is = (InputStream) input;
            byte[] buffer = new byte[2048];
            int size = 0;
            while ((size = is.read(buffer)) != -1) {
                bos.write(buffer, 0, size);
            }
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                log.error("Could not close http connection output stream.", e);
            }
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                log.error("Could not close temporary stream for copying data.", e);
            }
        }
    }
}
