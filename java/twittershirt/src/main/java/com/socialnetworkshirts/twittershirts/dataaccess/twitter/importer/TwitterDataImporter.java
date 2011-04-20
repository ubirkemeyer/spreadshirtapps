package com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer;

import com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions.RetrievalException;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;

import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public interface TwitterDataImporter {
    List<User> fetchFollowers(String twitterName, long followersCount)
            throws RetrievalException;

    User fetchUser(String twitterName)
            throws RetrievalException;
}
