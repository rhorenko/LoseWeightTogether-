package org.googlecode.vkontakte_android;


import org.googlecode.vkontakte_android.service.CheckingService;
import org.googlecode.vkontakte_android.utils.ServiceHelper;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;

public class FriendListActivity extends TabActivity {
    public static final int ALL = 1;
    public static final int ONLINE = ALL + 1;
    public static final int REQUESTS = ONLINE + 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.friends_view);

        final TabHost tabHost = getTabHost();
        Intent allFriends = new Intent(this, FriendsListTabActivity.class);
        allFriends.putExtra("type", ALL);
        Intent requestFriends = new Intent(this, FriendsListTabActivity.class);
        requestFriends.putExtra("type", REQUESTS);
        Intent onlineFriends = new Intent(this, FriendsListTabActivity.class);
        onlineFriends.putExtra("type", ONLINE);

        tabHost.addTab(tabHost.newTabSpec("All").setIndicator(
                "All").setContent(
                allFriends));

        tabHost.addTab(tabHost.newTabSpec("Online").setIndicator(
        		"Online").setContent(
        		onlineFriends));
        
        tabHost.addTab(tabHost.newTabSpec("Requests").setIndicator(
                "Requests").setContent(
                requestFriends));


        int type= ONLINE;
        
        if (getIntent().getExtras()!=null){
        	type=getIntent().getExtras().getInt("type");
        }
        
        tabHost.setCurrentTab(type-1);
        refreshOnStart();
    }
    
    private void refreshOnStart() {
        new AsyncTask<Object, Object, Object>(){
        	
        	@Override
            protected void onPreExecute() {
                super.onPreExecute();
                setProgressBarIndeterminateVisibility(true);
            }
            @Override
            protected void onPostExecute(Object result) {
                setProgressBarIndeterminateVisibility(false);
            }
    		@Override
    		protected Object doInBackground(Object... params) {
    			try {
    				ServiceHelper.getService().update(CheckingService.ContentToUpdate.FRIENDS.ordinal(), true);
    			} catch (RemoteException e) {
    				e.printStackTrace();
    			}
    			return null;
    		}
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friends_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
				refreshOnStart();
            	return true;
            	
            default:
                return super.onContextItemSelected(item);
        }
    }    
}