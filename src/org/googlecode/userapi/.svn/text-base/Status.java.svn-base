package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class Status {
    private long statusId;
    private long userId;
    private int reserved;//0, reserved (rather user's gender?)
    private String userName;
    private Date date;
    private String text;

    public static Status fromJson(JSONArray statusJson) throws JSONException {
        if (statusJson.getString(0).split("_").length == 1 || statusJson.getString(0).split("_")[1].equals("-1"))
            return null;
        long statusId = Long.parseLong(statusJson.getString(0).split("_")[1]);
        long userId = statusJson.getLong(1);
//        reserved = statusJson.getInt(2);
        String userName = statusJson.getString(3);
        Date date = new Date(statusJson.getLong(4) * 1000);
        String text = statusJson.getString(5);
        return new Status(statusId, userId, 0, userName, date, text);
    }

    public Status(long statusId, long userId, int reserved, String userName, Date date, String text) {
        this.statusId = statusId;
        this.userId = userId;
        this.reserved = reserved;
        this.userName = userName;
        this.date = date;
        this.text = text;
    }

    public long getStatusId() {
        return statusId;
    }

    public long getUserId() {
        return userId;
    }

    public int getReserved() {
        return reserved;
    }

    public String getUserName() {
        return userName;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusId=" + statusId +
                ", userId=" + userId +
                ", reserved=" + reserved +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
