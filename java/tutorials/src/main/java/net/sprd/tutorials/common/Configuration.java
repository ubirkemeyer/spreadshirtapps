package net.sprd.tutorials.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author mbs
 */
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static final Configuration INSTANCE = new Configuration();

    private static Properties properties = null;

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public String getAPIKey() {
        return getProperties().getProperty("net.sprd.sampleapps.apiKey");
    }

    public String getSecret() {
        return getProperties().getProperty("net.sprd.sampleapps.secret");
    }

    public String getShopUrl() {
        return getProperties().getProperty("net.sprd.sampleapps.apiUrl") +
                getProperties().getProperty("net.sprd.sampleapps.apiUrlShop");
    }    

    private Properties getProperties() {
        if (properties == null) {
            try {
                Properties properties = new Properties();
                properties.load(this.getClass().getResourceAsStream("/spreadshirt.properties"));
                this.properties = properties;
            } catch (IOException e) {
                log.error("Could not load properties!", e);
            }
        }
        return properties;
    }
}
