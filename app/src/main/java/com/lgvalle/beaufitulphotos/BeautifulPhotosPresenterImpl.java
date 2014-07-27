package com.lgvalle.beaufitulphotos;

import android.util.Log;
import com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.PhotoDetailsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiALTService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Favorites;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Photo500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;

import java.util.ArrayList;
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
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent} produce when gallery needs more items</li>
 * <p/>
 * In both cases queries photo service and dispatch results into app bus.
 */
public class BeautifulPhotosPresenterImpl implements BeautifulPhotosPresenter {
	private static final String TAG = BeautifulPhotosPresenterImpl.class.getSimpleName();
	/* UI layer interface */
	private final BeautifulPhotosScreen screen;
	/* Network service interface */
	private final ApiALTService500px service;
	/* Memory cached photo-model list */
	private List<Photo500px> photos;
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
	public BeautifulPhotosPresenterImpl(BeautifulPhotosScreen screen, ApiALTService500px service) {
		this.screen = screen;
		this.service = service;
		this.photos = new ArrayList<Photo500px>();
		resetPage();
	}

	/**
	 * Request more info about a photo (in this example, the count of favorites)
	 * The information is only requested if it's not already present
	 *
	 * @param p Photo object which we need more details of
	 */
	@Override
	public void needPhotoDetails(PhotoModel p) {
		final Photo500px photo = photos.get(photos.indexOf(p));
		if (photo.getFavorites() == null) {
			service.getFavorites(ApiALTService500px.CONSUMER_KEY_VALUE, p.getId(), new Callback<Favorites>() {
				@Override
				public void onResponse(Response<Favorites> favoritesResponse) {
					photo.setFavorites(favoritesResponse.getResult().getTotalItems());
					BusHelper.post(new PhotoDetailsAvailableEvent(photo));
				}
			});
		} else {
			BusHelper.post(new PhotoDetailsAvailableEvent(photo));
		}
	}

	/**
	 * Request photos to service.
	 * Save result to produce photo event to new subscribed listeners but also post immediately after fetching.
	 * Increments currentPage number after successful fetch.
	 * If failure, calls ui layer to display an error message.
	 *
	 * @param feature Feature param to get from service. Example: 'popular', 'highest rated', etc.
	 */
	@Override
	public void needPhotos(String feature) {
		if (currentPage < totalPages) {
			featureParam = feature;
			service.getPhotosPopular(ApiALTService500px.CONSUMER_KEY_VALUE, featureParam, ApiService500px.SIZE_SMALL, ApiService500px.SIZE_BIG, currentPage, new Callback<PhotosResponse>() {
						@Override
						public void onResponse(Response<PhotosResponse> photosResponseResponse) {
							// Cache photos info
							PhotosResponse data = photosResponseResponse.getResult();
							photos.addAll(data.getPhotos());
							// Post new results on bus
							BusHelper.post(new PhotosAvailableEvent(data.getPhotos()));
							// Update totalPages
							totalPages = data.getTotalPages();

							// //@author - lgvalle @date - 27/07/14 @time - 17:37
							//TODO: [BeautifulPhotosPresenterImpl - onResponse] - handle errors
							/*
								// Display error message
							screen.showError(R.string.service_error);
							// Page revert
							decrementPage();
							 */
						}


					}
			);
			incrementPage();
		}
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
		photos.clear();
		resetPage();
		needPhotos(featureParam);
	}

	/**
	 * Request next currentPage of items
	 *
	 * @param event Event object is empty for this event
	 */
	@Subscribe
	public void onGalleryRequestingMoreEvent(GalleryRequestingMoreElementsEvent event) {
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

	@Override
	public void setFeature(String param) {
		featureParam = param;
	}

	private void decrementPage() {
		if (currentPage > ApiService500px.FIRST_PAGE) {
			currentPage--;
		}
	}

	/**
	 * Increments currentPage number
	 */
	private void incrementPage() {
		if (currentPage < totalPages) {
			currentPage++;
		}
	}

	/**
	 * Reset currentPage number to first one in current service
	 */
	private void resetPage() {
		currentPage = ApiService500px.FIRST_PAGE;
		totalPages = Integer.MAX_VALUE;
	}


}
