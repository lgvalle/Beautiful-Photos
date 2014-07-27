package com.lgvalle.beaufitulphotos.fivehundredpxs;

import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Favorites;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import ly.apps.android.rest.cache.CachePolicy;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.annotations.*;


/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Retrofit interface describing a 500px APi endpoint
 */
@RestService
public interface Api500pxService {
	/* Consumer key is app specific */
	public static final String CONSUMER_KEY_VALUE = "B2VtIGTPFrbg1YXUVujHhKIo5I9lVjBxgPIFk7A4";
	public final static int FIRST_PAGE = 1;
	public final static int SIZE_SMALL = 3;
	public final static int SIZE_BIG = 4;
	public final static int FIVE_MINUTES = 5 * 60 * 1000;

	/**
	 * Query for /photos and specify a image size and page
	 * Documentation: https://github.com/500px/api-documentation/blob/master/endpoints/photo/GET_photos.md
	 * <p/>
	 * For example, this creates a query like: https://api.500px.com/v1/photos?feature=popular&image_size[]=2&image_size[]=4&page=1
	 *
	 * @param feature   For example 'popular'
	 * @param sizeSmall Number between 1 and 4
	 * @param sizeLarge Number between 1 and 4
	 * @param page      Page to fetch, starts in 1
	 * @param callback  Callback to access response
	 */
	@GET("/photos")
	@Cached(policy = CachePolicy.ENABLED, timeToLive = FIVE_MINUTES)
	void getPhotos(@QueryParam("consumer_key") String key,
	               @QueryParam("image_size[]") int sizeSmall,
	               @QueryParam("image_size[]") int sizeLarge,
	               @QueryParam("feature") String feature,
	               @QueryParam("page") int page,
	               Callback<PhotosResponse> callback);


	/**
	 * Returns all users that had favorite a photo.
	 *
	 * @param id       Photo ID
	 * @param callback Object containing number of favorites for a photo
	 */
	@GET("/photos/{id}/favorites")
	@Cached(policy = CachePolicy.ENABLED, timeToLive = FIVE_MINUTES)
	void getFavorites(@QueryParam("consumer_key") String key,
	                  @Path("id") Integer id,
	                  Callback<Favorites> callback);
}
