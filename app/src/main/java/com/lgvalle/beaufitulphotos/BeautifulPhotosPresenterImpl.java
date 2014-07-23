package com.lgvalle.beaufitulphotos;

import android.util.Log;
import com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreEvent;
import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * Responsible for all business layer of it's UI {@link com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen}
 * <p/>
 * This class main functionality is to query photo service and post results into app bus.
 * It also produce photo events in the bus when a someone new subscribes.
 * <p/>
 * It is subscribed to two bus events:
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent} produced when the gallery needs to completely refresh it's content</li>
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreEvent} produce when gallery needs more items</li>
 * <p/>
 * In both cases queries photo service and dispatch results into app bus.
 */
public class BeautifulPhotosPresenterImpl implements BeautifulPhotosPresenter {
	private static final String TAG = BeautifulPhotosPresenterImpl.class.getSimpleName();
	/* UI layer interface */
	private final BeautifulPhotosScreen screen;
	/* Network service interface */
	private final ApiService500px service;
	/* Memory cached photo-model list */
	private List<? extends PhotoModel> photos;
	/* Service currentPage. Increments after successful operation */
	private int currentPage;
	private int totalPages;
	private String featureParam;

	/**
	 * Create presenter and set its dependencies
	 *
	 * @param screen  UI Layer interface
	 * @param service Network service interface
	 */
	public BeautifulPhotosPresenterImpl(BeautifulPhotosScreen screen, ApiService500px service) {
		this.screen = screen;
		this.service = service;
		resetPage();
	}

	/**
	 * Request photos to service.
	 * Save result to produce photo event to new subscribed listeners but also post immediately after fetching.
	 * Increments currentPage number after successful fetch.
	 * If failure, calls ui layer to display an error message.
	 *
	 * @param feature Feature param to get from service. Example: 'popular', 'highest rated'...
	 */
	@Override
	public void needPhotos(String feature) {
		if (currentPage == totalPages) {
			// No more available pages. Exit
			return;
		}
		featureParam = feature;

		service.getPhotosPopular(
				featureParam,
				ApiService500px.SIZE_SMALL,
				ApiService500px.SIZE_BIG, currentPage,
				new Callback<PhotosResponse>() {
					@Override
					public void failure(RetrofitError error) {
						// Display error message
						screen.showError(R.string.service_error);
						// Produce event with previous cached results
						BusHelper.post(producePhotosAvailableEvent());
					}

					@Override
					public void success(PhotosResponse data, Response response) {
						// Save photos info and post on bus
						photos = data.getPhotos();
						BusHelper.post(producePhotosAvailableEvent());

						// Update totalPages and currentPage info
						totalPages = data.getTotalPages();
						currentPage = data.getCurrentPage() + 1;
					}
				}
		);
	}

	/**
	 * Refresh whole gallery.
	 * Reset currentPage counter first
	 *
	 * @param event Event object is empty for this event
	 */
	@Subscribe
	public void onGalleryRefreshingEvent(GalleryRefreshingEvent event) {
		Log.d(TAG, "[BeautifulPhotosPresenterImpl - onGalleryRefreshingEvent] - (line 82): " + "");
		resetPage();
		needPhotos(featureParam);
	}

	/**
	 * Request next currentPage of items
	 *
	 * @param event Event object is empty for this event
	 */
	@Subscribe
	public void onGalleryRequestingMoreEvent(GalleryRequestingMoreEvent event) {
		Log.d(TAG, "[BeautifulPhotosPresenterImpl - onGalleryRequestingMoreEvent] - (line 92): " + "");
		needPhotos(featureParam);
	}

	/**
	 * Send last fetched photos to new subscribed listeners
	 */
	@Produce
	public PhotosAvailableEvent producePhotosAvailableEvent() {
		return new PhotosAvailableEvent(photos);
	}

	/**
	 * Increments currentPage number
	 */
	private void nextPage() {
		currentPage++;
	}

	/**
	 * Reset currentPage number to first one in current service
	 */
	private void resetPage() {
		currentPage = ApiService500px.FIRST_PAGE;
		totalPages = Integer.MAX_VALUE;
	}
}
