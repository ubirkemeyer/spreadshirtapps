package net.sprd.tutorials.common.http;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author mbs
 */
public class FileBasedHttpCallCommand extends AbstractHttpCallCommand {
    public FileBasedHttpCallCommand(HttpUrlConnectionFactory connectionFactory,
                                    String url, HttpMethod method, HttpMethod tunneledMethod,
                                    boolean apiKeyProtected, boolean sessionIdProtected,
                                    boolean headerAllowed, File inputFile, File outputFile) {
        super(connectionFactory, url, method, tunneledMethod, apiKeyProtected, sessionIdProtected, headerAllowed);
        this.input = inputFile;
        this.output = outputFile;
    }

    protected void readOutput(HttpURLConnection connection)
            throws Exception {
        if (output != null) {
            InputStream is;
            OutputStream os = null;
            try {
                is = connection.getInputStream();
                os = new FileOutputStream((File)output);
                int length = 0;
                byte[] data = new byte[4096];
                while ((length = is.read(data)) != -1) {
                    os.write(data, 0, length);
                }
            } finally {
                if (os != null)
                    os.close();
            }
        }
    }

    protected void writeInput(HttpURLConnection connection)
            throws Exception {
        if (input != null) {
            OutputStream os;
            InputStream is = null;
            try {
                os = connection.getOutputStream();
                is = new FileInputStream((File)input);
                int length = 0;
                byte[] data = new byte[4096];
                while ((length = is.read(data)) != -1) {
                    os.write(data, 0, length);
                }
            } finally {
                if (is != null)
                    is.close();
            }
        }
    }

    public JAXBElement getJaxbOutput() {
        return (output == null) ? null : (JAXBElement) output;
    }
}
