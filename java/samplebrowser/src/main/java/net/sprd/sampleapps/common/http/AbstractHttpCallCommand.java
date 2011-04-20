package net.sprd.sampleapps.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public abstract class AbstractHttpCallCommand implements HttpCallCommand {
    private static final Logger log = LoggerFactory.getLogger(AbstractHttpCallCommand.class);

    protected HttpUrlConnectionFactory connectionFactory = null;
    protected String url;
    protected HttpMethod method;
    protected HttpMethod tunneledMethod;
    protected boolean apiKeyProtected;
    protected boolean sessionIdProtected;
    protected boolean headerAllowed;
    protected String acceptType = null;
    protected Object input;
    protected Object output;
    private String errorMessage;
    private int status;
    private String location;
    private String contentType;

    public AbstractHttpCallCommand(HttpUrlConnectionFactory connectionFactory, String url,
                                   HttpMethod method, HttpMethod tunneledMethod,
                                   boolean apiKeyProtected, boolean sessionIdProtected,
                                   boolean headerAllowed) {
        this.sessionIdProtected = sessionIdProtected;
        this.connectionFactory = connectionFactory;
        this.apiKeyProtected = apiKeyProtected;
        this.url = url;
        this.headerAllowed = headerAllowed;
        this.method = method;
        this.tunneledMethod = tunneledMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getTunneledMethod() {
        return tunneledMethod;
    }

    public void setTunneledMethod(HttpMethod tunneledMethod) {
        this.tunneledMethod = tunneledMethod;
    }

    public boolean isApiKeyProtected() {
        return apiKeyProtected;
    }

    public void setApiKeyProtected(boolean apiKeyProtected) {
        this.apiKeyProtected = apiKeyProtected;
    }

    public boolean isSessionIdProtected() {
        return sessionIdProtected;
    }

    public void setSessionIdProtected(boolean sessionIdProtected) {
        this.sessionIdProtected = sessionIdProtected;
    }

    public boolean isHeaderAllowed() {
        return headerAllowed;
    }

    public void setHeaderAllowed(boolean headerAllowed) {
        this.headerAllowed = headerAllowed;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Object getOutput() {
        return output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public int getStatus() {
        return status;
    }

    public boolean didErrorOccur() {
        return (status == -1 || status >= 400);
    }

    @Override
    public void execute() {
        errorMessage = "";
        status = -1;
        output = null;
        location = null;
        contentType = null;

        HttpURLConnection connection = null;
        try {
            connection = connectionFactory.createHttpConnection(url, method,
                    tunneledMethod, apiKeyProtected, sessionIdProtected, headerAllowed);
            if (input != null &&
                    (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method)))
                writeInput(connection);
            if (acceptType != null)
                connection.setRequestProperty("Accept", acceptType);
            connection.connect();
            status = connection.getResponseCode();
            contentType = connection.getHeaderField("Content-Type");
            if (status >= 400) {
                errorMessage = getResponseMessage(connection);
            }
            if (status == 201) {
                location = connection.getHeaderField("Location");
            }
            if (status == 200) {
                if (HttpMethod.GET.equals(method) ||
                        (HttpMethod.POST.equals(method) &&
                                !(HttpMethod.PUT.equals(tunneledMethod) ||
                                        HttpMethod.DELETE.equals(tunneledMethod))) ||
                        HttpMethod.OPTIONS.equals(method))
                    readOutput(connection);
            }
        } catch (Exception e) {
            log.error("Connection could not be established!", e);
            try {
                errorMessage = getResponseMessage(connection);
            } catch (IOException e1) {
                log.error("Error message could not be read!", e);
                errorMessage = "Error message could not be read!";
            }
        }
        finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    protected abstract void readOutput(HttpURLConnection connection)
            throws Exception;

    protected abstract void writeInput(HttpURLConnection connection)
            throws Exception;

    protected String getResponseMessage(HttpURLConnection connection)
            throws IOException {
        InputStream in = (connection.getResponseCode() < 400)
                ? connection.getInputStream()
                : connection.getErrorStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int redBytes = 0;
        while ((redBytes = in.read(bytes)) != -1) {
            out.write(bytes, 0, redBytes);
        }
        in.close();
        out.close();
        return out.toString("UTF-8");
    }
}
