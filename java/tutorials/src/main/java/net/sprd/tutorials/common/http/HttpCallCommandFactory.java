package net.sprd.tutorials.common.http;

import javax.xml.bind.JAXBContext;
import java.io.File;

/**
 * @author mbs
 */
public class HttpCallCommandFactory {
    private HttpUrlConnectionFactory connectionFactory = null;

    public HttpCallCommandFactory(HttpUrlConnectionFactory connectionFactory)
            throws Exception {
       this.connectionFactory = connectionFactory;
    }

    public HttpUrlConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(HttpUrlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public HttpCallCommand createPlainHttpCallCommand(String url, HttpMethod method,
                                                      HttpMethod tunneledMethod) {
        return new PlainHttpCallCommand(connectionFactory, url, method,
                tunneledMethod, false, false, false);
    }

    public HttpCallCommand createFileHttpCallCommand(String url, HttpMethod method,
                                                     HttpMethod tunneledMethod,
                                                     File inputFile, File outputFile) {
        return new FileBasedHttpCallCommand(connectionFactory, url, method,
                tunneledMethod, false, false, false, inputFile, outputFile);
    }
}
