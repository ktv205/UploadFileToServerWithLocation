package com.example.uploadfiletoserverwithlocation;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadModel implements Parcelable {
	private String filePath;
	private double lat;
	private double lng;
	private double rad;
	
	
  public UploadModel(){
	  
  }
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getRad() {
		return rad;
	}
	public void setRad(double rad) {
		this.rad = rad;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
    public static final Parcelable.Creator<UploadModel> CREATOR=
    		new Creator<UploadModel>(){

				@Override
				public UploadModel createFromParcel(Parcel source) {
					// TODO Auto-generated method stub
					return new UploadModel(source);
				}

				@Override
				public UploadModel[] newArray(int size) {
					// TODO Auto-generated method stub
					return new UploadModel[size];
				}
    	
    };
    public UploadModel(Parcel p){
    	filePath=p.readString();
        lat=p.readDouble();
        lng=p.readDouble();
        rad=p.readDouble();
    	
    }
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(filePath);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeDouble(rad);
		
		
	}
     
}
