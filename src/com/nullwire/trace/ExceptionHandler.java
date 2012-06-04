/*
Copyright (c) 2009 nullwire aps

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

Contributors: 
Mads Kristiansen, mads.kristiansen@nullwire.com
Glen Humphrey
Evan Charlton
Peter Hewitt
*/

package com.nullwire.trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.googlecode.vkontakte_android.utils.AppHelper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.util.Log;

public class ExceptionHandler {
	
	public static String TAG = "com.nullwire.trace.ExceptionsHandler";

	private static Handler s_han;
	private static Context s_ctx;
	/**
	 * Register handler for unhandled exceptions.
	 * @param app 
	 * @param han
	 */
	public static boolean register(Context ctx, Handler han) {
		Log.i(TAG, "Registering default exceptions handler: " + G.URL);
		s_han = han;
		s_ctx = ctx;
		Log.i(TAG, "Registering default exceptions handler");
		// Get information about the Package
		PackageManager pm = s_ctx.getPackageManager();
		try {
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(s_ctx.getPackageName(), 0);
			G.APP_VERSION = pi.versionName;
			// Package name
			G.APP_PACKAGE = pi.packageName;
			// Files dir for storing the stack traces
			G.FILES_PATH = s_ctx.getFilesDir().getAbsolutePath();
			// Device model
            G.PHONE_MODEL = android.os.Build.MODEL;
            // Android version
            G.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "TRACE_VERSION: " + G.TraceVersion);
		Log.d(TAG, "APP_VERSION: " + G.APP_VERSION);
		Log.d(TAG, "APP_PACKAGE: " + G.APP_PACKAGE);
		Log.d(TAG, "FILES_PATH: " + G.FILES_PATH);
		Log.d(TAG, "URL: " + G.URL);
		
		boolean stackTracesFound = false;
		// We'll return true if any stack traces were found
		if ( searchForStackTraces().length > 0 ) {
			stackTracesFound = true;
		}
		
		//it sends report
		new Thread() {
			@Override
			public void run() {
		       	s_han.post(new Runnable() {
					
					public void run() {
						String[] list = searchForStackTraces();
						if ( list != null && list.length > 0 ) {
							AppHelper.showExceptionDialog(s_ctx);
						}
					}
				}); 
		       	
				// First of all transmit any stack traces that may be lying around
				
				UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
				if (currentHandler != null) {
					Log.d(TAG, "current handler class="+currentHandler.getClass().getName());
				}	
				// don't register again if already registered
				if (!(currentHandler instanceof DefaultExceptionHandler)) {
					// Register default exceptions handler
					Thread.setDefaultUncaughtExceptionHandler(
							new DefaultExceptionHandler(currentHandler));
				}
			}
       	}.start();

 		return stackTracesFound;
	}
	
	/**
	 * Search for stack trace files.
	 * @return
	 */
	private static String[] searchForStackTraces() {
		File dir = new File(G.FILES_PATH + "/");
		// Try to create the files folder if it doesn't exist
		dir.mkdir();
		// Filter for ".stacktrace" files
		FilenameFilter filter = new FilenameFilter() { 
			public boolean accept(File dir, String name) {
				return name.endsWith(".stacktrace"); 
				
			} 
		}; 
		return dir.list(filter);	
	}
	
	/**
	 * Search for logfiles.
	 * @return
	 */
	private static String[] searchForLogs() {
		File dir = new File(G.FILES_PATH + "/");
		// Try to create the files folder if it doesn't exist
		dir.mkdir();
		// Filter for ".log" files
		FilenameFilter filter = new FilenameFilter() { 
			public boolean accept(File dir, String name) {
				return  name.endsWith(".log"); 
			} 
		}; 
		return dir.list(filter);	
	}
	
	/**
	 * Look into the files folder to see if there are any "*.stacktrace" files.
	 * If any are present, submit them to the trace server.
	 */
	public static void submitStackTraces() {
		try {
			Log.d(TAG, "Looking for exceptions in: " + G.FILES_PATH);
			//TODO append logs to related stacktraces
			
			//collecting log records
			String[] logs = searchForLogs();
			String logRecords = null;
			StringBuilder logcontents = new StringBuilder();
			if ( logs != null && logs.length > 0 ) {
				Log.d(TAG, "Found "+logs.length+" log(s)");
				for (int i=0; i < logs.length; i++) {
					String filePath = G.FILES_PATH+"/"+logs[i];
					logcontents.append("\n======== LOGS ===========");
					BufferedReader input =  new BufferedReader(new FileReader(filePath));
					String line = null;
					while (( line = input.readLine()) != null){ 
						logcontents.append(line); 
					}
					input.close();
				}
			}
			logRecords = logcontents.toString();
			
			//collecting and sending stacktrace
			String[] list = searchForStackTraces();
			if ( list != null && list.length > 0 ) {
				Log.d(TAG, "Found "+list.length+" stacktrace(s)");
				for (int i=0; i < list.length; i++) {
					String filePath = G.FILES_PATH+"/"+list[i];
					// Extract the version from the filename: "packagename-version-...."
					String version = list[i].split("-")[0];
					Log.d(TAG, "Stacktrace in file '"+filePath+"' belongs to version " + version);
					// Read contents of stacktrace
					StringBuilder contents = new StringBuilder();
					BufferedReader input =  new BufferedReader(new FileReader(filePath));
					String line = null;
					String androidVersion = null;
	                String phoneModel = null;
	                while (( line = input.readLine()) != null){
                        if (androidVersion == null) {
                            androidVersion = line;
                            continue;
                        }
                        else if (phoneModel == null) {
                            phoneModel = line;
                            continue;
                        }
                        contents.append(line);
			            contents.append(System.getProperty("line.separator"));
			        }
			        input.close();
			        String stacktrace;
			        stacktrace = contents.toString();
			       
			        Log.d(TAG, "Transmitting stack trace: " + stacktrace);
			        // Transmit stack trace with POST request
					DefaultHttpClient httpClient = new DefaultHttpClient(); 
					HttpPost httpPost = new HttpPost(G.URL);
					List <NameValuePair> nvps = new ArrayList <NameValuePair>(); 
					nvps.add(new BasicNameValuePair("package_name", G.APP_PACKAGE));
					nvps.add(new BasicNameValuePair("package_version", version));
                    nvps.add(new BasicNameValuePair("phone_model", phoneModel));
                    nvps.add(new BasicNameValuePair("android_version", androidVersion));
                    nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
                    nvps.add(new BasicNameValuePair("log", logRecords));
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
					// We don't care about the response, so we just hope it went well and on with it
					httpClient.execute(httpPost);	
				}
			}
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			deleteStackTrace();
		}
	}
	
	public static void deleteStackTrace() {
		try {
			//deleting stacktraces
			String[] trlist = searchForStackTraces(); 
			for ( int i = 0; i < trlist.length; i ++ ) {
				File file = new File(G.FILES_PATH+"/"+trlist[i]);
				file.delete();
			}
			//deleting logs
			String[] llist = searchForLogs(); 
			for ( int i = 0; i < llist.length; i ++ ) {
				File file = new File(G.FILES_PATH+"/"+llist[i]);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
