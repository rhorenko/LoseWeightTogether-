package org.googlecode.vkontakte_android.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.googlecode.vkontakte_android.provider.UserapiProvider;

import java.util.Date;
import java.util.List;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.*;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.STATUSES_URI;

public class StatusDao {
    private static final String TAG = "VK:StatusDao";

    private long rowId = -1;
    private long statusId;
    private long userId;
    //    private int reserved;//0, reserved (rather user's gender?)
    private String userName;
    private Date date;
    private String text;
    private boolean personal;

//    public static int bulkSave(Context context, List<StatusDao> statusList) {
//        ContentValues[] values = new ContentValues[statusList.size()];
//        int i = 0;
//        for (StatusDao status : statusList) {
//            ContentValues insertValues = new ContentValues();
//            insertValues.put(KEY_STATUS_STATUSID, status.getStatusId());
//            insertValues.put(KEY_STATUS_USERID, status.getUserId());
//            insertValues.put(KEY_STATUS_USERNAME, status.getUserName());
//            insertValues.put(KEY_STATUS_DATE, status.getDate().getTime());
//            insertValues.put(KEY_STATUS_TEXT, status.getText());
//            values[i++] = insertValues;
//        }
//        return context.getContentResolver().bulkInsert(STATUSES_URI, values);
//    }

    public static int bulkSaveOrUpdate(Context context, List<StatusDao> statusList) {
        int updated = 0;
        for (StatusDao status : statusList) {
            updated += status.saveOrUpdate(context);
        }
        context.getContentResolver().notifyChange(UserapiProvider.STATUSES_URI, null);
        Log.d(TAG, "Statuses updated:"+updated);
        return updated;
    }

    public static StatusDao get(Context context, long rowId) {
        if (rowId == -1) return null;
        Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(STATUSES_URI, rowId), null, null, null, null);
        StatusDao status = null;
        if (cursor != null && cursor.moveToNext()) {
            status = new StatusDao(cursor);
            cursor.close();
        } else if (cursor != null) cursor.close();
        return status;
    }

    public static StatusDao findByStatusId(Context context, long statusId) {
        if (statusId == -1) return null;
        Cursor cursor = context.getContentResolver().query(STATUSES_URI, null, KEY_STATUS_STATUSID + "=?", new String[]{String.valueOf(statusId)}, null);
        StatusDao status = null;
        if (cursor != null && cursor.moveToNext()) {
            status = new StatusDao(cursor);
            cursor.close();
        } else if (cursor != null) cursor.close();
        return status;
    }

    //does not notify!
    public int saveOrUpdate(Context context) {
        StatusDao status = StatusDao.findByStatusId(context, statusId);
        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_STATUS_STATUSID, getStatusId());
        insertValues.put(KEY_STATUS_USERID, getUserId());
        insertValues.put(KEY_STATUS_PERSONAL, isPersonal() ? 1 : 0);
        insertValues.put(KEY_STATUS_USERNAME, getUserName());
        insertValues.put(KEY_STATUS_DATE, getDate().getTime());
        insertValues.put(KEY_STATUS_TEXT, getText());
        if (status == null) {
            context.getContentResolver().insert(STATUSES_URI, insertValues);
            return 1;
        } else {
            context.getContentResolver().update(ContentUris.withAppendedId(STATUSES_URI, status.rowId), insertValues, null, null);
            return 0;
        }
    }

    public StatusDao(Cursor cursor) {
        rowId = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_STATUS_ROWID));
        statusId = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_STATUS_STATUSID));
        userId = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_STATUS_USERID));
        userName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS_USERNAME));
        date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_STATUS_DATE)));
        text = cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS_TEXT));
        personal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS_PERSONAL)) == 1;
    }

    public StatusDao(long statusId, long userId, String userName, Date date, String text, boolean personal) {
        this.statusId = statusId;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.text = text;
        this.personal = personal;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public long getStatusId() {
        return statusId;
    }

    public String getText() {
        return text;
    }

    public boolean isPersonal() {
        return personal;
    }

    @Override
    public String toString() {
        return "StatusDao{" +
                "id=" + rowId +
                ", statusId=" + statusId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", personal=" + personal +
                '}';
    }
}