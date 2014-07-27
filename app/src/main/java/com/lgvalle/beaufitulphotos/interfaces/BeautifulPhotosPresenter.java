package com.lgvalle.beaufitulphotos.interfaces;

import android.content.Context;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Feature;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * Interface for main activity presenter.
 * This is just a simple example to illustrate how it's works
 */
public interface BeautifulPhotosPresenter {
	/**
	 * Request photos from service and post results into events bus
	 */
	void needPhotos();

	/**
	 * Request details for photo and post results into events bus
	 * @param photoModel Photo from which details are needed
	 */
	void needPhotoDetails(PhotoModel photoModel);

	/**
	 * Switch service feature resetting previous data and requesting new photos
	 * @param feature New feature to switch to
	 */
	void switchFeature(Feature feature);

	/**
	 * Share photo with system intent
	 */
	void share(Context ctx);
}
