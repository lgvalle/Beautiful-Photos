package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500pxImage implements Parcelable {
	@Expose
	private String url;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
	}

	public Photo500pxImage() {
	}

	private Photo500pxImage(Parcel in) {
		this.url = in.readString();
	}

	public static final Creator<Photo500pxImage> CREATOR = new Creator<Photo500pxImage>() {
		public Photo500pxImage createFromParcel(Parcel source) {
			return new Photo500pxImage(source);
		}

		public Photo500pxImage[] newArray(int size) {
			return new Photo500pxImage[size];
		}
	};
}
