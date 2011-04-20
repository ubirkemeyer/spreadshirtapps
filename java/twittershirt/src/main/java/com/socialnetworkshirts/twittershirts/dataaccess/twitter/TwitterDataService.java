package com.socialnetworkshirts.twittershirts.dataaccess.twitter;

import com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions.RetrievalException;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer.TwitterDataImporter;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer.TwitterLiveDataImporter;
import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
public class TwitterDataService {
    private static final Logger log = LoggerFactory.getLogger(TwitterDataService.class);

    private static final String CACHE_FOLLOWERS = "followers";
    private static final String CACHE_USERS = "users";

    private TwitterDataImporter importer;
    private CacheManager cacheManager;
    private Cache followersCache;
    private Cache usersCache;

    private static TwitterDataService instance = new TwitterDataService();

    public static TwitterDataService getInstance() {
        return instance;
    }

    private TwitterDataService() {
        log.info("Initializing TwitterLiveDataImporter ...");
        importer = new TwitterLiveDataImporter();
        //importer = new TwitterLiveDataImporter();
        log.info("Initializing CacheManager with data from ehcache.xml ...");
        cacheManager = new CacheManager(this.getClass().getClassLoader().getResourceAsStream("/ehcache.xml"));
        followersCache = cacheManager.getCache(CACHE_FOLLOWERS);
        usersCache = cacheManager.getCache(CACHE_USERS);
    }

    public List<User> getFollowers(String twitterName)
            throws RetrievalException {
        Element elem = followersCache.get(twitterName);
        if (elem == null) {
            if (log.isDebugEnabled())
                log.debug("Loading followers for " + twitterName + " ...");
            User user = getUser(twitterName);
            List<User> followers = importer.fetchFollowers(twitterName, user.getFollowersCount());
            if (log.isDebugEnabled())
                log.debug("Caching followers data for " + twitterName + " ...");
            followersCache.put(new Element(twitterName, followers));
            return followers;
        } else
            return (List<User>) elem.getValue();
    }

    public User getUser(String twitterName)
            throws RetrievalException {
        Element elem = usersCache.get(twitterName);
        if (elem == null) {
            if (log.isDebugEnabled())
                log.debug("Loading user data for " + twitterName + " ...");
            User user = importer.fetchUser(twitterName);
            if (log.isDebugEnabled())
                log.debug("Caching user data for " + twitterName + " ...");
            usersCache.put(new Element(twitterName, user));
            return user;
        } else
            return (User) elem.getValue();
    }
}
