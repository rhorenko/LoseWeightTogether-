package org.googlecode.vkontakte_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CImagesManager {

    public enum Icons {
        STUB
    }

    private static Map<Icons, Bitmap> s_bitmaps = new HashMap<Icons, Bitmap>();
    private static Map<Icons, Integer> s_iconPaths = new HashMap<Icons, Integer>();

    static {
        s_iconPaths.put(Icons.STUB, R.drawable.stub);
    }

    public static Bitmap getBitmap(Context ctx, Icons icon) {
        if (!s_bitmaps.containsKey(icon)) {
            InputStream is = ctx.getResources().openRawResource(s_iconPaths.get(icon));
            s_bitmaps.put(icon, Bitmap.createScaledBitmap(BitmapFactory
                    .decodeStream(is), 60, 60, false));
        }
        return s_bitmaps.get(icon);
    }
}
