package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by lgvalle on 27/07/14.
 * Model for 'favorites' query. We only care about number of favorites in this example
 */
public class Favorites implements Serializable{
	@JsonProperty("total_items")
	int totalItems;

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
}
