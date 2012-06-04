package org.googlecode.vkontakte_android;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import org.googlecode.vkontakte_android.CImagesManager.Icons;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.utils.AvatarLoader;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;


public class FriendsListAdapter extends ResourceCursorAdapter implements OnScrollListener {

    private static final String TAG = "VK:FriendsListAdapter";
    
    private String ONLINE_STATUS;
    private String OFFLINE_STATUS;

    private int scrollState;
    private AvatarLoader avatarLoader;

    public FriendsListAdapter(Context context, int layout, Cursor cursor) {
    	super(context, layout, cursor);
    	ONLINE_STATUS = context.getResources().getString(R.string.status_online);
    	OFFLINE_STATUS = context.getResources().getString(R.string.status_offline);

        avatarLoader = new AvatarLoader(context);
    }
    
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
    	UserDao userDao = UserDao.make(context, cursor);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView status = (TextView) view.findViewById(R.id.status);
        name.setText(userDao.getUserName());
        status.setText(userDao.isOnline() ? ONLINE_STATUS : OFFLINE_STATUS);

        ImageView avatarView = (ImageView) view.findViewById(R.id.photo);
        AvatarLoader.AvatarInfo info = new AvatarLoader.AvatarInfo();
        info.view = avatarView;
        info.avatarUrl = userDao.getUserPhotoUrl();
        info.type = AvatarLoader.AvatarInfo.AvatarType.FRIENDS;
        avatarView.setTag(info.avatarUrl);

        if (PreferenceHelper.shouldLoadPics(context)) {
            avatarLoader.applyAvatarDeferred(info);
            if (scrollState != OnScrollListener.SCROLL_STATE_FLING)
                // Scrolling is idle or slow, getting the avatar right now
                avatarLoader.loadMissedAvatars();
        }
        else {
            avatarView.setImageBitmap(CImagesManager.getBitmap(context, Icons.STUB));
            avatarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
            // If we are in a fling, stop loading avatars.
            avatarLoader.cancelLoading();
        }
        else
            avatarLoader.loadMissedAvatars();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Nothing to do
    }

    public void resumeAvatarLoading() {
        avatarLoader.loadMissedAvatars();
    }

    public void pauseAvatarLoading() {
        avatarLoader.cancelLoading();
        scrollState = SCROLL_STATE_IDLE;
    }

    public void cancelAvatarLoading() {
        avatarLoader.abortProcess();
        scrollState = SCROLL_STATE_IDLE;
    }

}