package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

/**
 * Wall message
 */
public class WallMessage {

	//types TEXT by default
    public static final int TEXT = 0;
    public static final int FOTO = 1;
    public static final int GRAFFITY = 2;
    public static final int VIDEO = 3;
    public static final int AUDIO = 4;

    private long id;
    private Date date;
    
    private String text;
    private int type = TEXT;
    private String title;
    private String previewUrl;
    private String contentUrl;
    private long ownerId;
    private long objectId;
    private long objectSize;
    
    private User sender;
    private User receiver;
    
    /**
     * @deprecated what for?
     */
    private boolean read;

    /**
     * Constructing WallMessage bean 
     * @param message
     * @throws JSONException
     */
    public WallMessage(JSONArray message) throws JSONException {
        // [0]
    	id = message.getLong(0);

    	// [1]
        date = new Date(1000 * message.getLong(1));

        // [2]
        JSONArray messageContent = message.getJSONArray(2);
        text = messageContent.getString(0);
        if (messageContent.length() > 1) {
            type = messageContent.getInt(1);
            if ((type == FOTO) || (type == GRAFFITY) || (type == VIDEO)) {
                previewUrl = messageContent.getString(3);
                contentUrl = messageContent.getString(4);
            } else if (type == AUDIO) {
                contentUrl = messageContent.getString(4);
            }
        }

        // [3]
        sender = new User(message.getJSONArray(3));

        // [4]
        receiver = new User(message.getJSONArray(4));
    }

    /**
     * @deprecated
     */
    public WallMessage(JSONArray jsonArray, VkontakteAPI api) throws JSONException {
        id = jsonArray.getLong(0);
        date = new Date(1000 * jsonArray.getLong(1));
        JSONArray textJsonArray = jsonArray.getJSONArray(2);
        text = textJsonArray.getString(0);
        if (textJsonArray.length() > 1) {
            type = textJsonArray.getInt(1);
            if (type == FOTO || type == GRAFFITY || type == VIDEO) {
                previewUrl = textJsonArray.getString(3);
            }
            if (type == FOTO || type == GRAFFITY || type == AUDIO || type == VIDEO) {
                contentUrl = textJsonArray.getString(4);
            }
        }
        sender = new User(jsonArray.getJSONArray(3), api);
        receiver = new User(jsonArray.getJSONArray(4), api);
        if (jsonArray.length() == 6)
            read = jsonArray.getInt(5) == 1;
    }

    public WallMessage(long id, Date date, String text, User sender, User receiver, int type, boolean read, String contentUrl, String previewUrl) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.read = read;
        this.contentUrl = contentUrl;
        this.previewUrl = previewUrl;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
