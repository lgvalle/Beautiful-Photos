package com.lgvalle.beaufitulphotos.fivehundredpxs;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by lgvalle on 21/07/14.
 */
public class ApiModule500px {
	private static final String END_POINT = "https://api.500px.com/v1";
	private static final String CONSUMER_KEY_PARAM = "consumer_key";
	private static final String CONSUMER_KEY_VALUE = "B2VtIGTPFrbg1YXUVujHhKIo5I9lVjBxgPIFk7A4";
	private static final ApiService500px service;

	/**
	 * Hide constructor
	 */
	private ApiModule500px() {}

	static {
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam(CONSUMER_KEY_PARAM, CONSUMER_KEY_VALUE);

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
