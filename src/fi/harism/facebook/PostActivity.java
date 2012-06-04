package fi.harism.facebook;

import java.util.Vector;

import mast.avalons.DbHelper;
import mast.avalons.Provider;
import mast.avalons.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fi.harism.facebook.dao.FBComment;
import fi.harism.facebook.dao.FBFeed;
import fi.harism.facebook.dao.FBPost;
import fi.harism.facebook.request.RequestUI;
import fi.harism.facebook.util.StringUtils;

public class PostActivity extends BaseActivity {
	private static final String[] mContent = new String[] { DbHelper.WEIGHT};
	public static final String INTENT_FEED_PATH = "fi.harism.facebook.PostActivity.feedPath";
	public static final String INTENT_POST_ID = "fi.harism.facebook.PostActivity.postId";
	public static final String INTENT_POST_TEXT = "fi.harism.facebook.PostActivity.text";
	private static final int CONST_NEXT2=102;
	double delta;
	String sentMessage;
	private FBPost mFBPost = null;
	private Cursor mCursor;
	private final int IDD_DIALOG = 0;
	String text;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post);
		//showDialog(IDD_DIALOG);
		delta=searchDelta();
		final Activity self1 = this;
		String feedPath = getIntent().getStringExtra(INTENT_FEED_PATH);
		String postId = getIntent().getStringExtra(INTENT_POST_ID);
		String text = getIntent().getStringExtra(INTENT_POST_TEXT);
		EditText inputWeight = ((EditText) findViewById(R.id.inputWeight1));
		EditText statusEditText = ((EditText) findViewById(R.id.StatusEditText1));
		inputWeight.setText(Double.toString(delta));
		statusEditText.setText(text);
		FBFeed fbFeed = getGlobalState().getFBFactory().getFeed(feedPath);
		Log.e("PostActivity", ""+"getFeed="+fbFeed);
		Vector<FBPost> posts = fbFeed.getPosts();
		//Log.e("Start", ""+feedPath);
		//Log.e("Start", ""+fbFeed.getPosts());
		//Log.e("PostActivity", "Firstelement=="+posts.firstElement() );
		//if(postId==null){postId=posts.firstElement().getId();};
		for (FBPost post : posts) {
			if (post.getId().equals(postId)) {
				mFBPost = post;
				break;
			}
		}
		if (mFBPost == null) {
			finish();
			return;
		}
		sendComment(this);
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, 
        		"Shared:\n"+((EditText)findViewById(R.id.StatusEditText1)).getText().toString(), Toast.LENGTH_LONG);
       toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
		Intent i8 = new Intent();
		i8.setClass(self1, MainActivity.class);
		startActivity(i8);
		finish();  
		Button sendButton = (Button) findViewById(R.id.StatusSubmitButton1);
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				sendComment(self1);
				Context context = getApplicationContext();
				Toast toast = Toast.makeText(context, 
		        		"Shared:\n"+((EditText)findViewById(R.id.StatusEditText1)).getText().toString(), Toast.LENGTH_LONG);
		       toast.setGravity(Gravity.BOTTOM, 0, 0);
		        toast.show();
				Intent i8 = new Intent();
				i8.setClass(self1, PostActivity.class);
				startActivity(i8);
		       
			}
		});

		updateComments();
		
	}
	
	private void updateComments() {
		LinearLayout container = (LinearLayout) findViewById(R.id.activity_post_content);
		container.removeAllViews();

		for (FBComment comment : mFBPost.getComments()) {
			View commentView = getLayoutInflater().inflate(
					R.layout.view_comment, null);

			TextView fromView = (TextView) commentView
					.findViewById(R.id.view_comment_from);
			fromView.setText(comment.getFromName());

			TextView messageView = (TextView) commentView
					.findViewById(R.id.view_comment_message);
			messageView.setText(comment.getMessage());

			TextView detailsView = (TextView) commentView
					.findViewById(R.id.view_comment_details);
			detailsView.setText(StringUtils.convertFBTime(comment
					.getCreatedTime()));

			container.addView(commentView);
		}
	}

	private void sendComment(Activity self1) {
		showProgressDialog();
		SendRequest request = new SendRequest(this,
				(EditText) findViewById(R.id.StatusEditText1));
		getGlobalState().getRequestQueue().addRequest(request);
		setResult(RESULT_CANCELED);
        self1.finish();
	}
	private void sendComment() {
		showProgressDialog();
		SendRequest request = new SendRequest(this,
				(EditText) findViewById(R.id.StatusEditText1));
		getGlobalState().getRequestQueue().addRequest(request);
	}

	private class SendRequest extends RequestUI {

		private EditText mEditText;
		private String mMessage;

		public SendRequest(Activity activity, EditText editText) {
			super(activity, activity);
			mEditText = editText;
			mMessage = mEditText.getText().toString().trim();
		}

		
		public void execute() throws Exception {
			if (mMessage.length() != 0) {
				mFBPost.sendComment(mMessage);
				mMessage = "";
				mFBPost.update();
			}
		}

		
		public void executeUI(Exception ex) {
			if (ex == null) {
				mEditText.setText(mMessage);
				updateComments();
			} else {
				showAlertDialog(ex.toString());
			}
			hideProgressDialog();
		}

	}

	private double searchDelta() {
		Log.d("----", "START");
		float minWeight=0;
		float maxWeight=0;
		
		mCursor = managedQuery(Provider.CONTENT_URI, mContent, null, null,"_ID DESC");
		float [] ArrayWeightInt =  new float[mCursor.getCount()];
		//Log.d("----", "AFTER DB="+Integer.toString(mCursor.getCount()));
		for (int i1 = 0; i1 < mCursor.getCount(); i1++) {
			mCursor.moveToPosition(i1);
			ArrayWeightInt[i1]=Float.parseFloat(mCursor.getString(0));
			//Log.d("----", "--"+Integer.toString(i1)+"--"+mCursor.getString(0)); 
		}
		//Log.d("----", "CENTER");       
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
	      return (double) Math.floor((maxWeight-minWeight)*1000.0D)/1000.0D;
	};
}
