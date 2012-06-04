//package org.googlecode.vkontakte_android;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Gallery;
//import android.widget.Toast;
//import android.view.View;
//import android.content.DialogInterface;
//import org.googlecode.userapi.VkontakteAPI;
//import org.googlecode.vkontakte_android.R;
//import org.googlecode.vkontakte_android.LoginDialog;
//
//import java.io.IOException;
//
//
//public class PhotoListActivity extends Activity {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.photo_list);
//        final VkontakteAPI api = new VkontakteAPI();
//        final LoginDialog ld = new LoginDialog(this);
//        ld.show();
//        ld.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            public void onDismiss(DialogInterface dialogInterface) {
//                finish();
//            }
//        });
//        ld.setOnLoginClick(new View.OnClickListener() {
//            public void onClick(View view) {
//                try {
//                    if (api.login(ld.getLogin(), ld.getPass())) {
//                        ld.dismiss();
//                        Gallery gallery = (Gallery) findViewById(R.id.photos);
//                        gallery.setAdapter(new PhotoListAdapter(getApplicationContext(), 0, api));
//                    } else {
//                        Toast.makeText(getApplicationContext(), "login/pass incorrect", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//
//            }
//        });
//
//    }
//}
