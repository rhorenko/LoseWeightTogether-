package org.googlecode.vkontakte_android;

import mast.avalons.DbHelper;
import mast.avalons.Provider;
import mast.avalons.R;

import org.googlecode.vkontakte_android.utils.AppHelper;
import org.googlecode.vkontakte_android.utils.ServiceHelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.nullwire.trace.ExceptionHandler;

public class HomeGridActivity extends Activity {
	float delta;
	private static final String[] mContent = new String[] { DbHelper.WEIGHT};
	private Cursor mCursor; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCursor = managedQuery(
                Provider.CONTENT_URI, mContent, null, null,"_ID DESC");
        //register remote-stacktrace handler
        Handler handler = new Handler();
        //ExceptionHandler.register(this, handler);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.masthomegrid);
        initStatus();
        delta=searchDelta();
        EditText edWeight = ((EditText) findViewById(R.id.inputWeight));
        edWeight.setText(Float.toString(delta));
    };
    private float searchDelta() {
		Log.d("----", "START");
		float minWeight=0;
		float maxWeight=0;
		float []	ArrayWeightInt =  new float[mCursor.getCount()];
		Log.d("----", "AFTER DB="+Integer.toString(mCursor.getCount()));
		for (int i1 = 0; i1 < mCursor.getCount(); i1++) {
			mCursor.moveToPosition(i1);
			ArrayWeightInt[i1]=Float.parseFloat(mCursor.getString(0));
			Log.d("----", "--"+Integer.toString(i1)+"--"+mCursor.getString(0)); 
		}
		Log.d("----", "CENTER");       
		float min,max;
		int i;
	    for (i=0; i<ArrayWeightInt.length; i++);
	    min = ArrayWeightInt[0];
	    max = ArrayWeightInt[0];
	    for (i=1; i<ArrayWeightInt.length; i++) {
	        if (ArrayWeightInt[i] < min)
	            min = ArrayWeightInt[i]; 
	        if (ArrayWeightInt[i] > max)
		        max = ArrayWeightInt[i];
	            };    
	      minWeight=min;   
	      maxWeight=max;
	      Log.d("----", "END");   
	      Log.d("----", "min="+Float.toString(minWeight)); 
	      Log.d("----", "max="+Float.toString(maxWeight));    
	      return (float) maxWeight-minWeight;
	};
	
    private void initStatus() {
        final EditText statusEdit = (EditText) findViewById(R.id.StatusEditText);
        statusEdit.setInputType(InputType.TYPE_NULL);

        statusEdit.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                statusEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                statusEdit.onTouchEvent(event);
                return true;
            }
        });

        findViewById(R.id.StatusSubmitButton).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String statusText = ((EditText) findViewById(R.id.StatusEditText)).getText().toString();
                new AsyncTask<String, Object, Boolean>() {

                    String m_status = "";

                    @Override
                    protected void onPostExecute(Boolean result) {
                    	EditText et = ((EditText) findViewById(R.id.StatusEditText));
                        Toast.makeText(et.getContext(), "\"" + et.getText().toString() + "\" Shared!", Toast.LENGTH_SHORT).show();
                        et.setText(result ? m_status : "");
                    }

                    @Override
                    protected Boolean doInBackground(String... params) {
                        try {
                            m_status = params[0];
                            ServiceHelper.getService().sendStatus(m_status);
                            return true;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            AppHelper.showFatalError(HomeGridActivity.this, "Error while launching the application");
                        }
                        return false;
                    }
                }.execute(new String[]{statusText});
            }
        });

    }

    public void myClickHandler(View view) {
    	EditText et = ((EditText) findViewById(R.id.StatusEditText));
    	switch (view.getId()) {
	case R.id.button1:
		 	et.setText(R.string.textForStatus0);
		 break;
	
	case R.id.button6:
		delta=searchDelta();
		String txt1="Мой вес снижен на ";
		String txt2=" кг с помощью программы LoseWeight Together!";
		EditText edWeight = ((EditText) findViewById(R.id.inputWeight));
		//Button setTextButton = ((Button) findViewById(R.id.button6));
		//setTextButton.setText(txt1+edWeight.toString()+txt2);
		edWeight.setText(Float.toString(delta));
		String txt="";
		txt=(String)edWeight.getText().toString();
		et.setText(txt1+txt+txt2);			
	 break;
		}
    }
    

    
}
