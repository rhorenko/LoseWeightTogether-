package org.googlecode.vkontakte_android.database;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.net.URLEncoder;

public class StoredFile {
    private static final String TAG = "VK:StoredFile";
    private static final byte[] buffer = new byte[1024];

    //not thread safe!
    public static void save(Context context, String url, InputStream is) {
        FileOutputStream os = null;
        try {
            os = context.openFileOutput(URLEncoder.encode(url), Context.MODE_PRIVATE);
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getFile(String url, Context context) {
        byte[] data = new byte[0];
        FileInputStream stream = null;
        try {
            stream = context.openFileInput(URLEncoder.encode(url));
            data = new byte[stream.available()];
            int read = stream.read(data);
            if (read != data.length)
                Log.e(TAG, "problem reading file for " + url + ": expected " + data.length + ", but got " + read);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static FileInputStream getStream(String url, Context context) {
        FileInputStream stream = null;
        try {
            stream = context.openFileInput(URLEncoder.encode(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stream;
    }

    public static void delete(Context context, String url) {
        context.deleteFile(URLEncoder.encode(url));
    }
}