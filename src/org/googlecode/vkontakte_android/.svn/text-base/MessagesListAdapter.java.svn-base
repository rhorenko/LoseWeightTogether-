package org.googlecode.vkontakte_android;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import org.googlecode.vkontakte_android.database.MessageDao;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.provider.UserapiProvider;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;
import org.googlecode.vkontakte_android.utils.TextFormatHelper;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_USER_ID;


public class MessagesListAdapter extends ResourceCursorAdapter {
    private static final String TAG = "MessagesListAdapter";
    private int readColor;
    private int unreadColor;

    public MessagesListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
        readColor = context.getResources().getColor(R.color.msg_read_bkg);
        unreadColor = context.getResources().getColor(R.color.msg_unread_bkg);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MessageDao messageDao = new MessageDao(cursor);

        String strName;
        String strDate;

        Long senderid = messageDao.getSenderId();
        Long receiverid = messageDao.getReceiverId();
        Long receivedate = messageDao.date;

		if (view.findViewById(R.id.name) != null) {
			if (receiverid.equals(PreferenceHelper.getMyId(context))) {
				strName = getNameById(context, senderid);
			} else {
				strName = getNameById(context, receiverid);
			}

			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(strName);
			strDate = DateFormat.getMediumDateFormat(context).format( receivedate);
			
		} else {
			TextView name = (TextView) view.findViewById(R.id.sendername);
			strName = getNameById(context, senderid);
			name.setText(strName);
			strDate = DateFormat.getMediumDateFormat(context).format( receivedate);
			strDate +=" ("+DateFormat.getTimeFormat(context).format(receivedate)+")";
		}        	

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(strDate);

        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(Html.fromHtml(messageDao.text));

        View bkg = view.findViewById(R.id.bkg);
        if (messageDao.read) {
            bkg.setBackgroundColor(readColor);
        }
        else {
            bkg.setBackgroundColor(unreadColor);
        }
    }

    private String getNameById(Context context, Long userid) {
        String username = userid.toString();
        if (userid.equals(PreferenceHelper.getMyId(context))) {
            return context.getString(R.string.send_by_me);
        }

        Cursor sc = context.getContentResolver().query(
                UserapiProvider.USERS_URI, null, KEY_USER_ID + "=?",
                new String[]{userid.toString()}, null);
        if (sc.moveToNext()) {
            UserDao ud = UserDao.make(context, sc);
            if (ud.getUserName() != null) { username = ud.getUserName(); }
        } else {
            Log.e(TAG, "No such user in DB ");
            username = userid.toString();
        }
        sc.close();
        return username;
    }
}