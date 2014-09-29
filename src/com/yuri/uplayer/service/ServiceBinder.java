
package com.yuri.uplayer.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yuri.uplayer.IApolloService;
import com.yuri.uplayer.helpers.utils.MusicUtils;

public class ServiceBinder implements ServiceConnection {
    private final ServiceConnection mCallback;

    public ServiceBinder(ServiceConnection callback) {
        mCallback = callback;
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        MusicUtils.mService = IApolloService.Stub.asInterface(service);
        if (mCallback != null)
            mCallback.onServiceConnected(className, service);
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        if (mCallback != null)
            mCallback.onServiceDisconnected(className);
        MusicUtils.mService = null;
    }
}
