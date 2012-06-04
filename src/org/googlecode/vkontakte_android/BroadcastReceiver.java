package org.googlecode.vkontakte_android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.googlecode.vkontakte_android.service.UpdatesNotifier;
import org.googlecode.vkontakte_android.utils.AppHelper;

/**
 * Currently receiver can handle only AppHelper.ACTION_NOTIFICATION_CLEARED intent (when user clears all notifications
 * in the status bar)
 *
 * @author Ayzen
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private static final String TAG = "VK:BReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received intent: " + intent);

        if (AppHelper.ACTION_NOTIFICATION_CLEARED.equals(intent.getAction()))
            UpdatesNotifier.notificationActive = false;

        //TODO: handle low battery intents to reduce timer interval of CheckingService, etc...
    }

}
