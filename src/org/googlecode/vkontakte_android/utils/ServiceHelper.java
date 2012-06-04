package org.googlecode.vkontakte_android.utils;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import org.googlecode.vkontakte_android.service.IVkontakteService;

public class ServiceHelper {

    private final static String TAG = "VK:ServiceHelper";

    private static IVkontakteService mVKService = null;

    public static IVkontakteService getService() throws RemoteException {
        if (ServiceHelper.mVKService == null) {
            Log.e(TAG, "Service binder is null");
            throw new RemoteException();
        }
        return ServiceHelper.mVKService;
    }

    public static boolean isBinded() {
    	return ServiceHelper.mVKService != null;
    }
    
    public static void connect(IBinder service) {
        ServiceHelper.mVKService = IVkontakteService.Stub.asInterface(service);
        VLog.d(TAG, "Service has been connected");
    }

    public static void disconnect() {
        VLog.d(TAG, "Service has been disconnected");
    }
}
