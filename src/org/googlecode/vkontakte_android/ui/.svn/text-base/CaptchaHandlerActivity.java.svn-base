package org.googlecode.vkontakte_android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import org.googlecode.userapi.UrlBuilder;
import org.googlecode.vkontakte_android.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class CaptchaHandlerActivity extends Activity implements
        DialogInterface.OnClickListener {

    public static final String CAPTCHA_TEXT = "captcha_text";
    public static final String CAPTCHA_SID = "captcha_sid";

    private Random random = new Random();
    private Dialog dialog;

    private String captcha_sid;
    private String captcha_decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = LayoutInflater.from(this).inflate(R.layout.catcha_dialog, null);
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.captcha_request_title)
                .setView(mainView)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this)
                .create();
        dialog.show();

        View refresh = dialog.findViewById(R.id.captcha_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadCaptcha();

            }
        });
        loadCaptcha();
    }

    private String generateCaptchaUrl() {
        captcha_sid = String.valueOf(Math.abs(random.nextLong()));
        String captcha_url = UrlBuilder.makeUrl("captcha") + "&csid=" + captcha_sid;
        return captcha_url;
    }

    private void loadCaptcha() {
        new AsyncTask<Void, Void, Bitmap>() {
            TextView textView = (TextView) dialog.findViewById(R.id.loading);
            ViewSwitcher vs = (ViewSwitcher) dialog.findViewById(R.id.vs);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.iv);

            @Override
            protected void onPreExecute() {
                vs.setDisplayedChild(1);
                textView.setText(R.string.captcha_loading);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    vs.setDisplayedChild(0);
                    imageView.setImageBitmap(bitmap);
                } else {
                    vs.setDisplayedChild(1);
                    textView.setText(R.string.captcha_load_failed);
                }
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                InputStream is = null;
                Bitmap bitmap = null;
                try {
                    String captchaUrl = generateCaptchaUrl();
                    is = new URL(captchaUrl).openStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bitmap;
            }
        }.execute();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent data = new Intent();
        if (DialogInterface.BUTTON_POSITIVE == which) {
            EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.captcha_text);
            captcha_decoded = editText.getText().toString();
            data.putExtra(CAPTCHA_TEXT, captcha_decoded);
            data.putExtra(CAPTCHA_SID, captcha_sid);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED, data);
        }
        finish();
    }
}