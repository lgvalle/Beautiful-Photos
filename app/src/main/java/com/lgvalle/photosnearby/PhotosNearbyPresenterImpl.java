package com.lgvalle.photosnearby;

import android.util.Log;
import com.lgvalle.photosnearby.event.PhotosAvailableEvent;
import com.lgvalle.photosnearby.interfaces.PhotosNearbyPresenter;
import com.lgvalle.photosnearby.model.Photo500Px;
import com.lgvalle.photosnearby.model.PhotosResponse;
import com.lgvalle.photosnearby.net.ApiModule500px;
import com.lgvalle.photosnearby.util.BusHelper;
import com.squareup.otto.Produce;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosNearbyPresenterImpl implements PhotosNearbyPresenter{
	private static final String TAG = PhotosNearbyPresenterImpl.class.getSimpleName();
	private final PhotosNearbyActivity screen;
	private final ApiModule500px.ApiService500px service;
	private List<Photo500Px> photos;

	public PhotosNearbyPresenterImpl(PhotosNearbyActivity photosNearbyActivity, ApiModule500px.ApiService500px service) {
		this.screen = photosNearbyActivity;
		this.service = service;
	}

	@Override
	public void needPhotos() {
		Log.d(TAG, "[PhotosNearbyPresenterImpl - needPhotos] - (line 29): " + "");
		service.getPhotosPopular(new Callback<PhotosResponse>() {
			@Override
			public void success(PhotosResponse data, Response response) {
				Log.d(TAG, "[PhotosNearbyPresenterImpl - success] - (line 34): " + "");
				photos = data.getPhotos();
				producePhotosAvailableEvent();

			}

			@Override
			public void failure(RetrofitError error) {
				Log.e(TAG, "[PhotosNearbyPresenterImpl - failure] - (line 40): " + "", error);
				//@author - lgvalle @date - 21/07/14 @time - 20:30
				//FIXME: [PhotosNearbyPresenterImpl - failure] - error handle
			}
		});
	}

	@Produce
	private void producePhotosAvailableEvent() {
		Log.d(TAG, "[PhotosNearbyPresenterImpl - producePhotosAvailableEvent] - (line 54): " + "");
		BusHelper.post(new PhotosAvailableEvent(photos));
	}
}
