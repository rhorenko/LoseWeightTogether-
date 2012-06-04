package org.googlecode.vkontakte_android.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.TabHost;
import android.widget.TextView;
import org.googlecode.vkontakte_android.*;

import java.lang.reflect.Method;


public class MessageTabActivity extends TabActivity {
    private static final String TAG = "org.googlecode.vkontakte_android.ui.MessageTabActivity";

    public static final int INCOMING_MESSSAGES_TAB = 1;
    public static final int OUTGOING_MESSSAGES_TAB = 2;
    private static final String INCOMING_TAB_ID = "incoming";
    private static final String OUTGOING_TAB_ID = "outgoing";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.message_view);

        final TabHost tabHost = getTabHost();
        boolean isOldSdk = Build.VERSION.SDK.equals("3");

        TabHost.TabSpec incomingSpec = tabHost.newTabSpec(INCOMING_TAB_ID);
        Intent incomingIntent = new Intent(this, MessagesListActivity.class);
        incomingIntent.putExtra("type", INCOMING_MESSSAGES_TAB);
        incomingSpec.setIndicator(INCOMING_TAB_ID).setContent(incomingIntent);

        TabHost.TabSpec outgoingSpec = tabHost.newTabSpec(OUTGOING_TAB_ID);
        final Intent outgoingIntent = new Intent(this, MessagesListActivity.class);
        outgoingIntent.putExtra("type", OUTGOING_MESSSAGES_TAB);
        outgoingSpec.setContent(outgoingIntent);


        if (isOldSdk) {
            incomingSpec.setIndicator(getString(R.string.inbox));
            outgoingSpec.setIndicator(getString(R.string.sent));
        } else {
            LayoutInflater li = LayoutInflater.from(this);

            final TextView incomingTab = (TextView) li.inflate(R.layout.tab_textview, null);
            incomingTab.setText(R.string.inbox);

            final TextView outgoingTab = (TextView) li.inflate(R.layout.tab_textview, null);
            outgoingTab.setText(R.string.sent);

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {
                    if (s.equals(OUTGOING_TAB_ID)) {
                        outgoingTab.setTypeface(Typeface.DEFAULT_BOLD);
                        incomingTab.setTypeface(Typeface.DEFAULT);
                    } else {
                        outgoingTab.setTypeface(Typeface.DEFAULT);
                        incomingTab.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            });

            try {
                Method method = TabHost.TabSpec.class.getMethod("setIndicator", View.class);
                method.invoke(incomingSpec, incomingTab);
                method.invoke(outgoingSpec, outgoingTab);
            } catch (Exception ignore) {
            }


        }

        tabHost.addTab(incomingSpec);
        tabHost.addTab(outgoingSpec);
    }
}