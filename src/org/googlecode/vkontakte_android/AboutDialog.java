package org.googlecode.vkontakte_android;

import mast.avalons.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AboutDialog {
    public static Dialog makeDialog(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.about, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(R.drawable.vkontakte_logo_48)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setView(view)
                .setNegativeButton(R.string.legal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //todo: insert license dialog
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(android.R.string.ok, null)
                .create();

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            TextView build = (TextView) view.findViewById(R.id.build);
            build.setText(context.getResources().getString(R.string.version) + " " + pi.versionName + context.getResources().getString(R.string.build) + " " + Integer.toString(pi.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
//             TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dialog;
    }
}
