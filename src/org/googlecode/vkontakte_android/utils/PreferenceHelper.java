package org.googlecode.vkontakte_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.googlecode.userapi.Credentials;


public class PreferenceHelper {
    
    public static final String TAG = "VK:PreferenceHelper";

    public static final String LOGIN_STATUS = "is_logged";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String REMIX = "remix";
    public static final String SID = "sid";
    public static final String SYNC_PERIOD = "sync_period";
    public static final String MY_ID = "my_id";
    public static final String TS_MESSAGES = "ts_messages";
    public static final int SYNC_INTERVAL_NEVER = -1;
    private static final boolean DEFAULT_LOGIN_STATUS = false;
    private static final String DEFAULT_LOGIN_NAME = "";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_REMIX = null;
    private static final String DEFAULT_SID = null;
    private static final int DEFAULT_SYNC_INTERVAL = SYNC_INTERVAL_NEVER;


    /**
     * Get login status
     *
     * @param ctx
     * @return true if login/pass has been provided, false if not
     */
    public static boolean isLogged(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(LOGIN_STATUS, DEFAULT_LOGIN_STATUS);
    }

    /**
     * Set login status
     *
     * @param context
     * @param logged
     */
    public static void setLogged(Context context, boolean logged) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(LOGIN_STATUS, logged).commit();
    }

    public static Credentials getCredentials(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String login = prefs.getString(LOGIN, DEFAULT_LOGIN_NAME);
        String pass = prefs.getString(PASSWORD, DEFAULT_PASSWORD);
        String remix = prefs.getString(REMIX, DEFAULT_REMIX);
        String sid = prefs.getString(SID, DEFAULT_SID);
        return new Credentials(login, pass, remix, sid);
    }

    public static boolean shouldLoadPics(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean("pics", true);
    }


    public static void clearPrivateInfo(Context ctx) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        ed.remove(LOGIN);
        ed.commit();
        ed.remove(PASSWORD);
        ed.commit();
        ed.remove(REMIX);
        ed.commit();
        ed.remove(SID);
        ed.commit();
    }

    public static long getMyId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(MY_ID, 0L);
    }

    public static void saveMyId(Context context, long myId) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putLong(MY_ID, myId).commit();
    }

    /**
     * Get sync interval
     *
     * @param context
     * @return interval in minutes
     */
    public static int getSyncPeriod(final Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(pref.getString(SYNC_PERIOD, String.valueOf(DEFAULT_SYNC_INTERVAL)));
    }

    public static boolean getNotifications(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("notif", true);
    }

    public static void saveLogin(Context context, Credentials credentials) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(LOGIN, credentials.getLogin()).commit();
        ed.putString(PASSWORD, credentials.getPass()).commit();
        if (credentials.getRemixpass() != null) {
            ed.putString(REMIX, credentials.getRemixpass()).commit();
        }
        if (credentials.getSid() != null) {
            ed.putString(SID, credentials.getSid()).commit();
        }
        setLogged(context, true);
    }

    public static long getMessagesTimestamp(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(TS_MESSAGES, -1);
    }

    public static void setMessagesTimestamp(Context context, long timestamp) {
        Log.v(TAG, "New messages timestamp: " + timestamp);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong(TS_MESSAGES, timestamp).commit();
    }

}