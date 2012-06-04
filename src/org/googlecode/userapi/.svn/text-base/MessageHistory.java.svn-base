package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * @author Ayzen
 */
public class MessageHistory {

    public enum Type {add, del, undel, restore, read}

    private long timestamp;
    private Type type;
    private Message message;

    public MessageHistory() {
    }

    public MessageHistory(JSONArray messageHistoryInfo, VkontakteAPI api) throws JSONException {
        timestamp = messageHistoryInfo.getLong(0);

        String typeStr = messageHistoryInfo.getString(1);
        try {
            type = Type.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new JSONException("Couldn't determine type of message history: " + typeStr);
        }

        message = new Message(messageHistoryInfo.getJSONArray(2), api);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
