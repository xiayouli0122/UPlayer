package com.yuri.uplayer.helpers;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;

/**
 * new this class to update mediaprovider,when you add a media file or delete a media file</br>
 * you can do like this:new SingleMediaScanner(this,file) or new SingleMediaScanner(this,filepath);
 * @date 20130929
 */
public class SingleMediaScanner implements MediaScannerConnectionClient {
	private static final String TAG = "SingleMediaScanner";
	private MediaScannerConnection mConnection;
	private String mFilePath;
	
	public SingleMediaScanner(Context context, File file){
		new SingleMediaScanner(context, file.getAbsolutePath());
	}
	
	public SingleMediaScanner(Context context, String path){
		this.mFilePath = path;
		mConnection = new MediaScannerConnection(context, this);
		mConnection.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		mConnection.scanFile(mFilePath, null);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		Log.d(TAG, "onScanCompleted.path:" + path);
		if (null == uri) {
			Log.e(TAG, "onScanCompleted.failed");
		}
		mConnection.disconnect();
	}

}
