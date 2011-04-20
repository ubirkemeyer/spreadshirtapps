package com.socialnetworkshirts.twittershirts.data.model;

import java.util.List;

/**
 * @author mbs
 */
public class TwitterUser {
    private String id;
    private String name;
    private String screenName;
    private long followersCount;
    private List<TwitterUser> followers;
    private String profileImageUrl;

    public TwitterUser(String id, String name, String screenName, long followersCount,
                       List<TwitterUser> followers, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.followersCount = followersCount;
        this.followers = followers;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public List<TwitterUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<TwitterUser> followers) {
        this.followers = followers;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
