package com.socialnetworkshirts.twittershirts.dataaccess.twitter.importer;

import com.socialnetworkshirts.twittershirts.dataaccess.twitter.model.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mbs
 * @version $version$
 */
class TwitterUserXMLProcessingHandler extends DefaultHandler {
    private String TAG_USER = "user";
    private String TAG_ID = "id";
    private String TAG_NAME = "name";
    private String TAG_SCREEN_NAME = "screen_name";
    private String TAG_PROFILE_IMAGE_URL = "profile_image_url";
    private String TAG_FOLLOWERS_COUNT = "followers_count";
    private String TAG_NEXT_CURSOR = "next_cursor";
    private List RELEVANT_TAGS = Arrays.asList(TAG_USER, TAG_ID, TAG_NAME, TAG_SCREEN_NAME,
            TAG_PROFILE_IMAGE_URL, TAG_FOLLOWERS_COUNT, TAG_NEXT_CURSOR);

    private List<User> users = new ArrayList<User>();
    private String nextCursor;
    private User currentUser;
    private StringBuffer buffer = new StringBuffer();
    private boolean relevant = false;

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (TAG_USER.equals(localName))
            currentUser = new User();
        else if (RELEVANT_TAGS.contains(localName)) {
            buffer.delete(0, buffer.length());
            relevant = true;
        } else
            relevant = false;
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (relevant)
            buffer.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (TAG_USER.equals(localName))
            users.add(currentUser);
        else if (TAG_ID.equals(localName))
            currentUser.setId(buffer.toString());
        else if (TAG_NAME.equals(localName))
            currentUser.setName(buffer.toString());
        else if (TAG_PROFILE_IMAGE_URL.equals(localName))
            currentUser.setProfileImageUrl(buffer.toString());
        else if (TAG_SCREEN_NAME.equals(localName))
            currentUser.setScreenName(buffer.toString());
        else if (TAG_FOLLOWERS_COUNT.equals(localName))
            currentUser.setFollowersCount(Long.parseLong(buffer.toString()));
        else if (TAG_NEXT_CURSOR.equals(localName))
            nextCursor = buffer.toString();
    }

    List<User> getUsers() {
        return users;
    }

    public String getNextCursor() {
        return nextCursor;
    }
}
