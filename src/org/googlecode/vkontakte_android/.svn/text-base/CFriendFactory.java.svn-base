package org.googlecode.vkontakte_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
//import android.widget.ImageView;
import android.widget.TextView;

public class CFriendFactory {

    public static View getFriendView(Context ctx, String friendname, Bitmap bm,
                                     boolean online) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.friend_row, null);

        //ImageView photo = (ImageView) v.findViewById(R.id.photo);
        //photo.setImageBitmap( bm==null ? CImagesManager.getBitmap("stub"):bm);

        TextView vfrom = (TextView) v.findViewById(R.id.name);
        vfrom.setText(friendname);

        TextView vmess = (TextView) v.findViewById(R.id.status);
        vmess.setText(online ? R.string.status_online : R.string.status_offline);

//		ImageButton b = (ImageButton) v.findViewById(R.id.send_message);
//		b.setImageDrawable(new BitmapDrawable(CImagesManager.getBitmap("send")));

        return v;

    }

}
