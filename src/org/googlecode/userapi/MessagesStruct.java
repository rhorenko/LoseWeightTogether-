package org.googlecode.userapi;

import java.util.List;

/**
 * Structure that wraps response to act=message|inbox|outbox request.
 *
 * @author Ayzen
 */
public class MessagesStruct {

    private int count;
    private long timestamp;
    private List<Message> messages;

    public MessagesStruct() {
    }

    public MessagesStruct(int count, long timestamp, List<Message> messages) {
        this.count = count;
        this.timestamp = timestamp;
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
