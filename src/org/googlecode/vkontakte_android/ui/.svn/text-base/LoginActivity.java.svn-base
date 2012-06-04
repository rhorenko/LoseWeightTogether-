package org.googlecode.vkontakte_android.ui;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.googlecode.userapi.Captcha;
import org.googlecode.userapi.Credentials;
import org.googlecode.userapi.UserapiLoginException;
import org.googlecode.userapi.UserapiLoginException.ErrorType;
import org.googlecode.vkontakte_android.HomeGridActivity;
import org.googlecode.vkontakte_android.R;
import org.googlecode.vkontakte_android.VApplication;
import org.googlecode.vkontakte_android.database.MessageDao;
import org.googlecode.vkontakte_android.service.MyRemoteException;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;
import org.googlecode.vkontakte_android.utils.ServiceHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Cap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "VK:LoginActivity";
	
    private final int DIALOG_PROGRESS = 0;
    private final int DIALOG_ERROR_PASSWORD = 1;
    private final int DIALOG_ERROR_CAPTCHA = 2;
    private final int DIALOG_ERROR_CONNECTION = 3;
    private final int DIALOG_ERROR_REMOTE = 4;

    private AsyncTask<String, Void, RemoteException> currentTask;

    private boolean viewIsLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferenceHelper.isLogged(this)) {
            setupMainView();
        } else {
            Credentials credentials = PreferenceHelper.getCredentials(this);
            System.out.println("credentials.getSid() = " + credentials.getSid());
            login(credentials.getLogin(), credentials.getPass(), 
            		credentials.getRemixpass(), credentials.getSid());
        }
    }

    private void setupMainView() {
        setContentView(R.layout.login_dialog);
        
       
        ((EditText)findViewById(R.id.login)).addTextChangedListener(new TextWatcher() {
			
        	Button login = (Button) findViewById(R.id.button_login);
        	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (LoginActivity.isEmailValid(s.toString()) 
					|| LoginActivity.isLoginValid(s.toString())) {
					login.setEnabled(true);
				} else {
					login.setEnabled(false);
				}
			}
		});
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button_signup).setOnClickListener(this);
        viewIsLoaded = true;
    }

    private void startHome() {
        Intent intent = new Intent(this, HomeGridActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                login(getLogin(), getPass(), null, null);
                break;
            case R.id.button_signup: 
            	Intent i = new Intent(Intent.ACTION_VIEW); 
            	Uri u = Uri.parse("http://vk.com/reg0"); 
            	i.setData(u); 
            	startActivity(i);
            	break;
        }
    }

    private void login(final String login, 
    		final String pass, 
    		final String remixpass, 
    		final String sid) {
            currentTask = new AsyncTask<String, Void, RemoteException>() {
            @Override
            protected void onPreExecute() {
                LoginActivity.this.showDialog(DIALOG_PROGRESS);
            }

            @Override
            protected void onCancelled() {
                //todo: cancel connection?
            }

            @Override
            protected void onPostExecute(RemoteException e) {
                dismissDialog(DIALOG_PROGRESS);
                if (e == null) {
                    PreferenceHelper.setLogged(LoginActivity.this, true);
                    startHome();
                } else {
                    if (!viewIsLoaded) {
                        setupMainView();
                        ((TextView) findViewById(R.id.login)).setText(login);
                        ((TextView) findViewById(R.id.pass)).setText(pass);
                    }
                    if (e instanceof MyRemoteException) {
                        Exception exception = ((MyRemoteException) e).innerException;
                        if (exception instanceof UserapiLoginException) {
                        	UserapiLoginException usex = (UserapiLoginException) exception;
                            switch (usex.getType()) {
                                case LOGIN_INCORRECT:
                                case LOGIN_INCORRECT_CAPTCHA_NOT_REQUIRED:
                                    LoginActivity.this.showDialog(DIALOG_ERROR_PASSWORD);
                                    PreferenceHelper.setLogged(LoginActivity.this, false);
                                    break;
                                case CAPTCHA_INCORRECT:
                                case LOGIN_INCORRECT_CAPTCHA_REQUIRED:
                                    LoginActivity.this.askForCaptcha();
                                    break;
                            }
                        }
                        if (exception instanceof IOException) {
                            LoginActivity.this.showDialog(DIALOG_ERROR_CONNECTION);
                        }
                    } else {
                        LoginActivity.this.showDialog(DIALOG_ERROR_REMOTE);
                    }
                }
            }

            @Override
            protected RemoteException doInBackground(String... params) {
                    if (! ServiceHelper.isBinded() && !Thread.interrupted()) {  
                        try {
                         	//timeout 2sec to give additional chance for service to bind.
                        	//if returns false, remote exception will be thrown soon
                        	VApplication.s_bindingSem.tryAcquire(2, TimeUnit.SECONDS);
                        	
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                try {
                    ServiceHelper.getService().login(params[0], params[1], params[2], params[3]);
                    
                } catch (RemoteException e) {
                    return e;
                }
                return null;
            }
        }.execute(login, pass, remixpass, sid);

    }

    private String getLogin() {
        return ((TextView) findViewById(R.id.login)).getText().toString();
    }

    private String getPass() {
        return ((TextView) findViewById(R.id.pass)).getText().toString();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage(getString(R.string.state_msg_checking_cred));
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (currentTask != null) currentTask.cancel(true);
                    }
                });
                return pd;
            case DIALOG_ERROR_CONNECTION:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.err_msg_connection_problem))
                        .setMessage(getString(R.string.err_msg_check_connection))
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
            //todo: retry
            case DIALOG_ERROR_PASSWORD:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.err_msg_wrong_pass))
                        .setMessage(getString(R.string.err_msg_wrong_pass_long))
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
            case DIALOG_ERROR_REMOTE:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.err_msg_internal_error))
                        .setMessage(getString(R.string.err_msg_report))
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
            default:
                return super.onCreateDialog(id);
        }
    }


    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String captcha_decoded = data.getStringExtra(CaptchaHandlerActivity.CAPTCHA_TEXT);
			String captcha_sid = data.getStringExtra(CaptchaHandlerActivity.CAPTCHA_SID);
			Log.d(TAG, "Captcha:"+captcha_decoded+", sid: "+captcha_sid);
			Captcha.setCaptchaData(captcha_sid, captcha_decoded);
			login(getLogin(), getPass(), null, null);
		} 
	}

    private void askForCaptcha() {
		Intent i = new Intent(this, CaptchaHandlerActivity.class);
		startActivityForResult(i, 0);
    }
    
    public static boolean isEmailValid(String s) {
       Pattern pattern = Pattern.compile(
              "^[\\w\\.-]*@[\\.\\w-]*$");
       Matcher matcher = pattern.matcher(s);
       return matcher.matches();
    }
    
    public static boolean isLoginValid(String s) {
    	Pattern pattern = Pattern.compile(
          "^\\w+$");
    	Matcher matcher = pattern.matcher(s);
    	return matcher.matches();
    }
}