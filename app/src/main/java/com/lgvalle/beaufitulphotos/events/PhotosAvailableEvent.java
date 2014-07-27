package com.lgvalle.beaufitulphotos.events;

import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 *
 * Event: new photos list available
 */
public class PhotosAvailableEvent {
	private final List<? extends PhotoModel> photos;

	public PhotosAvailableEvent(List<? extends PhotoModel> photos) {
		this.photos = photos;
	}

	public List<? extends PhotoModel> getPhotos() {
		return photos;
	}
}
