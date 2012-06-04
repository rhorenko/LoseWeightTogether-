package org.googlecode.vkontakte_android.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class UserapiDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "VK:UserapiDatabaseHelper";

    public static final String DATABASE_USERS_TABLE = "users";
    public static final String KEY_USER_ID = BaseColumns._ID;
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_MALE = "male";
    public static final String KEY_USER_ONLINE = "online";
    public static final String KEY_USER_NEW_FRIEND = "newfriend";
    public static final String KEY_USER_IS_FRIEND = "isfriend";
    public static final String KEY_USER_AVATAR_URL = "photo_small_url";
    public static final String KEY_USER_AVATAR_SMALL = "_data";

    public static final String USERS_FULL_INSERT = "INSERT INTO " + DATABASE_USERS_TABLE + " VALUES (?,?,?,?,?,?,?,?)";

    public static final String KEY_MESSAGE_ID = BaseColumns._ID;
    public static final String KEY_MESSAGE_DATE = "date";
    public static final String KEY_MESSAGE_TEXT = "text";
    public static final String KEY_MESSAGE_SENDERID = "senderid";
    public static final String KEY_MESSAGE_RECEIVERID = "receiverid";
    public static final String KEY_MESSAGE_READ = "read";

    public static final String KEY_WALL_ROWID = BaseColumns._ID;
    public static final String KEY_WALL_TEXT = "text";
    public static final String KEY_WALL_SENDERID = "senderid";
    public static final String KEY_WALL_PIC = "pic";
    public static final String KEY_WALL_DATA = "data";

    public static final String KEY_PROFILE_ROWID = BaseColumns._ID;
    public static final String KEY_PROFILE_USERID = "userid";
    public static final String KEY_PROFILE_FIRSTNAME = "firstname";
    public static final String KEY_PROFILE_SURNAME = "surname";
    public static final String KEY_PROFILE_STATUS = "status";
    public static final String KEY_PROFILE_SEX = "sex";
    public static final String KEY_PROFILE_BIRTHDAY = "birthday";
    public static final String KEY_PROFILE_PHONE = "phone";
    public static final String KEY_PROFILE_PHOTO = "photo";
    public static final String KEY_PROFILE_PV = "pv";
    public static final String KEY_PROFILE_FS = "fs";
    public static final String KEY_PROFILE_CURRENT_CITY = "current_city";

    public static final String KEY_STATUS_ROWID = BaseColumns._ID;
    public static final String KEY_STATUS_STATUSID = "statusid";
    public static final String KEY_STATUS_PERSONAL = "personal";
    public static final String KEY_STATUS_USERID = "userid";
    public static final String KEY_STATUS_USERNAME = "username";
    public static final String KEY_STATUS_DATE = "date";
    public static final String KEY_STATUS_TEXT = "text";

    public static final String DATABASE_NAME = "userapi";
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_USERS_CREATE = "create table " + DATABASE_USERS_TABLE + " (" +
            KEY_USER_ID + " long primary key, " +
            KEY_USER_NAME + " text, " +
            KEY_USER_MALE + " int, " +
            KEY_USER_ONLINE + " int, " +
            KEY_USER_NEW_FRIEND + " int, " +
            KEY_USER_IS_FRIEND + " int, " +
            KEY_USER_AVATAR_URL + " text, " +
            KEY_USER_AVATAR_SMALL + " text" +
            ");";

    public static final String DATABASE_MESSAGES_TABLE = "messages";
    private static final String DATABASE_MESSAGES_CREATE = "create table " + DATABASE_MESSAGES_TABLE + " (" +
            KEY_MESSAGE_ID + " long primary key, " +
            KEY_MESSAGE_DATE + " long, " +
            KEY_MESSAGE_TEXT + " text , " +
            KEY_MESSAGE_SENDERID + " long, " +
            KEY_MESSAGE_RECEIVERID + " long, " +
            KEY_MESSAGE_READ + " int " +
            ");";

    public static final String DATABASE_WALL_TABLE = "wall";
    private static final String DATABASE_WALL_CREATE = "create table " + DATABASE_WALL_TABLE + " ("
            + KEY_WALL_ROWID + " integer primary key autoincrement, "
            + KEY_MESSAGE_SENDERID + " long"
            + KEY_WALL_TEXT + " text, "
            + KEY_WALL_PIC + " blob "
            + ");";

    public static final String DATABASE_PROFILE_TABLE = "profiles";
    private static final String DATABASE_PROFILE_CREATE = "create table " + DATABASE_PROFILE_TABLE + " ("
            + KEY_PROFILE_ROWID + " integer primary key autoincrement, "
            + KEY_PROFILE_USERID + " long,"
            + KEY_PROFILE_FIRSTNAME + " text, "
            + KEY_PROFILE_SURNAME + " text, "
            + KEY_PROFILE_STATUS + " text, "
            + KEY_PROFILE_SEX + " long,"
            + KEY_PROFILE_BIRTHDAY + " long,"
            + KEY_PROFILE_PHONE + " text,"
            + KEY_PROFILE_PHOTO + " text,"
            + KEY_PROFILE_FS + " int,"
            + KEY_PROFILE_PV + " int,"
            + KEY_PROFILE_CURRENT_CITY + " text"
            + ");";

    public static final String DATABASE_STATUS_TABLE = "statuses";
    private static final String DATABASE_STATUS_CREATE = "create table " + DATABASE_STATUS_TABLE + " ("
            + KEY_STATUS_ROWID + " integer primary key autoincrement, "
            + KEY_STATUS_STATUSID + " long,"
            + KEY_STATUS_PERSONAL + " int,"
            + KEY_STATUS_USERID + " long,"
            + KEY_STATUS_USERNAME + " text, "
            + KEY_STATUS_DATE + " long,"
            + KEY_STATUS_TEXT + " text"
            + ");";


    public static void bindParamsToUser(SQLiteStatement st, ContentValues values) {
        DatabaseUtils.bindObjectToProgram(st, 1, values.get(KEY_USER_ID));
        DatabaseUtils.bindObjectToProgram(st, 2, values.get(KEY_USER_NAME));
        DatabaseUtils.bindObjectToProgram(st, 3, values.get(KEY_USER_MALE));
        DatabaseUtils.bindObjectToProgram(st, 4, values.get(KEY_USER_ONLINE));
        DatabaseUtils.bindObjectToProgram(st, 5, values.get(KEY_USER_NEW_FRIEND));
        DatabaseUtils.bindObjectToProgram(st, 6, values.get(KEY_USER_IS_FRIEND));
        DatabaseUtils.bindObjectToProgram(st, 7, values.get(KEY_USER_AVATAR_URL));
        DatabaseUtils.bindObjectToProgram(st, 8, values.get(KEY_USER_AVATAR_SMALL));
    }

    public UserapiDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_USERS_CREATE);
        db.execSQL(DATABASE_MESSAGES_CREATE);
        db.execSQL(DATABASE_PROFILE_CREATE);
        db.execSQL(DATABASE_STATUS_CREATE);
        db.execSQL(DATABASE_WALL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_MESSAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_WALL_TABLE);
        onCreate(db);
    }
}
