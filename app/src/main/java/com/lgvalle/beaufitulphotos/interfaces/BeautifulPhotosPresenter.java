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
	void needPhotos();

	void needPhotoDetails(PhotoModel photoModel);

	void switchFeature(Feature feature);

	void share(Context ctx);
}
