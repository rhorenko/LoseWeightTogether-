package org.googlecode.vkontakte_android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.googlecode.userapi.ChangesHistory;
import org.googlecode.vkontakte_android.*;
import org.googlecode.vkontakte_android.provider.UserapiProvider;
import org.googlecode.vkontakte_android.utils.AppHelper;

public class UpdatesNotifier {

    private static final String TAG = "VK:Notifier";

    private static final int HISTORY_ID = 1;

    /**
     * State of notification in the status bar. True if it is active now.
     */
    public static boolean notificationActive = false;

    public static void showError(final Context ctx, final int error) {
        // I don't think it's a good idea to use toast notification about errors from services. Normally that should be
        // done from activities. If every application would use toasts from services, then in case of network problems
        // user will get dozen of toasts with "network problem" notification.

        // Here we got thread leak: every showError() usage will create thread and Looper.loop() will hang that thread.
        //TODO: use Handler or use this method only from UI threads
        /*new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();*/
    }

    public static void notifyChangesHistory(Context ctx, ChangesHistory changesHistory, boolean newEvents) {
        Log.v(TAG, "Updating notification info: " + changesHistory);

        // If user has cleared notification, we should create new one only if there were new events
        if (!notificationActive && !newEvents)
            return;

        int friends = changesHistory.getFriendsCount();
        int messages = changesHistory.getMessagesCount();
        int photos = changesHistory.getPhotosCount();

        String notificationText;
        Intent notificationIntent = null;

        if (friends == 0 && messages == 0 && photos == 0) {
            clearNotification(ctx);
            return;
        }
        else if (friends > 0 && messages == 0 && photos == 0) {
            notificationText = friends > 1
                    ? ctx.getString(R.string.notif_new_friends)
                    : ctx.getString(R.string.notif_new_friend);
        }
        else if (friends == 0 && messages > 0 && photos == 0) {
            notificationText = messages > 1
                    ? ctx.getString(R.string.notif_new_messages)
                    : ctx.getString(R.string.notif_new_message);
            notificationIntent = new Intent(Intent.ACTION_VIEW, UserapiProvider.MESSAGES_URI);
        }
        else if (friends == 0 && messages == 0 && photos > 0) {
            notificationText = photos > 1
                    ? ctx.getString(R.string.notif_new_photos)
                    : ctx.getString(R.string.notif_new_photo);
        }
        else {
            notificationText = ctx.getString(R.string.notif_new_events);
        }

        StringBuilder text = new StringBuilder();
        if (friends > 0)
            text.append(ctx.getString(R.string.notif_friends)).append(" ").append(friends).append("; ");
        if (messages > 0)
            text.append(ctx.getString(R.string.notif_messages)).append(" ").append(messages).append("; ");
        if (photos > 0)
            text.append(ctx.getString(R.string.notif_photos)).append(" ").append(photos).append("; ");

        Notification notification = new Notification(R.drawable.vkontakte_icon_48, notificationText, System.currentTimeMillis());
        // This intent will be thrown if user clears all notifications
        Intent deleteIntent = new Intent(AppHelper.ACTION_NOTIFICATION_CLEARED);
        notification.deleteIntent = PendingIntent.getBroadcast(ctx, 0, deleteIntent, 0);

        if (notificationIntent == null)
            notificationIntent = new Intent(ctx, HomeGridActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);

        notification.setLatestEventInfo(ctx, notificationText, text, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(HISTORY_ID, notification);

        notificationActive = true;
    }

    public static void clearNotification(Context ctx) {
        if (notificationActive) {
            NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(HISTORY_ID);
            notificationActive = false;
        }    
    }

}