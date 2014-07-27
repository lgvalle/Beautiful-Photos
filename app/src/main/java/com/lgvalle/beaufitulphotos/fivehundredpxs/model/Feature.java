package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import com.lgvalle.beaufitulphotos.R;

/**
 * Created by luis.gonzalez on 23/07/14.
 */
public enum Feature {
	Popular("popular", R.string.feature_popular, R.drawable.ic_action_group),
	HighestRated("highest_rated", R.string.feature_highest_rated, R.drawable.ic_action_important);

	private final String param;
	private final int title;
	private final int icon;


	Feature(String param, int title, int icon) {
		this.param = param;
		this.title = title;
		this.icon = icon;
	}

	public int getIcon() {
		return icon;
	}

	public String getParam() {
		return param;
	}

	public int getTitle() {
		return title;
	}
}
