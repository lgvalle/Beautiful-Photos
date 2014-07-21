package com.lgvalle.photosnearby.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosResponse {

	@SerializedName("current_page")
	@Expose
	private Integer currentPage;
	@SerializedName("total_pages")
	@Expose
	private Integer totalPages;
	@Expose
	private List<Photo500Px> photos;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<Photo500Px> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo500Px> photos) {
		this.photos = photos;
	}
}
