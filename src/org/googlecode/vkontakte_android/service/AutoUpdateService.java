package org.googlecode.vkontakte_android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.googlecode.vkontakte_android.utils.AppHelper;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Light service for periodically checking updates from VKontakte.
 *
 * @author Ayzen
 */
public class AutoUpdateService extends Service {

    private static final String TAG = "VK:AutoUpdateService";

    private int wasPeriod = PreferenceHelper.SYNC_INTERVAL_NEVER;

    private Timer timer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "Service created");
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        
    	Log.v(TAG, "Started command: " + intent);
    	String action = intent.getAction();
        if (AppHelper.ACTION_SET_AUTOUPDATE.equals(action))
            processAutoUpdate(intent.getIntExtra(AppHelper.EXTRA_AUTOUPDATE_PERIOD, PreferenceHelper.SYNC_INTERVAL_NEVER));
    }
    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Started command: " + intent);
           
        String action = intent.getAction();
        if (AppHelper.ACTION_SET_AUTOUPDATE.equals(action))
            processAutoUpdate(intent.getIntExtra(AppHelper.EXTRA_AUTOUPDATE_PERIOD, PreferenceHelper.SYNC_INTERVAL_NEVER));

        return START_STICKY;
    }
*/
    /**
     * Starts auto update timer with period specified by user in preferences.
     *
     * @param period time period between auto updates
     */
    private void processAutoUpdate(int period) {
        if (wasPeriod == period) {
            Log.d(TAG, "Auto update is already working with the same period");
            return;
        }
        wasPeriod = period;

        // In any case we should first cancel the timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (period == PreferenceHelper.SYNC_INTERVAL_NEVER) {
            Log.d(TAG, "Scheduled updates disabled by user");
            stopSelf();
            return;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new AutoUpdateTask(), 0, period * 1000 * 60);
        Log.d(TAG, "Scheduled updates started with period: " + period + " minutes");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Task that sends auto update Intent.
     */
    private class AutoUpdateTask extends TimerTask {

        @Override
        public void run() {
            Log.v(TAG, "Sending check updates intent");
            startService(new Intent(AppHelper.ACTION_CHECK_UPDATES));
        }

    }

}
