package com.socialnetworkshirts.twittershirts.dataaccess.twitter.model;

import java.io.Serializable;

/**
 * Represents the data of a twitter user required for us.
 *
 * @author mbs
 * @version $version$
 */
public class User implements Serializable {
    private String id;
    /**
     * User name of twitter user.
     */
    private String name;

    private String screenName;
    /**
     * Image url of twitter user;
     */
    private String profileImageUrl;

    private long followersCount;

    public User() {
    }

    public User(String id, String name, String screenName, String profileImageUrl,
                long followersCount) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.followersCount = followersCount;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (followersCount != that.followersCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (profileImageUrl != null ? !profileImageUrl.equals(that.profileImageUrl) : that.profileImageUrl != null)
            return false;
        if (screenName != null ? !screenName.equals(that.screenName) : that.screenName != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        result = 31 * result + (profileImageUrl != null ? profileImageUrl.hashCode() : 0);
        result = 31 * result + (int) (followersCount ^ (followersCount >>> 32));
        return result;
    }


    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("TwitterUser{");
        sb.append("id='").append(id == null ? "null" : id).append("'");
        sb.append(", name='").append(name == null ? "null" : name).append("'");
        sb.append(", screenName='").append(screenName == null ? "null" : screenName).append("'");
        sb.append(", profileImageUrl='").append(profileImageUrl == null ? "null" : profileImageUrl).append("'");
        sb.append(", followersCount=").append(followersCount);
        sb.append('}');
        return sb.toString();
    }
}

