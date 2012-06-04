package org.googlecode.vkontakte_android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import org.googlecode.userapi.*;
import org.googlecode.vkontakte_android.database.MessageDao;
import org.googlecode.vkontakte_android.database.StatusDao;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.provider.UserapiProvider;
import org.googlecode.vkontakte_android.utils.AppHelper;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

//TODO check for thread-safety!!1 

public class CheckingService extends Service {

	private static final String TAG = "VK:CheckingService";

    public static final int MESSAGE_NUM_LOAD = 10;
    public static final int STATUS_NUM_LOAD = 6;

    private List<Thread> threads = Collections.synchronizedList(new LinkedList<Thread>());

    private ChangesHistory prevChangesHistory = new ChangesHistory();

    public enum ContentToUpdate {
        FRIENDS, MESSAGES_ALL, MESSAGES_IN, MESSAGES_OUT, WALL, HISTORY, STATUSES, ALL, PROFILE
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "Service onCreate");
        super.onCreate();
        m_binder = new VkontakteServiceBinder(this);
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.v(TAG, "Started command: " + intent);
        try {
            String action = intent.getAction();
            if (AppHelper.ACTION_CHECK_UPDATES.equals(action))
                checkUpdates();
        } catch (Exception e) {
            Log.e(TAG, "Exception while checking updates", e);
            //TODO: Need to save that to show for user later...
        }
    	
       
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Started command: " + intent);

        try {
            String action = intent.getAction();
            if (AppHelper.ACTION_CHECK_UPDATES.equals(action))
                checkUpdates();
        } catch (Exception e) {
            Log.e(TAG, "Exception while checking updates", e);
            //TODO: Need to save that to show for user later...
        }

        return START_NOT_STICKY;
    }
