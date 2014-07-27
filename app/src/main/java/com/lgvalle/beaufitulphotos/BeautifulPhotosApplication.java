package com.lgvalle.beaufitulphotos;

import android.app.Application;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiALTModule500px;
import com.lgvalle.beaufitulphotos.utils.TypefaceUtil;

/**
 * Created by lgvalle on 21/07/14.
 */
public class BeautifulPhotosApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ApiALTModule500px.init(this);
		// Replace font typeface in all application
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/RobotoCondensed-Regular.ttf");
	}

}
