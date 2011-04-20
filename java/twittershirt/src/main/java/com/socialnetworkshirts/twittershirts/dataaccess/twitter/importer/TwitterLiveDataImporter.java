package com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer;

import com.socialnetworkshirts.twittershirts.Configuration;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions.RetrievalException;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mbs
 * @version $version$
 */
public class TwitterLiveDataImporter implements TwitterDataImporter {
    private static final Logger log = LoggerFactory.getLogger(TwitterLiveDataImporter.class);

    private static final String USER_NAME = Configuration.getInstance().getTwitterUserId();
    private static final String PASSWORD = Configuration.getInstance().getTwitterPassword();
    private static final String DOMAIN = "http://twitter.com";
    private static final String REQUEST_URL_FOLLOWERS = DOMAIN + "/statuses/followers/{0}.xml?cursor={1}";
    private static final String REQUEST_URL_USER = DOMAIN + "/users/show/{0}.xml";

    static {
        log.info("Twitter account data is: " + USER_NAME + " " + PASSWORD);
    }

    public List<User> fetchFollowers(String twitterName, long followersCount)
            throws RetrievalException {
        List<User> followers = new ArrayList<User>();
        FollowersResult result;
        String nextCursor = "-1";
        for (int i = 0; i < 3; i++) {
            result = fetchFollowersInternal(twitterName, nextCursor);
            followers.addAll(result.users);            

            if (result.nextCursor == null)
                break;
            nextCursor = result.nextCursor;
        }
        return followers;
    }

    public FollowersResult fetchFollowersInternal(String twitterName, String cursor)
            throws RetrievalException {
        int statusCode = -1;
        try {
            HttpClient client = getHttpClient();
            log.info("Loading data from: " + MessageFormat.format(REQUEST_URL_FOLLOWERS, twitterName, cursor));
            GetMethod method = new GetMethod(MessageFormat.format(REQUEST_URL_FOLLOWERS, twitterName, cursor));
            method.setDoAuthentication(true);
            statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                InputSource source = new InputSource(method.getResponseBodyAsStream());
                TwitterUserXMLProcessingHandler handler = new TwitterUserXMLProcessingHandler();
                XMLReader parser = XMLReaderFactory.createXMLReader();
                parser.setContentHandler(handler);
                parser.parse(source);
                return new FollowersResult(handler.getUsers(), handler.getNextCursor());
            } else
                throw new RetrievalException("Could not fetch followers data for " +
                        twitterName + "! Status code was " + statusCode + "!");

        } catch (Exception e) {
            throw new RetrievalException("Could not fetch followers data for " +
                    twitterName + "!", e);
        }
    }

    public User fetchUser(String twitterName)
            throws RetrievalException {
        int statusCode = -1;
        try {
            HttpClient client = getHttpClient();
            log.info("Loading data from: " + MessageFormat.format(REQUEST_URL_USER, twitterName));
            GetMethod method = new GetMethod(MessageFormat.format(REQUEST_URL_USER, twitterName));
            method.setDoAuthentication(true);
            statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                InputSource source = new InputSource(method.getResponseBodyAsStream());
                TwitterUserXMLProcessingHandler handler = new TwitterUserXMLProcessingHandler();
                XMLReader parser = XMLReaderFactory.createXMLReader();
                parser.setContentHandler(handler);
                parser.parse(source);
                if (handler.getUsers().size() == 0)
                    throw new RetrievalException("Could not retrieve data for user " + twitterName + "!");
                return handler.getUsers().get(0);
            } else
                throw new RetrievalException("Could not fetch user data for " +
                        twitterName + "! Status code was " + statusCode + "!");

        } catch (Exception e) {
            throw new RetrievalException("Could not fetch user data for " +
                    twitterName + "!", e);
        }
    }

    private HttpClient getHttpClient() {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(USER_NAME, PASSWORD));
        return client;
    }

    class FollowersResult {
        List<User> users;
        String nextCursor;

        FollowersResult(List<User> users, String nextCursor) {
            this.users = users;
            this.nextCursor = nextCursor;
        }
    }
}
