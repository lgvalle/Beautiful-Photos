package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lgvalle on 27/07/14.
 * Model for 'favorites' query. We only care about number of favorites here
 */
public class Favorites {
	@SerializedName("total_items")
	int totalItems;

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
}
