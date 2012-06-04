package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * User
 */
public class User implements Comparable {

	protected long userId;
    protected String userName;
    protected String userPhotoUrl;
    protected String userPhotoUrlSmall;
    protected boolean male;
    protected boolean online = false;
    protected boolean friend = false;
    protected boolean newFriend = false;
    
    /**
     * @deprecated bean shouldn't contain any service 
     */
    private VkontakteAPI api;

    //sometimes we don't get any avatar. then use it as default
    public static final String STUB_URL = "http://vkontakte.ru/images/question_b.gif";

    /**
     * Constructing user bean
     * @param userInfo
     * @throws JSONException
     */
    public User(JSONArray userInfo) throws JSONException {
        if (userInfo.isNull(0)) {
            return;
        }

        userId = userInfo.getLong(0);
        int length = userInfo.length();
        if (length >= 3) {
            userName = userInfo.getString(1);
            userPhotoUrl = userInfo.getString(2);
            if (userPhotoUrl.equals("0")) userPhotoUrl = null;
        }
        if (length == 4)
            online = userInfo.getInt(3) == 1;
        if (length == 6) {
            userPhotoUrlSmall = userPhotoUrl == null ? null : userPhotoUrl.substring(0, userPhotoUrl.lastIndexOf("/") + 1) + userInfo.getString(3) + ".jpg";
            male = userInfo.getInt(4) == 2;
            online = userInfo.getInt(5) == 1;
        }
    }
    
    /**
     * @deprecated
     */
    public User(JSONArray userInfo, VkontakteAPI api) throws JSONException {
        if (userInfo.isNull(0))
            return;

        this.api = api;
        userId = userInfo.getLong(0);
        int length = userInfo.length();
        if (length >= 3) {
            userName = userInfo.getString(1);
            userPhotoUrl = userInfo.getString(2);
            if (userPhotoUrl.equals("0")) userPhotoUrl = null;
        }
        if (length == 4)
            online = userInfo.getInt(3) == 1;
        if (length == 6) {
            userPhotoUrlSmall = userPhotoUrl == null ? null : userPhotoUrl.substring(0, userPhotoUrl.lastIndexOf("/") + 1) + userInfo.getString(3) + ".jpg";
            male = userInfo.getInt(4) == 2;
            online = userInfo.getInt(5) == 1;
        }
    }

    public User() {
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl == null ? STUB_URL : userPhotoUrl;
    }

    public String getUserPhotoUrlSmall() {
        return userPhotoUrlSmall;
    }

    public byte[] getUserPhoto() throws IOException {
        return api.getFileFromUrl(getUserPhotoUrl());
    }

    public byte[] getUserPhotoSmall() throws IOException {
        return api.getFileFromUrl(userPhotoUrlSmall);
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isNewFriend() {
        return newFriend;
    }

    public void setNewFriend(boolean newFriend) {
        this.newFriend = newFriend;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhotoUrl='" + userPhotoUrl + '\'' +
                ", userPhotoUrlSmall='" + userPhotoUrlSmall + '\'' +
                ", male=" + male +
                ", online=" + online +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || !(o instanceof User))
            return 1;

        User that = (User) o;

        if (this.userId < that.userId)
            return -1;
        else if (this.userId == that.userId)
            return 0;
        else
            return 1;
    }
    
}
