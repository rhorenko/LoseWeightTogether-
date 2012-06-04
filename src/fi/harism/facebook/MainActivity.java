package fi.harism.facebook;

import mast.avalons.DbHelper;
import mast.avalons.Provider;
import mast.avalons.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fi.harism.facebook.dao.FBBitmap;
import fi.harism.facebook.dao.FBComment;
import fi.harism.facebook.dao.FBFeed;
import fi.harism.facebook.dao.FBPost;
import fi.harism.facebook.dao.FBUser;
import fi.harism.facebook.net.FBClient;
import fi.harism.facebook.request.RequestUI;
import fi.harism.facebook.util.BitmapUtils;
import fi.harism.facebook.util.FacebookURLSpan;
import fi.harism.facebook.util.StringUtils;
import fi.harism.facebook.view.BitmapSwitcher;
import fi.harism.facebook.view.UserView;

/**
 * Main Activity of this application. Once Activity is launched it starts to
 * fetch default information from currently logged in user in Facebook API.
 * 
 * @author harism
 */
public class MainActivity extends BaseActivity {
	private static final int CONST_NEXT=101;
	private static final String[] mContent = new String[] { DbHelper.WEIGHT};
	//private static final String[] mContent = new String[] {DbHelper.WEIGHT};
	private static final String PROTOCOL_SHOW_PROFILE = "showprofile://";
	// Default picture used as sender's profile picture.
	private Bitmap mDefaultPicture = null;
		// Rounding radius for user picture.
		// TODO: Move this value to resources instead.
	private static final int PICTURE_ROUND_RADIUS = 7;
		// Span onClick observer for profile protocol.
	private SpanClickObserver mSpanClickObserver;
		// Feed path.
	private String mFeedPath="me/feed";
		// Observer for post onClick events.
	private PostClickObserver mPostClickObserver;
	//private Cursor mCursor;
	double delta;
	private FBPost mFBPost = null;
	private Cursor mCursor;
	public String FirstPostId="";
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getGlobalState().getFBClient().authorizeCallback(requestCode,
				resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_feed);
		
		
		mSpanClickObserver = new SpanClickObserver();
		mPostClickObserver = new PostClickObserver();

		final FBFeed fbFeed = getGlobalState().getFBFactory()
				.getFeed(mFeedPath);
		Log.e("MainActivity", ""+"getFeed="+fbFeed);
		
		delta=searchDelta();
		EditText inputWeight = ((EditText) findViewById(R.id.inputWeight));
		inputWeight.setText(Double.toString(delta));
		final Activity self = this;
		
