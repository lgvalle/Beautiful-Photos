package com.lgvalle.photosnearby.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by lgvalle on 21/04/14.
 */
public class FontsHelper {
	public static Typeface robotoCondensedRegular = null;

	private FontsHelper() { }

	/**
	 * Method init.
	 * @param context Context
	 */
	public static void init(AssetManager assets) {
		// FONT CONFIG
		robotoCondensedRegular = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Regular.ttf");
	}


	/**
	 * Set custom font on a textview array
	 */
	public static void setFont(Typeface font, TextView... views) {
		for(TextView v : views) {
			v.setTypeface(font);
		}
	}
}