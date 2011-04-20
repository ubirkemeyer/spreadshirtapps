package net.sprd.sampleapps.common.http;

import javax.xml.bind.JAXBContext;

/**
 * @author mbs
 */
public class HttpCallCommandFactory {
    private HttpUrlConnectionFactory connectionFactory = null;
    private JAXBContext jaxbContext = null;

    public HttpCallCommandFactory(HttpUrlConnectionFactory connectionFactory)
            throws Exception {
        this(connectionFactory, "net.spreadshirt.api");

    }

    public HttpCallCommandFactory(HttpUrlConnectionFactory connectionFactory, String contextPath)
            throws Exception {
        this.connectionFactory = connectionFactory;
        this.jaxbContext = JAXBContext.newInstance(contextPath);
    }

    public HttpUrlConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(HttpUrlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public JAXBContext getJaxbContext() {
        return jaxbContext;
    }

    public void setJaxbContext(JAXBContext jaxbContext) {
        this.jaxbContext = jaxbContext;
    }

    public HttpCallCommand createPlainHttpCallCommand(String url, HttpMethod method,
                                                      HttpMethod tunneledMethod) {
        return new PlainHttpCallCommand(connectionFactory, url, method,
                tunneledMethod, false, false, false);
    }

    public HttpCallCommand createJaxbHttpCallCommand(String url, HttpMethod method,
                                                     HttpMethod tunneledMethod) {
        return new JAXBHttpCallCommand(connectionFactory, jaxbContext, url, method,
                tunneledMethod, false, false, false);
    }
}