*/
    /**
     * Check given content type for updates
     *
     * @param toUpdate   - ordinal of ContentToUpdate
     * @param syncronous
     */
    void doCheck(final int toUpdate, final Bundle updateParams, boolean syncronous) {
        if (syncronous) {
            updateContent(toUpdate, updateParams);
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateContent(toUpdate, updateParams);
                }
            });
            threads.add(t);
            t.start();
        }
    }

    private void updateContent(final int toUpdate, final Bundle updateParams) {
        ContentToUpdate what = ContentToUpdate.values()[toUpdate];
        Log.d(TAG, "updating " + what + " is starting...");
        try {
            switch (what) {
                case FRIENDS:
                    refreshFriends();
                    refreshNewFriends();
                    break;
                case WALL:
                    updateWall();
                    break;
                case MESSAGES_ALL:
                case MESSAGES_IN:
                case MESSAGES_OUT:
                    updateMessages();
                    break;
                case HISTORY:
                    checkUpdates();
                    break;
                case STATUSES:
                    updateStatuses(0, STATUS_NUM_LOAD);
                    break;
                case PROFILE:
                    //updateProfile();
                    break;

                default:
                    updateStatuses(0, STATUS_NUM_LOAD);
                    updateMessages();
                    //updateWall();
                    refreshFriends();
                    //checkUpdates();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UserapiLoginException e) {
            e.printStackTrace();
        }
    }

    // =============== updating methods

    private long loadInboxMessages(int first, int last) throws IOException, JSONException, UserapiLoginException {
        VkontakteAPI api = ApiCheckingKit.getApi();
        MessagesStruct messagesStruct = api.getInboxMessagesStruct(first, last);
        List<Message> messages = messagesStruct.getMessages();
        if (messages != null) {
            for (Message m : messages) {
                MessageDao md = new MessageDao(m);
                md.add(this);
            }
        }
        getContentResolver().notifyChange(UserapiProvider.MESSAGES_URI, null);
        return messagesStruct.getTimestamp();
    }

    private long loadOutboxMessages(int first, int last) throws IOException, JSONException, UserapiLoginException {
        VkontakteAPI api = ApiCheckingKit.getApi();
        MessagesStruct messagesStruct = api.getOutboxMessagesStruct(first, last);
        List<Message> messages = messagesStruct.getMessages();
        if (messages != null) {
            for (Message m : messages) {
                MessageDao md = new MessageDao(m);
                md.add(this);
            }
        }
        getContentResolver().notifyChange(UserapiProvider.MESSAGES_URI, null);
        return messagesStruct.getTimestamp();
    }

    protected void loadMoreMessages(ContentToUpdate type) throws IOException, JSONException, UserapiLoginException {
        int count;
        switch (type) {
            case MESSAGES_IN:
                count = MessageDao.getInboxMessagesCount(this);
                loadInboxMessages(count, count + MESSAGE_NUM_LOAD - 1);
                break;
            case MESSAGES_OUT:
                count = MessageDao.getOutboxMessagesCount(this);
                loadOutboxMessages(count, count + MESSAGE_NUM_LOAD - 1);
                break;
            case MESSAGES_ALL:
                break;
        }
    }

    private void updateMessages() throws IOException, JSONException, UserapiLoginException {
        long timestamp = PreferenceHelper.getMessagesTimestamp(this);
        // If we haven't got messages yet...
        if (timestamp == -1) {
            // Removing old messages
            MessageDao.deleteAllMessages(this);
            long inTs, outTs;
            // This loop is needed to be sure that no new messages user will receive between loadInboxMessages and
            // loadOutboxMessages calls
            do {
                inTs = loadInboxMessages(0, MESSAGE_NUM_LOAD - 1);
                outTs = loadOutboxMessages(0, MESSAGE_NUM_LOAD - 1);
            } while (inTs != outTs);
            PreferenceHelper.setMessagesTimestamp(this, inTs);
        }
        else {
            // Getting messages changes from server and applying them to DB
            VkontakteAPI api = ApiCheckingKit.getApi();
            List<MessageHistory> history = api.getPrivateMessagesHistory(timestamp);
            if (history != null)
                MessageDao.applyMessagesHistory(this, history);
            else {
                // New session
                Log.d(TAG, "New session - need to update timestamp");
                PreferenceHelper.setMessagesTimestamp(this, -1);
                updateMessages();
            }
        }
    }

    private void updateWall() {
        Log.d(TAG, "updating wall");
        // todo: implement
    }

    private void checkUpdates() throws IOException, JSONException, UserapiLoginException {
        Log.v(TAG, "Checking updates");

        Timestamps timestamps = new Timestamps();
        long timestamp = PreferenceHelper.getMessagesTimestamp(this);
        timestamps.setMessagesTs(timestamp);

        ChangesHistory changesHistory = ApiCheckingKit.getApi().getChangesHistory(timestamps);

        // Applying history changes if exist
        List<MessageHistory> messagesHistory = changesHistory.getMessagesHistory();
        if (messagesHistory != null)
            MessageDao.applyMessagesHistory(this, messagesHistory);
        else if (timestamp != -1) {
            // New session
            Log.d(TAG, "New session - need to update timestamp");
            PreferenceHelper.setMessagesTimestamp(this, -1);
            updateMessages();
        }

        int changes = prevChangesHistory.compareTo(changesHistory);

        // if there were changes and notifications are enabled in settings
        if (changes != 0 && PreferenceHelper.getNotifications(getApplicationContext())) {
            prevChangesHistory = changesHistory;

            boolean newEvents = changes == -1;
            UpdatesNotifier.notifyChangesHistory(getApplicationContext(), changesHistory, newEvents);
        }
    }

    protected void updateStatuses(int start, int end) throws IOException, JSONException {
        Log.d(TAG, "updating statuses " + start + " to " + end);
        VkontakteAPI api = ApiCheckingKit.getApi();
        List<Status> statuses = null;
        try {
            statuses = api.getTimeline(start, end);
        } catch (UserapiLoginException e) {
            e.printStackTrace();
        }
        List<StatusDao> statusDaos = new LinkedList<StatusDao>();
        if (statuses != null) {
            for (Status status : statuses) {
                boolean personal = false;
                StatusDao statusDao = new StatusDao(status.getStatusId(), status.getUserId(), status.getUserName(), status.getDate(), status.getText(), personal);
                statusDaos.add(statusDao);
            }
        }
        StatusDao.bulkSaveOrUpdate(getApplicationContext(), statusDaos);
    }

    protected void updateStatusesForUser(int start, int end, long id) throws IOException, JSONException {
        Log.d(TAG, "updating statuses for user:" + id + "/" + start + " to " + end);
        VkontakteAPI api = ApiCheckingKit.getApi();
        List<Status> statuses = null;
        try {
            statuses = api.getStatusHistory(id, start, end, 0);
        } catch (UserapiLoginException e) {
            e.printStackTrace();
        }
        List<StatusDao> statusDaos = new LinkedList<StatusDao>();
        if (statuses != null) {
            for (Status status : statuses) {
                boolean personal = true;
                StatusDao statusDao = new StatusDao(status.getStatusId(), status.getUserId(), status.getUserName(), status.getDate(), status.getText(), personal);
                statusDaos.add(statusDao);
            }
        }
        StatusDao.bulkSaveOrUpdate(getApplicationContext(), statusDaos);
    }

    private synchronized void refreshFriends() throws IOException, JSONException, UserapiLoginException {
        Log.v(TAG, "Refreshing friends");
        List<User> users = ApiCheckingKit.getApi().getMyFriends();
        UserDao.synchronizeFriends(this, users, UserDao.SYNC_FRIENDS);
    }

    private synchronized void refreshNewFriends() throws IOException, JSONException, UserapiLoginException {
        Log.v(TAG, "Refreshing new friends");
        List<User> users = ApiCheckingKit.getApi().getMyNewFriends();
        // Userapi returns unsorted list
        Collections.sort(users);
        UserDao.synchronizeFriends(this, users, UserDao.SYNC_NEW_FRIENDS);
    }

    private synchronized void refreshOnlineFriends() throws IOException, JSONException, UserapiLoginException {
        Log.v(TAG, "Refreshing online friends");
        List<User> users = ApiCheckingKit.getApi().getMyFriendsOnline();
        UserDao.synchronizeFriends(this, users, UserDao.SYNC_ONLINE_FRIENDS);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "Service onDestroy");

        // TODO: stop all running threads
        for (Thread t : threads) {
            if (t.isAlive())
                t.interrupt();
        }
        super.onDestroy();
    }

    // ============ RPC stuff ============================ 

    private IVkontakteService.Stub m_binder;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service onBind");

        return m_binder;
    }


}
