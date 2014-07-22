package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import com.google.gson.annotations.Expose;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500pxUser {

	@Expose
	private Integer id;

	@Expose
	private String fullname;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
