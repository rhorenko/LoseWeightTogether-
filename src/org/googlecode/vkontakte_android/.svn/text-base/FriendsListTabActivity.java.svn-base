package org.googlecode.vkontakte_android;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.utils.UserHelper;


import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.*;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.USERS_URI;

public class FriendsListTabActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private FriendsListAdapter adapter;

    @SuppressWarnings("unused")
    private static String TAG = "VK:FriendsListTabActivity";

    enum FriendsCursorType {
        ALL, NEW, ONLINE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

        Cursor cursor;
        int type = FriendListActivity.ONLINE;

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
        }

        if (type == FriendListActivity.ALL) {
            cursor = makeCursor(FriendsCursorType.ALL);
        }
        else if (type == FriendListActivity.ONLINE) {
            cursor = makeCursor(FriendsCursorType.ONLINE);
        }
        else if (type == FriendListActivity.REQUESTS) {
            cursor = makeCursor(FriendsCursorType.NEW);
        }
        else {
            cursor = makeCursor(FriendsCursorType.ONLINE);
        }

        adapter = new FriendsListAdapter(this, R.layout.friend_row, cursor);

        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);
        getListView().setAdapter(adapter);
        getListView().setOnScrollListener(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.resumeAvatarLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.pauseAvatarLoading();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.cancelAvatarLoading();
    }

    private Cursor makeCursor(FriendsCursorType type) {

        switch (type) {
            case NEW:
                return managedQuery(USERS_URI, null, KEY_USER_NEW_FRIEND + "=1", null,
                                    KEY_USER_ID + " ASC," + KEY_USER_NEW_FRIEND + " DESC, " + KEY_USER_ONLINE + " DESC"

                );
            case ONLINE:
                return managedQuery(USERS_URI, null, KEY_USER_ONLINE + "=1", null,
                                    KEY_USER_ID + " ASC," + KEY_USER_NEW_FRIEND + " DESC, " + KEY_USER_ONLINE + " DESC"
                );
            case ALL:
                return managedQuery(USERS_URI, null,
                                    KEY_USER_IS_FRIEND + "=?", new String[]{"1"},
                                    KEY_USER_ID + " ASC," + KEY_USER_NEW_FRIEND + " DESC, " + KEY_USER_ONLINE + " DESC"
                );
            default:
                return managedQuery(USERS_URI, null, null, null, null);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friend_context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        UserDao user = UserDao.get(this, info.id);
        if (user.isNewFriend()) {
            menu.removeItem(R.id.remove_from_friends);
        }
        else {
            menu.removeItem(R.id.add_to_friends);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long rowId = info.id;
        UserDao user = UserDao.get(this, rowId);
        long userId = user.getUserId();
        switch (item.getItemId()) {
            case R.id.view_profile:
                UserHelper.viewProfile(this, userId);
                return true;
            case R.id.remove_from_friends:
                //todo
                return true;
            case R.id.send_message:
                UserHelper.sendMessage(this, userId);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long rowId) {
        UserDao user = UserDao.get(this, rowId);
        UserHelper.viewProfile(this, user.getUserId());
    }
}
