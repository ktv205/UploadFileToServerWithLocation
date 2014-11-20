package com.example.uploadfiletoserverwithlocation;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	final static int REQUEST_CODE = 0;
	final static String DEBUG_TAG = "MainActivity";
	private LocationClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(DEBUG_TAG, "onCreate");
		findViewById(R.id.upload_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("audio/mpeg");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						startActivityForResult(intent, REQUEST_CODE);
					}
				});
		findViewById(R.id.submit_main).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
		client = new LocationClient(this, this, this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		client.connect();
	}

	@Override
	protected void onStop() {
		client.disconnect();
		super.onStop();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(DEBUG_TAG, "onActivityResult");
		if (requestCode == REQUEST_CODE) {
			Log.d(DEBUG_TAG, "onActivityResult,request code passed");
			if (resultCode == RESULT_OK) {
				Log.d(DEBUG_TAG, "onActivityResult,result code passed");
				Uri uri = data.getData();
				Log.d(DEBUG_TAG, "cotent-uri->" + uri.toString());
				List<String> list=getFilePathFromUri(uri);
				TextView meta=(TextView)findViewById(R.id.song_tile);
				meta.setText(list.get(1));
			}
		}
	}

	@SuppressLint("NewApi")
	public List<String>  getFilePathFromUri(Uri contentURI) {
		List<String> list=new ArrayList<String>(); 
		String wholeID = DocumentsContract.getDocumentId(contentURI);
		Log.d("wholeID", wholeID);
		String id = wholeID.split(":")[1];
		String[] column = { MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DISPLAY_NAME };

		// where id is equal to
		String sel = MediaStore.Audio.Media._ID + "=?";

		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, column, sel,
				new String[] { id }, null);

		String filePath = "";
		String title="";

		int columnIndexPath = cursor.getColumnIndex(column[0]);
		int columnIndexTitle=cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
		Log.d("cursor", cursor.getCount() + "");
		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndexPath);
			title=cursor.getString(columnIndexTitle);
			Log.d("string title",title);
			Log.d("string path", filePath);
		}
		list.add(filePath);
		list.add(title);
		return list;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(DEBUG_TAG, "onConnectionFailed");
        
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d(DEBUG_TAG, "onConnected");
        Location currentLocation;
        currentLocation=client.getLastLocation();
	    setCurrentLatLng(currentLocation);
	}
	

	@Override
	public void onDisconnected() {
		Log.d(DEBUG_TAG, "onDisconnected");
	}
	public void setCurrentLatLng(Location location){
		TextView lat=(TextView)findViewById(R.id.lat);
		TextView lng=(TextView)findViewById(R.id.lng);
		lat.setText(String.valueOf(location.getLatitude()));
		lng.setText(String.valueOf(location.getLongitude()));
	}
}
