package org.googlecode.vkontakte_android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by Ildar Karimov
 * Date: Sep 1, 2009
 */
public class TabHelper {
    public static TextView injectTabCounter(TabWidget tabWidget, int i, Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.WHITE);
        tv.setVisibility(View.INVISIBLE);

        int cornerRadius = 10;
        float[] outerR = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        RectF inset = new RectF(2, 2, 2, 2);
        float[] innerR = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        ShapeDrawable circle = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR));
        circle.setPadding(6, 0, 6, 0);

        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.BLUE, Color.RED});
        gradient.setCornerRadius(cornerRadius);
        gradient.setBounds(circle.getBounds());

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradient, circle});
        tv.setBackgroundDrawable(layerDrawable);

        if (tabWidget.getChildAt(i) instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) tabWidget.getChildAt(i);
            for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                if (relativeLayout.getChildAt(j) instanceof ImageView) {
                    relativeLayout.removeViewAt(j);
                    //todo: make compound with that wrap ImageView
                    relativeLayout.addView(tv, j);
                    break;
                }
            }
        }
        return tv;
    }
}
