package org.googlecode.vkontakte_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
import android.widget.TextView;

public class CMessageFactory {

    public static View getMessageView(Context ctx, String from, Bitmap bm, String message) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.messages_record,
                null);

        Log.d("mess", message);

        TextView vfrom = (TextView) v.findViewById(R.id.mess_from);
        vfrom.setText(from);

        TextView vmess = (TextView) v.findViewById(R.id.message_text);
        vmess.setText(message);


        //ImageView photo = (ImageView) v.findViewById(R.id.sender_photo);
        //photo.setImageBitmap( bm==null ? CImagesManager.getBitmap("stub"):bm);


     //   ImageButton b = (ImageButton) v.findViewById(R.id.answer_btn);
        //b.setImageDrawable(new BitmapDrawable(CImagesManager.getBitmap("reply")));

        return v;

    }

}
