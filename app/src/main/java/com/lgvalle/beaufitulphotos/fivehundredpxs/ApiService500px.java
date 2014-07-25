package com.lgvalle.beaufitulphotos.fivehundredpxs;

import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Retrofit interface describing a 500px APi endpoint
 */
public interface ApiService500px {
	public final static int FIRST_PAGE = 1;
	public final static int SIZE_SMALL = 3;
	public final static int SIZE_BIG = 4;

	/**
	 * Query for /photos and specify a image size and page
	 * Documentation: https://github.com/500px/api-documentation/blob/master/endpoints/photo/GET_photos.md
	 *
	 * For example, this creates a query like: https://api.500px.com/v1/photos?feature=popular&image_size[]=2&image_size[]=4&page=1
	 *
	 * @param feature For example 'popular'
	 * @param sizeSmall Number between 1 and 4
	 * @param sizeLarge Number between 1 and 4
	 * @param page Page to fetch, starts in 1
	 * @param callback Callback to access response
	 */
	@GET("/photos")
	void getPhotosPopular(@Query("feature") String feature,
	                      @Query("image_size[]") int sizeSmall,
	                      @Query("image_size[]") int sizeLarge,
	                      @Query("page") int page,
	                      Callback<PhotosResponse> callback);

}
