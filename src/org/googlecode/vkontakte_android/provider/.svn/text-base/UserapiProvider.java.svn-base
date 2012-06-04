package org.googlecode.vkontakte_android.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import org.googlecode.vkontakte_android.utils.AppHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.*;

public class UserapiProvider extends ContentProvider {

    private static final String TAG = "VK:UserapiProvider";

    public static final Uri USERS_URI = Uri.parse("content://org.googlecode.vkontakte_android/users");
    public static final Uri MESSAGES_URI = Uri.parse("content://org.googlecode.vkontakte_android/messages");
    public static final Uri WALL_URI = Uri.parse("content://org.googlecode.vkontakte_android/wall");
    public static final Uri PROFILES_URI = Uri.parse("content://org.googlecode.vkontakte_android/profiles");
    public static final Uri STATUSES_URI = Uri.parse("content://org.googlecode.vkontakte_android/statuses");

    public static final String MESSAGES_TYPE = "vnd.android.cursor.dir/vnd.org.googlecode.vkontakte_android.message";
    public static final String MESSAGES_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.googlecode.vkontakte_android.message";

    private static final int ALL_USERS = 1;
    private static final int SINGLE_USER = 2;
    private static final int ALL_MESSAGES = 3;
    private static final int SINGLE_MESSAGE = 4;
    private static final int ALL_WALL = 7;
    private static final int SINGLE_WALL = 8;
    private static final int ALL_PROFILES = 9;
    private static final int SINGLE_PROFILE = 10;
    private static final int ALL_STATUSES = 11;
    private static final int SINGLE_STATUS = 12;

