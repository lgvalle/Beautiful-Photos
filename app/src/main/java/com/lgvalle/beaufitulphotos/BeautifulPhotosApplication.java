package com.lgvalle.beaufitulphotos;

import android.app.Application;
import com.lgvalle.beaufitulphotos.util.TypefaceUtil;

/**
 * Created by lgvalle on 21/07/14.
 */
public class BeautifulPhotosApplication extends Application {
	private static final String TAG = BeautifulPhotosApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/RobotoCondensed-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
	}


}
