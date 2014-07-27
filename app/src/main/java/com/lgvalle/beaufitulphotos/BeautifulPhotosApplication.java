package com.lgvalle.beaufitulphotos;

import android.app.Application;
import com.lgvalle.beaufitulphotos.fivehundredpxs.Api500pxModule;
import com.lgvalle.beaufitulphotos.utils.TypefaceUtil;

/**
 * Created by lgvalle on 21/07/14.
 */
public class BeautifulPhotosApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// Init service module
		Api500pxModule.init(this);
		// Replace font typeface in all application
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/RobotoCondensed-Regular.ttf");
	}

}
