package mast.avalons;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MastActivity extends Activity  {
	private final int IDD_DIALOG = 0;
	private final int IDD_DIALOG1 = 1;
    private static final int IDM_ADD = 101;
    private static final int IDM_EDIT_HISTORY = 102;
    private static final int IDM_SHARE_FRIENDS = 103;
    private static final int IDM_EXIT = 105;
    private static final String TAG = "LoseWeightTogether";
    public String date;
    private Cursor mCursor; 
    private static final String[] mContent = new String[] {
        DbHelper._ID, DbHelper.DATE,
        DbHelper.WEIGHT};   
    Animation animation = null;
    private static String fst_msg;
    private static String scd_msg;
    @Override
    public void onCreate(Bundle icicle) {   	
        super.onCreate(icicle);        
        setContentView(R.layout.mainmast);
        String MY_BANNER_UNIT_ID = "";//a14f59eb4b1f0d1
        LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
        AdView adView = new AdView(this, AdSize.BANNER, MY_BANNER_UNIT_ID);
        layout.addView(adView);
        AdRequest request = new AdRequest();      
        request.setTesting(true);
        adView.loadAd(request);  
        fst_msg = ""+getResources().getText(R.string.fst_msg1)+
        		getResources().getText(R.string.fst_msg2)+
        		getResources().getText(R.string.fst_msg3)+
        		getResources().getText(R.string.fst_msg4)+
        		getResources().getText(R.string.fst_msg5)+
        		getResources().getText(R.string.fst_msg6)+
        		getResources().getText(R.string.fst_msg7);
        scd_msg= ""+getResources().getText(R.string.scd_msg1)+
        		getResources().getText(R.string.scd_msg2)+
        		getResources().getText(R.string.scd_msg3)+
        		getResources().getText(R.string.scd_msg4)+
        		getResources().getText(R.string.scd_msg5)+
        		getResources().getText(R.string.scd_msg6)+
        		getResources().getText(R.string.scd_msg7)+
        		getResources().getText(R.string.scd_msg8)+
        		getResources().getText(R.string.scd_msg9);     
       //showDialog(IDD_DIALOG1);  
       //showDialog(IDD_DIALOG);    
       mCursor = managedQuery(Provider.CONTENT_URI, mContent, null, null,"_ID DESC");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_ADD, Menu.NONE, R.string.menu_add)
            .setIcon(R.drawable.ic_menu_add)
            .setAlphabeticShortcut('s');
        menu.add(Menu.NONE, IDM_EDIT_HISTORY, Menu.NONE, R.string.menu_edit_hist)
        	.setIcon(R.drawable.ic_menu_refresh)
        	.setAlphabeticShortcut('o');
        menu.add(Menu.NONE, IDM_SHARE_FRIENDS, Menu.NONE, R.string.menu_share_friends)
            .setIcon(R.drawable.ic_menu_friendslist)
            .setAlphabeticShortcut('x');
        menu.add(Menu.NONE, IDM_EXIT, Menu.NONE, R.string.menu_exit)
    		.setIcon(R.drawable.ic_menu_exit)
    		.setAlphabeticShortcut('g');
        return(super.onCreateOptionsMenu(menu));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case IDM_ADD:
        		String[] months = getResources().getStringArray(R.array.month);
        		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
        		final String date = Integer.toString(calendar.get(Calendar.YEAR))+"  "+
         			  months[calendar.get(Calendar.MONTH)]+"  "+
         			  Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))+"  "+
         		      Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)+4)+" : "+
         			  Integer.toString(calendar.get(Calendar.MINUTE))+" : "+
         			  Integer.toString(calendar.get(Calendar.SECOND))+"  ";
        		CallAddDialog(date);
            	break;
        	case IDM_EDIT_HISTORY:
                Intent intent = new Intent();
        		intent.setClass(this, HistoryActivity.class);
        		startActivity(intent);
                break;
            case IDM_SHARE_FRIENDS:
            	Intent intent4 = new Intent();
       	 	    intent4.setClass(this, ShareActivity.class);
       		    startActivityForResult(intent4,R.id.imageButton4);	    		    
                break;                        
            
            case IDM_EXIT:         	
                finish();
                break;
            default:
                return false;
        }
        return true;
    }
    public void myClickHandler(View view) {
    	switch (view.getId()) {
	case R.id.imageButton1:
		String[] months = getResources().getStringArray(R.array.month);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
		final String date = Integer.toString(calendar.get(Calendar.YEAR))+"  "+
 			  months[calendar.get(Calendar.MONTH)]+"  "+
 			  Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))+"  "+
 		      Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)+4)+" : "+
 			  Integer.toString(calendar.get(Calendar.MINUTE))+" : "+
 			  Integer.toString(calendar.get(Calendar.SECOND))+"  ";
		CallAddDialog(date);
		 break;
			
    case R.id.imageButton2:
    	 Intent intent = new Intent();
		 intent.setClass(this, HistoryActivity.class);
		 startActivity(intent);
		 break;
    case R.id.imageButton3:
    	 Intent intent1 = new Intent();
		 intent1.setClass(this, CalcBMIActivity.class);
		 startActivityForResult(intent1,R.id.imageButton1);
    	
    	 break;
    case R.id.imageButton4:
    	 //Intent intent4 = new Intent();
	 	 //intent4.setClass(this, ShareActivity.class);
		 //startActivityForResult(intent4,R.id.imageButton4);	
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
     	shareIntent.setType("text/plain");
     	shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Content subject");
     	shareIntent.putExtra(Intent.EXTRA_TEXT, "Content text");
     	startActivity(Intent.createChooser(shareIntent, "Sharing something."));
    	 break;
	case R.id.imageButton5:
		 showDialog(IDD_DIALOG1);			
		 break;
	case R.id.imageButton6:
		Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=mast.avalons&feature=search_result#?t=W251bGwsMSwxLDEsIm1hc3QuYXZhbG9ucyJd"));
    	startActivity(browseIntent);
    	Log.d("", "Start browser");
		finish();
		 break;
		 
		}
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case IDD_DIALOG:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(fst_msg);
            builder.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    
                }
            });
            builder.setCancelable(false);
            return builder.create();
        case IDD_DIALOG1:
        	AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(scd_msg);
            builder1.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    
                }
            });
            builder1.setCancelable(false);
            
            return builder1.create();
        default:
        return null;
        }
    }
    private void CallAddDialog(final String date) { 
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.dialog, null);
        final TextView output_date = (TextView)root.findViewById(R.id.output_date_text);
        final EditText textWeight = (EditText)root.findViewById(R.id.weight);
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        //root.setBackgroundResource(R.drawable.mastbackground);
        output_date.setText(getResources().getText(R.string.dialog_date)+"       "+date);
        b.setView(root);
        b.setTitle(R.string.title_add);
        b.setPositiveButton(
                R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ContentValues values = new ContentValues(2);
                
                values.put(DbHelper.DATE, date);
                values.put(DbHelper.WEIGHT, textWeight.getText().toString());
                
                getContentResolver().insert(Provider.CONTENT_URI, values);
                mCursor.requery();
            }
        });
        b.setNegativeButton(
                R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        b.show(); 
    }
  	   
}