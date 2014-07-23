package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500pxUser implements Parcelable{

	@Expose
	private Integer id;

	@Expose
	private String fullname;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.fullname);
	}

	public Photo500pxUser() {
	}

	private Photo500pxUser(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.fullname = in.readString();
	}

	public static final Creator<Photo500pxUser> CREATOR = new Creator<Photo500pxUser>() {
		public Photo500pxUser createFromParcel(Parcel source) {
			return new Photo500pxUser(source);
		}

		public Photo500pxUser[] newArray(int size) {
			return new Photo500pxUser[size];
		}
	};
}
