package org.googlecode.vkontakte_android.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;


import org.googlecode.vkontakte_android.CImagesManager;
import org.googlecode.vkontakte_android.ComposeMessageActivity;
import org.googlecode.vkontakte_android.ProfileViewActivity;
import org.googlecode.vkontakte_android.CImagesManager.Icons;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.provider.UserapiProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_MESSAGE_SENDERID;
import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_PROFILE_USERID;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.USERS_URI;

/**
 * Created by Ildar Karimov
 * Date: Oct 10, 2009
 */
public class UserHelper {

	@SuppressWarnings("unused")
	private static final String TAG = "VK:UserHelper";
	
    public static void viewProfile(Context context, long userId) {
        Intent intent = new Intent(context, ProfileViewActivity.class);
        intent.putExtra(KEY_PROFILE_USERID, userId);
        context.startActivity(intent);
    }

    public static void sendMessage(Context context, long userId) {
        Intent intent = new Intent(context, ComposeMessageActivity.class);
        intent.putExtra(KEY_MESSAGE_SENDERID, userId);
        context.startActivity(intent);
    }

}
