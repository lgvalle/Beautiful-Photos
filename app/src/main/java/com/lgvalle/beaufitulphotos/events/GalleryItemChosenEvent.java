package com.lgvalle.beaufitulphotos.events;

import android.view.View;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Event: Item selected in gallery
 */
public class GalleryItemChosenEvent {

	private final View view;
	private PhotoModel photo;

	public GalleryItemChosenEvent(PhotoModel photo, View v) {
		this.photo = photo;
		this.view = v;
	}

	public PhotoModel getPhoto() {
		return photo;
	}

	public View getView() {
		return view;
	}
}
