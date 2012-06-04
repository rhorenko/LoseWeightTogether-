package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Photo {
    private long userId;
    private long globalUserId;
    private long photoId;
    private String thumbnailUrl;
    private String imageUrl;

    private static Pattern p = Pattern.compile(".*userapi.com/u(\\d+)/.*");

    public Photo(long globalUserId, long photoId, String thumbnailUrl, String imageUrl, long userId) {
        this.globalUserId = globalUserId;
        this.photoId = photoId;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public static Photo fromJson(JSONArray photoInfo) throws JSONException {
        //todo: handle no photo set - images/question_b.gif
        String userAndPhotoString = photoInfo.getString(0);
        if (!userAndPhotoString.equalsIgnoreCase("_0")) {
            long globalUserId = Long.parseLong(userAndPhotoString.split("_")[0]);
            long photoId = Long.parseLong(userAndPhotoString.split("_")[1]);
            String thumbnailUrl = photoInfo.getString(1);
            String imageUrl = photoInfo.getString(2);
            long userId = getUserIdFromPhotoUrl(thumbnailUrl);
            return new Photo(globalUserId, photoId, thumbnailUrl, imageUrl, userId);
        } else {
            //temporary unavailable photo
            return null;
        }
    }

    private static long getUserIdFromPhotoUrl(String url) {
        Matcher m = p.matcher(url);
        if (m.matches())
            return Long.parseLong(m.group(1));
        else return -1;
    }

    public long getUserId() {
        return userId;
    }

    public long getGlobalUserId() {
        return globalUserId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "userId=" + globalUserId +
                ", realUserId=" + userId +
                ", photoId=" + photoId +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}