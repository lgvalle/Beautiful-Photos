package com.lgvalle.photosnearby;

import com.lgvalle.photosnearby.interfaces.PhotosNearbyPresenter;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosNearbyPresenterImpl implements PhotosNearbyPresenter{
	private final PhotosNearbyActivity screen;

	public PhotosNearbyPresenterImpl(PhotosNearbyActivity photosNearbyActivity) {
		this.screen = photosNearbyActivity;
	}

	@Override
	public void needPhotos() {

	}
}
