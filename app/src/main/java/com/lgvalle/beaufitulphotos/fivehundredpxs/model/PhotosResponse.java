package com.lgvalle.beaufitulphotos.fivehundredpxs.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosResponse implements Serializable{

	@JsonProperty("current_page")
	private Integer currentPage;
	@JsonProperty("total_pages")
	private Integer totalPages;
	@JsonProperty("photos")
	private List<Photo500px> photos;

	public PhotosResponse() {
		currentPage = 0;
		totalPages = 0;
		photos = new ArrayList<Photo500px>();
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<Photo500px> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo500px> photos) {
		this.photos = photos;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
