package com.lgvalle.photosnearby.event;

import com.lgvalle.photosnearby.model.Photo500Px;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosAvailableEvent {

	private final List<Photo500Px> photos;

	public PhotosAvailableEvent(List<Photo500Px> photos) {
		this.photos = photos;
	}

	public List<Photo500Px> getPhotos() {
		return photos;
	}
}
