package com.lgvalle.beaufitulphotos;

import android.util.Log;
import com.lgvalle.beaufitulphotos.event.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.interfaces.PopularPhotosPresenter;
import com.lgvalle.beaufitulphotos.util.BusHelper;
import com.squareup.otto.Produce;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class BeautifulPhotosPresenterImpl implements PopularPhotosPresenter {
	private static final String TAG = BeautifulPhotosPresenterImpl.class.getSimpleName();
	private final BeautifulPhotosActivity screen;
	private final ApiService500px service;
	private List<? extends PhotoModel> photos;

	public BeautifulPhotosPresenterImpl(BeautifulPhotosActivity beautifulPhotosActivity, ApiService500px service) {
		this.screen = beautifulPhotosActivity;
		this.service = service;
	}

	@Override
	public void needPhotos() {
		service.getPhotosPopular(new Callback<PhotosResponse>() {
			@Override
			public void success(PhotosResponse data, Response response) {
				Log.d(TAG, "[PhotosNearbyPresenterImpl - success] - (line 34): " + "");
				photos = data.getPhotos();
				BusHelper.post(producePhotosAvailableEvent());

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
	public PhotosAvailableEvent producePhotosAvailableEvent() {
		Log.d(TAG, "[PhotosNearbyPresenterImpl - producePhotosAvailableEvent] - (line 54): " + "");
		return new PhotosAvailableEvent(photos);
	}
}
