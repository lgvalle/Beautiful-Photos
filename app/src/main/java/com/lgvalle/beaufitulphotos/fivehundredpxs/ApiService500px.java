package com.lgvalle.beaufitulphotos.fivehundredpxs;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by lgvalle on 22/07/14.
 */
public interface ApiService500px {

	@GET("/photos?feature=popular&image_size[]=2&image_size[]=4")
	void getPhotosPopular(Callback<PhotosResponse> callback);

}
