package com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer;

import com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions.RetrievalException;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public class TwitterMockDataImporter implements TwitterDataImporter {
    public List<User> fetchFollowers(String twitterName, long followersCount)
            throws RetrievalException {
        try {
            InputSource source = new InputSource(this.getClass().getResourceAsStream("/followersSampleData.xml"));
            TwitterUserXMLProcessingHandler handler = new TwitterUserXMLProcessingHandler();
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            parser.parse(source);
            return handler.getUsers();
        } catch (Exception e) {
            throw new RetrievalException("Could not fetch followers data for " +
                    twitterName + "!", e);
        }
    }

    public User fetchUser(String twitterName)
            throws RetrievalException {
        try {
            InputSource source = new InputSource(this.getClass().getResourceAsStream("/userSampleData.xml"));
            TwitterUserXMLProcessingHandler handler = new TwitterUserXMLProcessingHandler();
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            parser.parse(source);
            return handler.getUsers().get(0);
        } catch (Exception e) {
            throw new RetrievalException("Could not fetch user data for " +
                    twitterName + "!", e);
        }
    }
}
