package org.googlecode.userapi;

import java.util.Map;

/**
 * Created by Ildar Karimov
 * Date: Oct 3, 2009
 */
public class UrlBuilder {
    public static final String urlBase = "http://userapi.com/";
    public static final String loginUrlBase = "http://login.userapi.com/";

    public static String makeUrl(String action) {
        String url = urlBase + "data?act=" + action;
        return url;
    }

    public static String makeUrl(String action, long id) {
        String url = urlBase + "data?act=" + action + "&id=" + id;
        return url;
    }

    public static String makeUrl(String action, long id, long timestamp) {
        return urlBase + "data?act=" + action + "&id=" + id + "&ts=" + timestamp;
    }

    public static String makeUrl(String action, long id, int from, int to) {
        String url = urlBase + "data?act=" + action + "&from=" + from + "&to=" + to + "&id=" + id;
        return url;
    }

    public static String makeUrl(String action, Map<String, String> params) {
        StringBuilder url = new StringBuilder(urlBase + "data?act=" + action);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return url.toString();
    }

    public static String makeUrl(String action, int from, int to) {
        String url = urlBase + "data?act=" + action + "&from=" + from + "&to=" + to;
        return url;
    }

    public static String makeUrl(Timestamps timestamps) {
        StringBuilder url = new StringBuilder(urlBase).append("data?act=history");

        if (timestamps != null) {
            if (timestamps.getWallTs() != -1)
                url.append("&wall=").append(timestamps.getWallTs());
            if (timestamps.getActivitiesTs() != -1)
                url.append("&activity=").append(timestamps.getActivitiesTs());
            if (timestamps.getMessagesTs() != -1)
                url.append("&message=").append(timestamps.getMessagesTs());
            if (timestamps.getPhotosUpdatesTs() != -1)
                url.append("&updates_photos=").append(timestamps.getPhotosUpdatesTs());
            if (timestamps.getTagedPhotosUpdatesTs() != -1)
                url.append("&updates_tagged_photos=").append(timestamps.getTagedPhotosUpdatesTs());
            if (timestamps.getFriendsTs() != -1)
                url.append("&updates_friends=").append(timestamps.getFriendsTs());
            if (timestamps.getActivitiesUpdatesTs() != -1)
                url.append("&updates_activity=").append(timestamps.getActivitiesUpdatesTs());
            if (timestamps.getPhotosCommentsTs() != -1)
                url.append("&photos_comments=").append(timestamps.getPhotosCommentsTs());
        }

        return url.toString();
    }

    public static String makeUrl() {
        String url = urlBase + "data?";
        return url;
    }

    public static String makeLoginUrl() {
        String url = urlBase + "auth?";
        return url;
    }

    public static String makeLoginUrl(long siteId) {
        String url = loginUrlBase + "auth?" + "site=" + siteId;
        return url;
    }

    public static String makeLoginUrl(long siteId, String action) {
        String url = loginUrlBase + "auth?login=" + action + "&site=" + siteId;
        return url;
    }
}
