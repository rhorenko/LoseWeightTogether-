package org.googlecode.vkontakte_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CWallMessageFactory {

    private CWallMessageFactory() {

    }

    private static Bitmap s_defaultBitmap;


    public final static int TEXT_MESSAGE = 42;
    public final static int PIC_MESSAGE = 43;

    public static View getMessageView(Context ctx, String from, String msg) {

        View v = LayoutInflater.from(ctx).inflate(R.layout.wall_record, null);
        LinearLayout lay = (LinearLayout) v.findViewById(R.id.wall_message_layout);

        TextView from_view = (TextView) v.findViewById(R.id.wall_from);
        from_view.setText(from);

        TextView tw = new TextView(ctx);
        tw.setId(TEXT_MESSAGE);
        tw.setText(msg);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 80);
        tw.setLayoutParams(params);

        lay.addView(tw, 0);
        return v;

    }

    public static View getMessageView(Context ctx, String from, Bitmap pic) {

        View v = LayoutInflater.from(ctx).inflate(R.layout.wall_record, null);
        LinearLayout lay = (LinearLayout) v.findViewById(R.id.wall_message_layout);

        TextView from_view = (TextView) v.findViewById(R.id.wall_from);
        from_view.setText(from);


        ImageView im = new ImageView(ctx);
        im.setId(PIC_MESSAGE);
        im.setImageBitmap(pic == null ? s_defaultBitmap : pic);

        //temp
        ImageButton del = (ImageButton) v.findViewById(R.id.wall_del);
        del.setVisibility(View.INVISIBLE);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 80);
        im.setLayoutParams(params);

        lay.addView(im, 0);
        return v;

    }

}
