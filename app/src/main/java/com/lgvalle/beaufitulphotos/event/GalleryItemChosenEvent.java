package com.lgvalle.beaufitulphotos.event;

import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;

/**
 * Created by lgvalle on 22/07/14.
 */
public class GalleryItemChosenEvent {

	private PhotoModel photo;

	public GalleryItemChosenEvent(PhotoModel photo) {
		this.photo = photo;
	}

	public PhotoModel getPhoto() {
		return photo;
	}
}
