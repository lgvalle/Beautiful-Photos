package com.lgvalle.photosnearby.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500pxImage {

	@Expose
	private Integer size;
	@Expose
	private String url;
	@SerializedName("https_url")
	@Expose
	private String httpsUrl;
	@Expose
	private String format;

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpsUrl() {
		return httpsUrl;
	}

	public void setHttpsUrl(String httpsUrl) {
		this.httpsUrl = httpsUrl;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
