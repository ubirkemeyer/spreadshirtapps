package com.socialnetworkshirts.twittershirts.dataaccess.spreadshirt.http;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public class JAXBHttpCallCommand extends AbstractHttpCallCommand {
    private JAXBContext jaxbContext = null;

    public JAXBHttpCallCommand(HttpUrlConnectionFactory connectionFactory,
                               JAXBContext jaxbContext,
                               String url, HttpMethod method,
                               HttpMethod tunneledMethod,
                               boolean apiKeyProtected, boolean sessionIdProtected,
                               boolean headerAllowed) {
        super(connectionFactory, url, method, tunneledMethod,
                apiKeyProtected, sessionIdProtected, headerAllowed);
        this.jaxbContext = jaxbContext;
    }

    protected void readOutput(HttpURLConnection connection)
            throws Exception {
        output = jaxbContext.createUnmarshaller()
                .unmarshal(connection.getInputStream());
    }

    protected void writeInput(HttpURLConnection connection)
            throws Exception {
        jaxbContext.createMarshaller().marshal(input, System.out);
        jaxbContext.createMarshaller().marshal(input, connection.getOutputStream());
    }

    public JAXBElement getJaxbOutput() {
        return (output == null) ? null : (JAXBElement) output;
    }
}
