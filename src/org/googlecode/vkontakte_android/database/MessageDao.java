package org.googlecode.vkontakte_android.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.googlecode.userapi.Message;
import org.googlecode.userapi.MessageHistory;
import org.googlecode.userapi.User;
import org.googlecode.vkontakte_android.provider.UserapiProvider;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;

import java.util.Date;
import java.util.List;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.*;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.MESSAGES_URI;

public class MessageDao extends Message {

    private static final String TAG = "VK:MessageDao";

    public long id;
    public long date;
    public String text;
    public long senderId;
    public long receiverId;
    public boolean read;

    private User sender = null;
    private User receiver = null;

    public MessageDao(Cursor cursor) {
        this.id = cursor.getLong(0);
        this.date = cursor.getLong(1);
        this.text = cursor.getString(2);
        this.senderId = cursor.getLong(3);
        this.receiverId = cursor.getLong(4);
        this.read = cursor.getInt(5) == 1;
    }

    /*
     * Use this only when you don't need to save it to DB
     */
    public MessageDao(long id, Date date, String text, long senderId, long receiverId, boolean read) {
        this.id = id;
        this.date = date.getTime();
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.read = read;
    }

    public MessageDao(Message mess) {
        this.id = mess.getId();
        this.date = mess.getDate().getTime();
        this.text = mess.getText();
        this.senderId = mess.getSender().getUserId();
        this.receiverId = mess.getReceiver().getUserId();
        this.read = mess.isRead();
        sender = mess.getSender();
        receiver = mess.getReceiver();
    }

//    //TODO add sender/receiver
//    public static int bulkSave(Context context, List<MessageDao> messages) {
//        ContentValues[] values = new ContentValues[messages.size()];
//        int i = 0;
//        for (MessageDao messageDao : messages) {
//            ContentValues insertValues = new ContentValues();
//            insertValues.put(KEY_MESSAGE_MESSAGEID, messageDao.getId());
//            insertValues.put(KEY_MESSAGE_DATE, messageDao.getDate().getTime());
//            insertValues.put(KEY_MESSAGE_TEXT, messageDao.getText());
//            insertValues.put(KEY_MESSAGE_SENDERID, messageDao.getSenderId());
//            insertValues.put(KEY_MESSAGE_RECEIVERID, messageDao.getReceiverId());//todo: save if not exist?
//            insertValues.put(KEY_MESSAGE_READ, messageDao.isRead() ? 0 : 1);
//            values[i] = insertValues;
//            i++;
//        }
//        return context.getContentResolver().bulkInsert(MESSAGES_URI, values);
//    }

//    public static MessageDao findAllBySenderOrReceiver(Context context, long userId) {
//        if (userId == -1) return null;
//        Cursor cursor = context.getContentResolver().query(MESSAGES_URI, null, KEY_MESSAGE_SENDERID + "=?" + " OR " + KEY_MESSAGE_RECEIVERID + "=?", new String[]{String.valueOf(userId), String.valueOf(userId)}, null);
//        MessageDao messageDao = null;
//        if (cursor != null && cursor.moveToNext()) {
//            messageDao = new MessageDao(cursor);
//            cursor.close();
//        } else if (cursor != null) cursor.close();
//        return messageDao;
//    }

    public void add(Context context) {
        Log.d(TAG, "Inserting message with id = " + id + " from " + sender.getUserId() + "(" + sender.getUserName() +
                ") to " + receiver.getUserId() + "(" + receiver.getUserName() + ")");

        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_MESSAGE_ID, id);
        insertValues.put(KEY_MESSAGE_DATE, date);
        insertValues.put(KEY_MESSAGE_TEXT, text);
        insertValues.put(KEY_MESSAGE_SENDERID, senderId);
        insertValues.put(KEY_MESSAGE_RECEIVERID, receiverId);
        insertValues.put(KEY_MESSAGE_READ, read ? 1 : 0);

        saveUserIfNeed(context, sender);
        saveUserIfNeed(context, receiver);

        context.getContentResolver().insert(UserapiProvider.MESSAGES_URI, insertValues);
    }

    public void delete(Context context) {
        Log.d(TAG, "Deleting message with id = " + id);
        context.getContentResolver().delete(ContentUris.withAppendedId(MESSAGES_URI, id), null, null);
    }

    /**
     * Sets read flag of this message and saves it to DB.
     *
     * @param ctx application context
     */
    public void read(Context ctx) {
        Log.d(TAG, "Reading message with id = " + id);

        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_MESSAGE_READ, 1);
        ctx.getContentResolver().update(ContentUris.withAppendedId(UserapiProvider.MESSAGES_URI, id), updateValues, null, null);
    }

    public long getId(){
    	return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public UserDao getSender(Context ctx) {
        return UserDao.get(ctx, senderId);
    }

    public UserDao getReceiver(Context ctx) {
        return UserDao.get(ctx, receiverId);
    }

    public boolean saveUserIfNeed(Context ctx, User user) {
        if (user == null) {
            return false;
        }
        if (user.getUserId() != PreferenceHelper.getMyId(ctx)) {
            UserDao.saveOrUpdate(ctx, user);
        }
        return true;
    }

    public static MessageDao get(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(MESSAGES_URI, id), null, null, null, null);
        try {
            if (cursor != null && cursor.moveToNext())
                return new MessageDao(cursor);
            else
                return null;
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public static void applyMessagesHistory(Context ctx, List<MessageHistory> history) {
        if (history.size() == 0)
            return;

        Log.v(TAG, "Applying messages history changes");

        long timestamp = -1;
        for (MessageHistory mh : history) {
            timestamp = mh.getTimestamp();
            MessageDao msg = new MessageDao(mh.getMessage());
            switch (mh.getType()) {
                case add:
                case restore:
                case undel:    
                    msg.add(ctx);
                    break;
                case del:
                    msg.delete(ctx);
                    break;
                case read:
                    msg.read(ctx);
                    break;
            }
        }

        PreferenceHelper.setMessagesTimestamp(ctx, timestamp);
        ctx.getContentResolver().notifyChange(UserapiProvider.MESSAGES_URI, null);
    }

    public static int getInboxMessagesCount(Context ctx) {
        String selectInbox = KEY_MESSAGE_RECEIVERID + "=" + PreferenceHelper.getMyId(ctx);

        ContentResolver cr = ctx.getContentResolver();
        Cursor cursor = cr.query(MESSAGES_URI, null, selectInbox, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public static int getOutboxMessagesCount(Context ctx) {
        String selectOutbox = KEY_MESSAGE_SENDERID + "=" + PreferenceHelper.getMyId(ctx);

        ContentResolver cr = ctx.getContentResolver();
        Cursor cursor = cr.query(MESSAGES_URI, null, selectOutbox, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public static int delete(Context context, long id) {
        return context.getContentResolver().delete(ContentUris.withAppendedId(MESSAGES_URI, id), null, null);
    }

    public static void deleteAllMessages(Context ctx) {
        Log.v(TAG, "Deleting all messages");
        ContentResolver cr = ctx.getContentResolver();
        cr.delete(MESSAGES_URI, null, null);
        PreferenceHelper.setMessagesTimestamp(ctx, -1);
    }

}