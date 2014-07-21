package com.lgvalle.photosnearby.net;

import com.lgvalle.photosnearby.model.PhotosResponse;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created by lgvalle on 21/07/14.
 */
public class ApiModule500px {

	private static final String END_POINT = "https://api.500px.com/v1";
	private static final ApiService500px service;

	public interface ApiService500px {
		@GET("/photos?feature=popular")
		void getPhotosPopular(Callback<PhotosResponse> callback);
	}

	static {

		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam("consumer_key", "B2VtIGTPFrbg1YXUVujHhKIo5I9lVjBxgPIFk7A4");

			}
		};

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(END_POINT)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setRequestInterceptor(requestInterceptor)
				.build();

		service = restAdapter.create(ApiService500px.class);
	}

	public static ApiService500px getService() {
		return service;
	}

}
