package org.googlecode.vkontakte_android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VLog {

	private static Logger logger = Logger.getLogger("org.googlecode.vkontakte_android");
	public static String filename = null;
	
	public static void initialize(Context ctx) {
     	// Random number to avoid duplicate files
    	Random generator = new Random();
    	int random = generator.nextInt(99999); 
    	// Embed version in stacktrace filename
    	PackageManager pm = ctx.getPackageManager();
    	PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(ctx.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		VLog.filename = pi.versionName+"-"+Integer.toString(random);

		FileHandler fh = null;
		try {
			fh = new FileHandler(ctx.getFilesDir().getAbsolutePath()+"/"+VLog.filename+".log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh); 
    	logger.setLevel(Level.ALL); 
	}
	
	public static void d(String TAG, String message) {
		Log.d(TAG, message);
		logger.fine(TAG+message);
	}
	
	public static void i(String TAG, String message) {
		Log.i(TAG, message);
		logger.info(TAG+message);
	}
	
	public static void e(String TAG, String message) {
		Log.e(TAG, message);
		logger.severe(TAG+message);
	}
	
	public static void e(String TAG, String message, Exception e) {
		Log.e(TAG, message, e);
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(ba));
		logger.severe(TAG+message+"\n"+ba.toString());
	}
}
