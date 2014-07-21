package com.lgvalle.photosnearby;

import android.app.Application;
import com.lgvalle.photosnearby.util.BusHelper;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosNearbyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		BusHelper.init();
	}
}
