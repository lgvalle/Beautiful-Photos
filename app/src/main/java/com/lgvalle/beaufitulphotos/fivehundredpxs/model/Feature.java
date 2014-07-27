package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import com.lgvalle.beaufitulphotos.R;

/**
 * Created by luis.gonzalez on 23/07/14.
 * Enum to represent service features
 */
public enum Feature {
	Popular("popular", R.string.feature_popular),
	HighestRated("highest_rated", R.string.feature_highest_rated);

	private final String param;
	private final int title;

	Feature(String param, int title) {
		this.param = param;
		this.title = title;
	}

	public String getParam() {
		return param;
	}

	public int getTitle() {
		return title;
	}
}