		View shareButton = findViewById(R.id.StatusSubmitButton);
		shareButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showProgressDialog();
				getGlobalState().getRequestQueue().removeRequests(self);
				FBFeedRequest request = new FBFeedRequest(self, fbFeed);
				getGlobalState().getRequestQueue().addRequest(request);
				String postId = FirstPostId;
				String text = ((EditText) findViewById(R.id.StatusEditText)).getText().toString();
				Log.e("MainActivity", ""+"FirstPostId="+FirstPostId);
				Intent i7 = new Intent();
				i7.setClass(self, PostActivity.class);
				i7.putExtra(PostActivity.INTENT_FEED_PATH, mFeedPath);
				i7.putExtra(PostActivity.INTENT_POST_ID, postId);
				i7.putExtra(PostActivity.INTENT_POST_TEXT, text);
				startActivityForResult(i7,CONST_NEXT);
			}
		});
		
		View showWallButton = findViewById(R.id.ShowWallButton);
		showWallButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showProgressDialog();
				getGlobalState().getRequestQueue().removeRequests(self);
				FBFeedRequest request = new FBFeedRequest(self, fbFeed);
				getGlobalState().getRequestQueue().addRequest(request);
				Button shareButton =(Button) findViewById(R.id.StatusSubmitButton);
				shareButton.setVisibility(View.VISIBLE);
			}
		});
		
		View setButton = findViewById(R.id.SetButton);
		setButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				EditText inputWeight = ((EditText) findViewById(R.id.inputWeight));	
				EditText statusEditText = ((EditText) findViewById(R.id.StatusEditText));
				String txt1="Мне удалось похудеть на ";
				String txt2=" кг с помощью программы LoseWeight Together!";
				statusEditText.setText(txt1+inputWeight.getText().toString()+txt2);
				}
		});
		// TODO: Should take care of situation in which user uses 'back' button
		// on login dialog.
		//View userView = findViewById(R.id.activity_main_current_user);
		//userView.setVisibility(View.GONE);

		if (getGlobalState().getFBClient().isAuthorized()) {
			loadUserInfo();
			
		} else {
			LoginObserver observer = new LoginObserver(this);
			getGlobalState().getFBClient().authorize(this, observer);
		};
	}

	private void sendComment() {
		showProgressDialog();
		SendRequest request = new SendRequest(this,
				(EditText) findViewById(R.id.StatusEditText));
		getGlobalState().getRequestQueue().addRequest(request);
	}
	
	public void onDestroy() {
		super.onDestroy();
		getGlobalState().getRequestQueue().removeRequests(this);
	}

	
	public void onPause() {
		super.onPause();
		getGlobalState().getRequestQueue().setPaused(this, true);
	}

	
	public void onResume() {
		super.onResume();
		getGlobalState().getRequestQueue().setPaused(this, false);
	}

	/**
	 * Loads currently logged in user's information.
	 */
	private void loadUserInfo() {
		// Update user information asynchronously if needed.
		FBUser fbUserMe = getGlobalState().getFBFactory().getUser("me");
		if (fbUserMe.getLevel() == FBUser.Level.FULL) {
			updateProfileInfo(fbUserMe);
		} else {
			FBUserRequest meRequest = new FBUserRequest(this, fbUserMe);
			getGlobalState().getRequestQueue().addRequest(meRequest);
		}
	}
	/**
	 * Click listener for our own link protocol. Rest is handled by default
	 * handler.
	 */
	private final class SpanClickObserver implements
			FacebookURLSpan.ClickObserver {
		
		public boolean onClick(FacebookURLSpan span) {
			String url = span.getURL();
			if (url.startsWith(PROTOCOL_SHOW_PROFILE)) {
				//String userId = url.substring(PROTOCOL_SHOW_PROFILE.length());
				//Intent i = createIntent(UserActivity.class);
				//i.putExtra("fi.harism.facebook.UserActivity.user", userId);
				//startActivity(i);
				return true;
			}
			return false;
		}
	}
	/**
	 * Updates user information to screen.
	 */
	private void updateProfileInfo(FBUser fbUserMe) {
		//UserView userView = (UserView) findViewById(R.id.activity_main_current_user);
		//userView.setVisibility(View.GONE);

		//userView.setName(fbUserMe.getName());
		//userView.setContent(fbUserMe.getStatus());

		//FBBitmap fbBitmapMe = getGlobalState().getFBFactory().getBitmap(
			//	fbUserMe.getPicture());
		//i//f (fbBitmapMe.getBitmap() != null) {
			//updateProfilePicture(fbBitmapMe);
		//} else {
			//userView.setPicture(getGlobalState().getDefaultPicture());
			//FBBitmapRequest request = new FBBitmapRequest(this, fbBitmapMe);
			//getGlobalState().getRequestQueue().addRequest(request);
		//}
	}

	/**
	 * Updates user's picture.
	 */
	private void updateProfilePicture(FBBitmap fbBitmapMe) {
		//UserView userView = (UserView) findViewById(R.id.activity_main_current_user);
		//userView.setPicture(fbBitmapMe.getBitmap());
	}

	/**
	 * Class for handling profile picture request.
	 */
	private final class FBBitmapRequest extends RequestUI {

		private FBBitmap fbBitmap;

		public FBBitmapRequest(Activity activity, FBBitmap fbBitmap) {
			super(activity, activity);
			this.fbBitmap = fbBitmap;
		}

		
		public void execute() throws Exception {
			fbBitmap.load();
		}

		
		public void executeUI(Exception ex) {
			if (ex == null) {
				updateProfilePicture(fbBitmap);
			}
		}
	}

	/**
	 * Class for handling "me" request.
	 */
	private final class FBUserRequest extends RequestUI {

		private FBUser mFBUser;

		public FBUserRequest(Activity activity, FBUser fbUser) {
			super(activity, activity);
			mFBUser = fbUser;
		}

		
		public void execute() throws Exception {
			mFBUser.load(FBUser.Level.FULL);
		}

		
		public void executeUI(Exception ex) {
			if (ex == null) {
				updateProfileInfo(mFBUser);
			} else {
				// TODO: This is rather disastrous situation actually.
				showAlertDialog(ex.toString());
			}
		}

	}

	/**
	 * LoginObserver observer for Facebook authentication procedure.
	 */
	private final class LoginObserver implements FBClient.LoginObserver {

		private Activity mActivity;

		public LoginObserver(Activity activity) {
			mActivity = activity;
		}

		
		public void onCancel() {
			// If user cancels login dialog let's simply close app.
			finish();
		}

		
		public void onComplete() {
			// On successful login start loading logged in user's information.
			loadUserInfo();
		}

		
		public void onError(Exception ex) {
			Log.e("MainActivity", ex.toString());
			finish();
			// On error trigger new login dialog.
			// LoginObserver observer = new LoginObserver(mActivity);
			// getGlobalState().getFBClient().authorize(mActivity, observer);
			// If there was an error during authorization show an alert to user.
			// showAlertDialog(ex.getLocalizedMessage());
		}
	}
	/**
	 * Click observer for post items.
	 */
	private final class PostClickObserver implements View.OnClickListener {
		
		public void onClick(View v) {
			//String postId = (String) v.getTag();
			//Intent i = createIntent(PostActivity.class);
			//i.putExtra(PostActivity.INTENT_FEED_PATH, mFeedPath);
			//i.putExtra(PostActivity.INTENT_POST_ID, postId);
			//startActivity(i);
		}

	}
	/**
	 * Request for handling FBFeed loading.
	 */
	private final class FBFeedRequest extends RequestUI {

		private FBFeed mFBFeed;

		public FBFeedRequest(Activity activity, FBFeed fbFeed) {
			super(activity, activity);
			mFBFeed = fbFeed;
		}

		
		public void execute() throws Exception {
			mFBFeed.load();
		}

		
		public void executeUI(Exception ex) {
			if (ex != null) {
				// We don't want to see this happening but just in case.
				showAlertDialog(ex.getLocalizedMessage());
			} else {
				updateFeedView(mFBFeed);
			}
			hideProgressDialog();
		}

	}
	/**
	 * Creates new feed post View.
	 */
	private View createPostView(FBPost post) {
		// Create default Feed Item view.
		View postView = getLayoutInflater().inflate(R.layout.view_post, null);

		// Set sender's name.
		String fromId = post.getFromId();
		String fromName = post.getFromName();
		TextView fromView = (TextView) postView
				.findViewById(R.id.view_post_from);
		StringUtils.setTextLink(fromView, fromName, PROTOCOL_SHOW_PROFILE
				+ fromId, mSpanClickObserver);

		// Get message from feed item. Message is the one user can add as a
		// description to items posted.
		String message = post.getMessage();
		TextView messageView = (TextView) postView
				.findViewById(R.id.view_post_message);
		if (message != null) {
			StringUtils.setTextLinks(messageView, message, null);
		} else {
			messageView.setVisibility(View.GONE);
		}

		// Get name from feed item. Name is shortish description like string
		// for feed item.
		String name = post.getName();
		TextView nameView = (TextView) postView
				.findViewById(R.id.view_post_name);
		if (name != null) {
			if (post.getLink() != null) {
				StringUtils.setTextLink(nameView, name, post.getLink(), null);
			} else {
				nameView.setText(name);
			}
		} else {
			nameView.setVisibility(View.GONE);
		}

		String caption = post.getCaption();
		TextView captionView = (TextView) postView
				.findViewById(R.id.view_post_caption);
		if (caption != null) {
			StringUtils.setTextLinks(captionView, caption, null);
		} else {
			captionView.setVisibility(View.GONE);
		}

		// Get description from feed item. This is longer description for
		// feed item.
		String description = post.getDescription();
		TextView descriptionView = (TextView) postView
				.findViewById(R.id.view_post_description);
		if (description != null) {
			StringUtils.setTextLinks(descriptionView, description, null);
		} else {
			descriptionView.setVisibility(View.GONE);
		}

		// Convert created time to more readable format.
		String createdTime = post.getCreatedTime();
		createdTime = StringUtils.convertFBTime(createdTime);
		TextView detailsView = (TextView) postView
				.findViewById(R.id.view_post_details);
		detailsView.setText(getResources().getString(
				R.string.activity_feed_post_details, createdTime,
				post.getCommentsCount(), post.getLikesCount()));

		return postView;
	}

	/**
	 * Updates list of post views from given FBFeed.
	 */
	private void updateFeedView(FBFeed fbFeed) {
		LinearLayout contentView = (LinearLayout) findViewById(R.id.activity_feed_content);
		contentView.setVisibility(View.GONE);
		contentView.removeAllViews();
		int j=0;
		for (FBPost post : fbFeed.getPosts()) {
			View postView = createPostView(post);
			postView.setTag(post.getId());
			if(j==0){FirstPostId=post.getId();
			Log.e("----------FirstPostId=",FirstPostId );
			};
			Log.e("postId",post.getId() );
			Log.e("FirstPostId=",FirstPostId );
			j++;
			ImageView imageView = (ImageView) postView
					.findViewById(R.id.view_post_picture);
			if (post.getPicture() != null) {
				imageView.setVisibility(View.GONE);
				FBBitmap fbBitmap = getGlobalState().getFBFactory().getBitmap(
						post.getPicture());
				Bitmap bitmap = fbBitmap.getBitmap();
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
					PostPictureRequest request = new PostPictureRequest(this,
							imageView, fbBitmap);
					getGlobalState().getRequestQueue().addRequest(request);
				}
			} else {
				imageView.setVisibility(View.GONE);
			}

			BitmapSwitcher profilePic = (BitmapSwitcher) postView
					.findViewById(R.id.view_post_from_picture);
			FBUser fbUser = getGlobalState().getFBFactory().getUser(
					post.getFromId());
			if (fbUser.getLevel() == FBUser.Level.UNINITIALIZED) {
				profilePic.setBitmap(mDefaultPicture);
				FromPictureRequest request = new FromPictureRequest(this,
						profilePic, fbUser);
				getGlobalState().getRequestQueue().addRequest(request);
			} else {
				FBBitmap fbBitmap = getGlobalState().getFBFactory().getBitmap(
						fbUser.getPicture());
				Bitmap bitmap = fbBitmap.getBitmap();
				if (bitmap != null) {
					profilePic.setBitmap(BitmapUtils.roundBitmap(bitmap,
							PICTURE_ROUND_RADIUS));
				} else {
					profilePic.setBitmap(mDefaultPicture);
					FromPictureRequest request = new FromPictureRequest(this,
							profilePic, fbUser);
					getGlobalState().getRequestQueue().addRequest(request);
				}
			}
			postView.setOnClickListener(mPostClickObserver);
			if (!(j==0)){contentView.addView(postView);};
			
		}
		TextView textForAdvise = (TextView) findViewById(R.id.textForAdvise);
		textForAdvise.setVisibility(View.GONE);
		contentView.setVisibility(View.VISIBLE);
	}
	/**
	 * Private class for handling sender/from picture requests.
	 */
	private final class FromPictureRequest extends RequestUI {

		private BitmapSwitcher mProfilePic;
		private FBUser mFBUser;
		private FBBitmap mFBBitmap;

		public FromPictureRequest(Activity activity, BitmapSwitcher profilePic,
				FBUser fbUser) {
			super(activity, activity);
			mProfilePic = profilePic;
			mFBUser = fbUser;
		}

		
		public void execute() throws Exception {
			mFBUser.load(FBUser.Level.DEFAULT);
			mFBBitmap = getGlobalState().getFBFactory().getBitmap(
					mFBUser.getPicture());
			mFBBitmap.load();
		}

		
		public void executeUI(Exception ex) {
			if (ex == null) {
				Bitmap rounded = BitmapUtils.roundBitmap(mFBBitmap.getBitmap(),
						PICTURE_ROUND_RADIUS);
				mProfilePic.setBitmap(rounded);
			}
		}
	}

	/**
	 * Private class for handling feed post picture requests.
	 */
	private final class PostPictureRequest extends RequestUI {

		private ImageView mImageView;
		private FBBitmap mFBBitmap;

		public PostPictureRequest(Activity activity, ImageView imageView,
				FBBitmap fbBitmap) {
			super(activity, activity);
			mImageView = imageView;
			mFBBitmap = fbBitmap;
		}

		
		public void execute() throws Exception {
			mFBBitmap.load();
		}

		
		public void executeUI(Exception ex) {
			if (ex == null) {
				mImageView.setImageBitmap(mFBBitmap.getBitmap());
				// TODO: Image size is (0, 0) and animation never takes place.
				Rect r = new Rect();
				if (mImageView.getLocalVisibleRect(r)) {
					AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
					inAnimation.setDuration(700);
					mImageView.startAnimation(inAnimation);
				}
			}
		}
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
