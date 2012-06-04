package org.googlecode.vkontakte_android;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;
import org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper;
import org.googlecode.vkontakte_android.provider.UserapiProvider;
import org.googlecode.vkontakte_android.service.CheckingService;
import org.googlecode.vkontakte_android.utils.AppHelper;
import org.googlecode.vkontakte_android.utils.ServiceHelper;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_MESSAGE_RECEIVERID;
import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_MESSAGE_SENDERID;

public class ComposeMessageActivity extends ListActivity implements AbsListView.OnScrollListener {

    public static final String TAG = "VK:ComposeMessageActivity";

    private MessagesListAdapter adapter;

    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        userId = getIntent().getExtras().getLong(UserapiDatabaseHelper.KEY_MESSAGE_SENDERID,
                getIntent().getExtras().getLong(UserapiDatabaseHelper.KEY_USER_ID, -1));
        if (userId == -1) {
            userId = Long.parseLong(getIntent().getData().getLastPathSegment()); // toDo new
        }

        setContentView(R.layout.messages_compose);
        adapter = new MessagesListAdapter(this, R.layout.message_thread_row, managedQuery(UserapiProvider.MESSAGES_URI, null, KEY_MESSAGE_SENDERID + "=?" + " OR " + KEY_MESSAGE_RECEIVERID + "=?", new String[]{String.valueOf(userId), String.valueOf(userId)}, UserapiDatabaseHelper.KEY_MESSAGE_DATE + " ASC"));
        setListAdapter(adapter);
        getListView().setStackFromBottom(true);
        getListView().setOnScrollListener(this);

        findViewById(R.id.send_reply).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        final TextView textView = (TextView) findViewById(R.id.mess_to_send);
        final String message = textView.getText().toString();

        if (message.trim().length() == 0)
            return;

        new AsyncTask<Object, Object, Object>(){
        	@Override
            protected void onPreExecute() {
                super.onPreExecute();
                setProgressBarIndeterminateVisibility(true);
                textView.setText("");
                textView.setEnabled(false);
            }
            @Override
            protected void onPostExecute(Object result) {
                setProgressBarIndeterminateVisibility(false);
                textView.setEnabled(true);
            }
    		@Override
    		protected Object doInBackground(Object... params) {
                try {
                    ServiceHelper.getService().sendMessage(message, userId);
                } catch (RemoteException e) {
                    Log.w("Error while sending message", e);
                    textView.setText(message);
                }
                return null;
    		}
        }.execute();
    }

    public void onScroll(AbsListView v, int i, int j, int k) {
    }

    public void onScrollStateChanged(AbsListView v, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && getListView().getLastVisiblePosition() == adapter.getCount() - 1) {
//            adapter.prepareData();
        }
    }

}