package org.googlecode.vkontakte_android;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * ListActivity that can load more records when user scrolls down the main ListView.
 * Setup it with setupLoader() function.
 *
 * @author bea
 */
public class AutoLoadActivity extends ListActivity implements AbsListView.OnScrollListener {

    /**
     * Loading is performed when corresponding action is set
     */
    public int ACTION_FLAGS = 3; //default ACTION_ON_SCROLL_END | ACTION_ON_KEY

    public static int ACTION_ON_SCROLL_END = 1;
    public static int ACTION_ON_KEY = 2;
    public static int ACTION_ON_SCROLL = 4;
    public static int ACTION_ON_START = 8;

    private static String TAG = "VK:AutoLoadActivity";
    protected ListAdapter m_adapter;
    private Loader m_loader;

    boolean m_doLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((ACTION_FLAGS & AutoLoadActivity.ACTION_ON_START) != 0) {
            loadMore();
        }
    }

    protected void changeAdapter(ListAdapter ad) {
    	m_adapter=ad;
    	setListAdapter(m_adapter);
    }
    
    /**
     * Call it only after child performs setContentView()
     *
     * @param l  - callback to be performed when needed.
     * @param ad - adapter to load more data from it.
     */
    public void setupLoader(Loader l, ListAdapter ad) {
        m_loader = l;
        m_adapter = ad;
        setListAdapter(m_adapter);
        getListView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN
                        && getListView().getSelectedItemPosition() == m_adapter.getCount() - 1) {
                    //to prevent multiple loading
                    if (m_doLoad && ((ACTION_FLAGS & ACTION_ON_KEY) != 0)) {
                        loadMore();
                    }
                }
                return false;
            }
        });

        getListView().setOnScrollListener(this);
    }

    private void loadMore() {
        if (m_loader == null) {
            Log.e(TAG, "Callback undefined. Use setupLoader() at first");
            return;
        }
        new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setProgressBarIndeterminateVisibility(true);
                m_doLoad = false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                setProgressBarIndeterminateVisibility(false);
                m_doLoad = true;
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                Log.d(TAG, "loading more info...");
                return m_loader.load();
            }
        }.execute();
    }

    //TODO make template

    public abstract interface Loader {
        Boolean load();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if ((ACTION_FLAGS & ACTION_ON_SCROLL) != 0 && m_doLoad) {
            loadMore();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && getListView().getLastVisiblePosition() == m_adapter.getCount() - 1) {
            // to prevent multiple loading
            if ((ACTION_FLAGS & ACTION_ON_SCROLL_END) != 0 && m_doLoad) {
                loadMore();
			}
		}
	}
}
