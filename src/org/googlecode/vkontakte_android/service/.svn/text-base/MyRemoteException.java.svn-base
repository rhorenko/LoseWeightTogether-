package org.googlecode.vkontakte_android.service;

import android.os.RemoteException;

/**
 *  Used for encapsulation of non-remote exception for throwing them from the service. 
 */
public class MyRemoteException extends RemoteException {
    private static final long serialVersionUID = 1L;
    public Exception innerException;

    public MyRemoteException() {
    }

    public MyRemoteException(Exception innerException) {
        this.innerException = innerException;
    }
}
