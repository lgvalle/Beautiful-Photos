package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500px implements PhotoModel, Parcelable, Comparable<Photo500px> {
	private static final int INDEX_SMALL = 0;
	private static final int INDEX_LARGE = 1;

	@Expose
	private Integer id;
	@SerializedName("user_id")
	@Expose
	private Integer userId;
	@Expose
	private String name;
	@Expose
	private String description;
	@Expose
	private List<Photo500pxImage> images = new ArrayList<Photo500pxImage>();
	@Expose
	private Photo500pxUser user;
	private Integer favorites;

	public Photo500px(String name) {
		this.name = name;
	}

	@Override
	public String getAuthorName() {
		return user.getFullname();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFavorites() {
		return favorites;
	}

	public void setFavorites(Integer favorites) {
		this.favorites = favorites;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Photo500pxImage> getImages() {
		return images;
	}

	public void setImages(List<Photo500pxImage> images) {
		this.images = images;
	}

	@Override
	public String getLargeUrl() {
		return images.get(INDEX_LARGE).getUrl();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSmallUrl() {
		return images.get(INDEX_SMALL).getUrl();
	}

	@Override
	public String getTitle() {
		return getName();
	}

	public Photo500pxUser getUser() {
		return user;
	}

	public void setUser(Photo500pxUser user) {
		this.user = user;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public int compareTo(Photo500px photo500px) {
		return id.compareTo(photo500px.getId());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeValue(this.userId);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeList(this.images);
		dest.writeParcelable(this.user, flags);
	}

	public Photo500px() {
	}

	private Photo500px(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
		this.name = in.readString();
		this.description = in.readString();
		this.images = (List<Photo500pxImage>) in.readSerializable();
		this.user = in.readParcelable(Photo500pxUser.class.getClassLoader());
	}

	public static final Creator<Photo500px> CREATOR = new Creator<Photo500px>() {
		public Photo500px createFromParcel(Parcel source) {
			return new Photo500px(source);
		}

		public Photo500px[] newArray(int size) {
			return new Photo500px[size];
		}
	};
}
