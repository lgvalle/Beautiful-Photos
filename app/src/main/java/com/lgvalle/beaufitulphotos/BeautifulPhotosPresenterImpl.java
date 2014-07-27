package com.lgvalle.beaufitulphotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.lgvalle.beaufitulphotos.events.GalleryReloadEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.PhotoDetailsAvailableEvent;
import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiALTService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiService500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Favorites;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Feature;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.PhotosResponse;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import org.apache.http.HttpStatus;

import java.util.ArrayList;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * Responsible for all business layer of it's UI {@link com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen}
 * <p/>
 * This class main functionality is to query photo service and post results into app bus.
 * It also produce photo events in the bus when a someone new subscribes.
 * <p/>
 * It is subscribed to two bus events:
 * <li>{@link com.lgvalle.beaufitulphotos.events.GalleryReloadEvent} produced when the gallery needs to completely refresh it's content</li>
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
	private Feature currentFeature;
	/* Memory cached photo-model list */
	private ArrayList<PhotoModel> photos;
	/* Service currentPage. Increments after successful operation */
	private int currentPage;
	/* Service total pages */
	private int totalPages;
	/* Index of current displayed item */
	private int itemIndex;

	/**
	 * Create presenter and set its dependencies
	 *
	 * @param screen  UI Layer interface
	 * @param service Network service interface
	 */
	public BeautifulPhotosPresenterImpl(BeautifulPhotosScreen screen, ApiALTService500px service, Feature feature) {
		this.screen = screen;
		this.service = service;
		this.currentFeature = feature;
		this.photos = new ArrayList<PhotoModel>();
		resetPage();
	}

	/**
	 * Request more info about a photo (in this example, the count of favorites)
	 * The information is only requested if it's not already present
	 *
	 * @param photoModel Photo object which we need more details of
	 */
	@Override
	public void needPhotoDetails(PhotoModel photoModel) {
		itemIndex = photos.indexOf(photoModel);
		if (itemIndex == -1) {
			Log.e(TAG, "[BeautifulPhotosPresenterImpl - needPhotoDetails] - (line 93): " + "Asking for details of a not saved item. Should never happen");
			return;
		}

		final PhotoModel photo = photos.get(itemIndex);
		// First time asking for details (favorites in this example) we get them from server and save them into the photo item to avoid future calls
		if (photo.getFavorites() == null) {
			service.getFavorites(ApiALTService500px.CONSUMER_KEY_VALUE, photoModel.getId(), new Callback<Favorites>() {
				@Override
				public void onResponse(Response<Favorites> response) {
					if (response.getStatusCode() == HttpStatus.SC_OK && response.getResult() != null) {
						// Save details to avoid future calls and post item on bus
						photo.setFavorites(response.getResult().getTotalItems());
						BusHelper.post(new PhotoDetailsAvailableEvent(photo));
					} else {
						/* if error nothing to do */
					}
				}
			});
		} else {
			// Already got details, just post photo item on bus
			BusHelper.post(new PhotoDetailsAvailableEvent(photo));
		}

	}

	/**
	 * Request photos to service.
	 * Save result to produce photo event to new subscribed listeners but also post immediately after fetching.
	 * Increments currentPage number after successful fetch.
	 * If failure, calls ui layer to display an error message.
	 */
	@Override
	public void needPhotos() {
		if (currentPage > totalPages) {
			// Already at the end. No more pages
			Log.i(TAG, "[BeautifulPhotosPresenterImpl - needPhotos] - (line 112): " + "No more pages");
			return;
		}

		service.getPhotosPopular(ApiALTService500px.CONSUMER_KEY_VALUE, currentFeature.getParam(), ApiService500px.SIZE_SMALL, ApiService500px.SIZE_BIG, currentPage, new Callback<PhotosResponse>() {
					@Override
					public void onResponse(Response<PhotosResponse> response) {
						if (response.getStatusCode() == HttpStatus.SC_OK && response.getResult() != null) {
							// Cache photos info
							PhotosResponse data = response.getResult();
							photos.addAll(data.getPhotos());
							// Post new results on bus
							BusHelper.post(new PhotosAvailableEvent(data.getPhotos()));
							// Update totalPages
							totalPages = data.getTotalPages();
						} else {
							// Display error message
							screen.showError(R.string.service_error);
							// Page revert
							decrementPage();
						}
					}
				}
		);
		incrementPage();
		// Update UI with current feature info
		screen.updateTitle(currentFeature.getTitle());

	}

	/**
	 * Request next currentPage of items
	 *
	 * @param event Event object is empty for this event
	 */
	@Subscribe
	public void onGalleryRequestingMoreEvent(GalleryRequestingMoreElementsEvent event) {
		Log.d(TAG, "[BeautifulPhotosPresenterImpl - onGalleryRequestingMoreEvent] - (line 92): " + "");
		needPhotos();
	}

	/**
	 * Switch to a new feature means reloading everything
	 *
	 * @param feature Feature to switch to
	 */
	@Override
	public void switchFeature(Feature feature) {
		currentFeature = feature;
		// Post this event on bus (Gallery UI should be listening and do whatever is needed)
		BusHelper.post(new GalleryReloadEvent());
		// Clear memory cached items
		photos.clear();
		// Reset page count
		resetPage();
		// Finally, request photos for new feature
		needPhotos();
	}

	/**
	 * Launch intent to share current photo
	 */
	@Override
	public void share(final Context ctx) {
		final PhotoModel photo = photos.get(itemIndex);

		// Picasso already has cached this image, so extract cached bitmap from its cache
		Picasso.with(ctx).load(photo.getLargeUrl()).into(new Target() {
			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
			}

			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
				// Get bitmap uri from filesystem and create intent with it.
				// TODO: do this in a new separate thread
				String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap, photo.getTitle(), null);
				Uri uri = Uri.parse(path);
				final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Intent.EXTRA_STREAM, uri);
				intent.setType("image/png");
				ctx.startActivity(intent);
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {

			}
		});

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
