package com.example.uploadfiletoserverwithlocation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {
    private UploadModel model;
    private UploadParams params;
    final static String DEBUG_TAG="UploadServce";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		model=intent.getExtras().getParcelable("UPLOADMODEL");
		Log.d(DEBUG_TAG,"path->"+model.getFilePath());
		uploadThread();
		return START_STICKY;
	}
	public void uploadThread(){
		Thread thread=new Thread(){
			@Override
			public void run(){
				//Log.d(DEBUG_TAG,MyHttpConntection.uploadFile(model.getFilePath()));
				params =new UploadParams();
				params.setURI("http://54.173.51.136/db/getMP3File.php");
				params.setMethod("POST");
				params.setParam("lat", String.valueOf(model.getLat()));
				params.setParam("lng",String.valueOf(model.getLng()));
				params.setParam("rad", String.valueOf(model.getRad()));
				params.setParam("path", model.getFilePath());
				Log.d(DEBUG_TAG,MyHttpConntection.UploadFileAndParams(params));
				
			}
		};
		thread.start();
	}
	

}
