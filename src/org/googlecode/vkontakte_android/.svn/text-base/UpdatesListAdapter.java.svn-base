package org.googlecode.vkontakte_android;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import org.googlecode.vkontakte_android.database.StatusDao;
import org.googlecode.vkontakte_android.utils.AvatarLoader;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;

import java.text.SimpleDateFormat;

public class UpdatesListAdapter extends ResourceCursorAdapter implements OnScrollListener {
    private static final String TAG = "VK:UpdatesListAdapter";

    public static final  SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm ");//todo: get rid of extra space by using padding(?)
    public static final SimpleDateFormat weektimeFormat = new SimpleDateFormat("EEE, HH:mm ");

    private int scrollState;
    private AvatarLoader avatarLoader;
    
    public UpdatesListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
        avatarLoader = new AvatarLoader(context);
        scrollState = SCROLL_STATE_IDLE;
    }
    
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        StatusDao status = new StatusDao(cursor);
        TextView nameLine = (TextView) view.findViewById(R.id.name);
        nameLine.setText(status.getUserName());
        TextView statusLine = (TextView) view.findViewById(R.id.status);
        statusLine.setText(Html.fromHtml(status.getText()));
        TextView timeLine = (TextView) view.findViewById(R.id.time);
        timeLine.setText(weektimeFormat.format(status.getDate()));

        ImageView avatarView = (ImageView) view.findViewById(R.id.photo);
        if (avatarView != null) {
            AvatarLoader.AvatarInfo info = new AvatarLoader.AvatarInfo();
            info.view = avatarView;
            info.userId = status.getUserId();
            info.type = AvatarLoader.AvatarInfo.AvatarType.UPDATES;
            avatarView.setTag(info.avatarUrl);


            if (PreferenceHelper.shouldLoadPics(context)) {
                avatarLoader.applyAvatarDeferred(info);
                if (scrollState != OnScrollListener.SCROLL_STATE_FLING)
                    // Scrolling is idle or slow, getting the avatar right now
                    avatarLoader.loadMissedAvatars();
            } else {
                avatarView.setImageBitmap(CImagesManager.getBitmap(context, CImagesManager.Icons.STUB));
                avatarView.setVisibility(View.GONE);
            }
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