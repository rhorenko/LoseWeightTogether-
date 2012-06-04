package org.googlecode.vkontakte_android;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.ListView;

public class AutoLoadList extends ListView {

    private Loader mLoader;

    public AutoLoadList(Context context) {
        super(context);
    }

    public void setLoader(Loader loader) {
        mLoader = loader;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                && event.getAction() == KeyEvent.ACTION_DOWN
                && getSelectedItemPosition() == getAdapter().getCount() - 2) {
            loadMore();
        }
        return super.onKeyDown(keyCode, event);
    }

/*	
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		
		if (getLastVisiblePosition() == getAdapter().getCount() - 1) {
			loadMore();
		}
	}
	*/

    /*
     public void onScrollStateChanged(AbsListView v, int state) {

         if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                 && getLastVisiblePosition() == getAdapter().getCount() - 1) {
             loadMore();
         }
     }
     */

    private void loadMore() {
        if (mLoader != null) {
            new AsyncTask<Object, Object, Boolean>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ((Activity) getContext()).setProgressBarIndeterminateVisibility(true);
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    ((Activity) getContext()).setProgressBarIndeterminateVisibility(false);
                }

                @Override
                protected Boolean doInBackground(Object... params) {
                    return mLoader.load();
                }
            }.execute();
        }
    }

    public abstract interface Loader {
        Boolean load();
    }

}
