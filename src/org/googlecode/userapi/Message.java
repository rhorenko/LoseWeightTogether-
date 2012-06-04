package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class Message {
    
    protected long id;
    protected Date date;
    protected String text;
    protected User sender;
    protected User receiver;
    protected long senderId;
    protected long receiverId;
    protected boolean read;

    public Message(JSONArray messageInfo, VkontakteAPI api) throws JSONException {
        id = messageInfo.getLong(0);
        date = new Date(1000 * messageInfo.getLong(1));

        Object textJson = messageInfo.get(2);
        if (textJson instanceof JSONArray) {
            JSONArray textJsonArray = (JSONArray) textJson;
            text = textJsonArray.getString(0);
            if (textJsonArray.length() > 1) {
//               System.out.println(textJsonArray);
                //todo: handle if any?
            }
        }
        sender = new User(messageInfo.getJSONArray(3), api);
        //System.out.println("Message sender: "+sender.toString());
        receiver = new User(messageInfo.getJSONArray(4), api);
        //System.out.println("Message receiver: "+receiver.toString());
        if (messageInfo.length() == 6)
            read = messageInfo.getInt(5) == 1;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", read=" + read +
                '}';
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

}


