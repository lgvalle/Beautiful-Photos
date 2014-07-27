package com.lgvalle.beaufitulphotos.fivehundredpxs;

import android.content.Context;
import ly.apps.android.rest.client.RestClient;
import ly.apps.android.rest.client.RestClientFactory;
import ly.apps.android.rest.client.RestServiceFactory;

/**
 * Created by lgvalle on 21/07/14.
 *
 * Retrofit api module.
 * Builds a rest client based on api interface description
 */
public class ApiALTModule500px {
	private static final String END_POINT = "https://api.500px.com/v1";
	private static final String CONSUMER_KEY_VALUE = "B2VtIGTPFrbg1YXUVujHhKIo5I9lVjBxgPIFk7A4";
	private static ApiALTService500px service;

	public static void init(Context ctx) {
		RestClient client = RestClientFactory.defaultClient(ctx);
		service = RestServiceFactory.getService(END_POINT, ApiALTService500px.class, client);
	}

	/**
	 * Hide constructor
	 */
	private ApiALTModule500px() {}

	/**
	 * Expose rest client
	 */
	public static ApiALTService500px getService() {
		return service;
	}

}
