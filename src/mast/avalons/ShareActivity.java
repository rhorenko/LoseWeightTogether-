package mast.avalons;

import org.googlecode.vkontakte_android.ui.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fi.harism.facebook.MainActivity;

public class ShareActivity extends Activity{
	
	
    
	 @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.share);
	 }
	//Обрабатываем нажатия кнопок
	    public void myClickHandler(View view) {
	    	switch (view.getId()) {
					
	    case R.id.imgBtnVKShare:
	    	 Intent intent = new Intent();
			 intent.setClass(this, LoginActivity.class);
			 startActivity(intent);
			 break;
	    case R.id.imgBtnFacebookShare:
	    	 Intent intent3 = new Intent();
			 intent3.setClass(this, MainActivity.class);
			 startActivityForResult(intent3,R.id.imageButton1);
	    	 break;
	    	 
			}
	    }
	

}