    private static UriMatcher uriMatcher;
    private SQLiteDatabase database;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "users", ALL_USERS);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "users/#", SINGLE_USER);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "messages", ALL_MESSAGES);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "messages/#", SINGLE_MESSAGE);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "wall", ALL_WALL);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "wall/#", SINGLE_WALL);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "profiles", ALL_PROFILES);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "profiles/#", SINGLE_PROFILE);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "statuses", ALL_STATUSES);
        uriMatcher.addURI("org.googlecode.vkontakte_android", "statuses/#", SINGLE_STATUS);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        createFolders();
        UserapiDatabaseHelper databaseHelper = new UserapiDatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return (database != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        String table;
        String column = null;
        String mySort;

        int dataType = uriMatcher.match(uri);
        switch (dataType) {
            case ALL_USERS:
                table = DATABASE_USERS_TABLE;
                mySort = KEY_USER_ID;
                break;
            case SINGLE_USER:
                table = DATABASE_USERS_TABLE;
                mySort = KEY_USER_ID;
                column = KEY_USER_ID;
                break;
            case ALL_MESSAGES:
                table = DATABASE_MESSAGES_TABLE;
                mySort = KEY_MESSAGE_ID;
                break;
            case SINGLE_MESSAGE:
                table = DATABASE_MESSAGES_TABLE;
                mySort = KEY_MESSAGE_ID;
                column = KEY_MESSAGE_ID;
                break;
            case ALL_WALL:
                table = DATABASE_WALL_TABLE;
                mySort = KEY_WALL_ROWID;
                break;
            case SINGLE_WALL:
                table = DATABASE_WALL_TABLE;
                mySort = KEY_WALL_ROWID;
                column = KEY_WALL_ROWID;
                break;
            case ALL_PROFILES:
                table = DATABASE_PROFILE_TABLE;
                mySort = KEY_PROFILE_ROWID;
                break;
            case SINGLE_PROFILE:
                table = DATABASE_PROFILE_TABLE;
                mySort = KEY_PROFILE_ROWID;
                column = KEY_PROFILE_ROWID;
                break;
            case ALL_STATUSES:
                table = DATABASE_STATUS_TABLE;
                mySort = KEY_STATUS_ROWID;
                break;
            case SINGLE_STATUS:
                table = DATABASE_STATUS_TABLE;
                mySort = KEY_STATUS_ROWID;
                column = KEY_STATUS_ROWID;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);
        String orderBy;
        if (TextUtils.isEmpty(sort)) {
            orderBy = mySort;
        } else {
            orderBy = sort;
        }
        if (column != null) selection = column + "="
                + uri.getPathSegments().get(1)
                + (!TextUtils.isEmpty(selection) ? " AND ("
                + selection + ')' : "");
        Cursor cursor = builder.query(database, projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                return "vnd.android.cursor.dir/vnd.org.googlecode.vkontakte_android.user";
            case SINGLE_USER:
                return "vnd.android.cursor.item/vnd.org.googlecode.vkontakte_android.user";
            case ALL_MESSAGES:
                return MESSAGES_TYPE;
            case SINGLE_MESSAGE:
                return MESSAGES_ITEM_TYPE;
//            case ALL_FILES:
//                return "vnd.android.cursor.dir/vnd.org.googlecode.vkontakte_android.file";
//            case SINGLE_FILE:
//                return "vnd.android.cursor.item/vnd.org.googlecode.vkontakte_android.file";
            case ALL_STATUSES:
                return "vnd.android.cursor.dir/vnd.org.googlecode.vkontakte_android.status";
            case SINGLE_STATUS:
                return "vnd.android.cursor.item/vnd.org.googlecode.vkontakte_android.status";
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String table;
        String column;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                table = DATABASE_USERS_TABLE;
                column = KEY_USER_ID;
                break;
            case ALL_MESSAGES:
                table = DATABASE_MESSAGES_TABLE;
                column = KEY_MESSAGE_ID;
                break;
            case ALL_PROFILES:
                table = DATABASE_PROFILE_TABLE;
                column = KEY_PROFILE_ROWID;
                break;
            case ALL_STATUSES:
                table = DATABASE_STATUS_TABLE;
                column = KEY_STATUS_ROWID;
                break;

//            case ALL_FILES:
//                table = DATABASE_FILES_TABLE;
//                column = KEY_FILE_ROWID;
//                break;
//            case ALL_CASTS:
//                table = DATABASE_CASTS_TABLE;
//                column = KEY_CAST_ROWID;
//                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        long rowId = database.insert(table, column, contentValues);
        if (rowId > 0) {
            Uri result = ContentUris.withAppendedId(uri, rowId);
//            getContext().getContentResolver().notifyChange(result, null);
            return result;
        } else return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        String table;
        String column = null;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                table = DATABASE_USERS_TABLE;
                break;
            case SINGLE_USER:
                table = DATABASE_USERS_TABLE;
                column = KEY_USER_ID;
                break;
            case ALL_MESSAGES:
                table = DATABASE_MESSAGES_TABLE;
                break;
            case SINGLE_MESSAGE:
                table = DATABASE_MESSAGES_TABLE;
                column = KEY_MESSAGE_ID;
                break;
            case ALL_PROFILES:
                table = DATABASE_PROFILE_TABLE;
                break;
            case SINGLE_PROFILE:
                table = DATABASE_PROFILE_TABLE;
                column = KEY_PROFILE_ROWID;
                break;
            case ALL_STATUSES:
                table = DATABASE_STATUS_TABLE;
                break;
            case SINGLE_STATUS:
                table = DATABASE_STATUS_TABLE;
                column = KEY_STATUS_ROWID;
                break;
//            case ALL_FILES:
//                table = DATABASE_FILES_TABLE;
//                break;
//            case SINGLE_FILE:
//                table = DATABASE_FILES_TABLE;
//                column = KEY_FILE_ROWID;
//                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        if (column != null) where = column + "="
                + uri.getPathSegments().get(1)
                + (!TextUtils.isEmpty(where) ? " AND ("
                + where + ')' : "");
        int count = database.delete(table, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
        String table;
        String column = null;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                table = DATABASE_USERS_TABLE;
                break;
            case SINGLE_USER:
                table = DATABASE_USERS_TABLE;
                column = KEY_USER_ID;
                break;
            case ALL_MESSAGES:
                table = DATABASE_MESSAGES_TABLE;
                break;
            case SINGLE_MESSAGE:
                table = DATABASE_MESSAGES_TABLE;
                column = KEY_MESSAGE_ID;
                break;
            case ALL_PROFILES:
                table = DATABASE_MESSAGES_TABLE;
                break;
            case SINGLE_PROFILE:
                table = DATABASE_PROFILE_TABLE;
                column = KEY_PROFILE_ROWID;
                break;
            case ALL_STATUSES:
                table = DATABASE_STATUS_TABLE;
                break;
            case SINGLE_STATUS:
                table = DATABASE_STATUS_TABLE;
                column = KEY_STATUS_ROWID;
                break;

//            case ALL_FILES:
//                table = DATABASE_FILES_TABLE;
//                break;
//            case SINGLE_FILE:
//                table = DATABASE_FILES_TABLE;
//                column = KEY_FILE_ROWID;
//                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        if (column != null) where = column + "="
                + uri.getPathSegments().get(1)
                + (!TextUtils.isEmpty(where) ? " AND ("
                + where + ')' : "");

        int count = database.update(table, contentValues, where, whereArgs);
//        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValueses) {
        database.beginTransaction();
        String table;
        String column = null;
        SQLiteStatement st = null;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                table = DATABASE_USERS_TABLE;
                st = database.compileStatement(USERS_FULL_INSERT);
                break;
            case ALL_MESSAGES:
                table = DATABASE_MESSAGES_TABLE;
                column = KEY_MESSAGE_ID;
                break;
            case ALL_PROFILES:
                table = DATABASE_PROFILE_TABLE;
                column = KEY_PROFILE_ROWID;
                break;
            case ALL_STATUSES:
                table = DATABASE_STATUS_TABLE;
                column = KEY_STATUS_ROWID;
                break;
//            case ALL_FILES:
//                table = DATABASE_FILES_TABLE;
//                column = KEY_FILE_ROWID;
//                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = 0;
        for (ContentValues values : contentValueses) {
            if (values == null) continue;

            if (st != null) {
                UserapiDatabaseHelper.bindParamsToUser(st, values);
                if (st.executeInsert() != -1)
                    count++;
            }
            else {
                if (database.insert(table, column, values) != -1)
                    count++;
            }    
        }
        database.setTransactionSuccessful();
        database.endTransaction();

        Log.d(TAG, "Inserted in: " + table + " rows: " + count);

        return count;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return this.openFileHelper(uri, mode);
    }

    private void createFolders() {
        String appDir = AppHelper.getAppDir(getContext());
        File f = new File(appDir);
        f.mkdir();
        f = new File(appDir + "profiles");
        f.mkdir();
        f = new File(appDir + "photos");
        f.mkdir();
        f = new File(AppHelper.getAvatarsDir(getContext()));
        f.mkdir();
    }

    public static boolean isExists(String path) {
        if (path == null) {
            return false;
        }
        File f = new File(path);
        return (!(!f.exists() || f.length() == 0));
    }
}