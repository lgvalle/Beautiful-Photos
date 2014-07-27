package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.facebook.rebound.*;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.events.PhotoDetailsAvailableEvent;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.lgvalle.beaufitulphotos.utils.PicassoHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Display a single photo in full screen.
 * <p/>
 * Initializes photoview with already cached thumbnail while fetching large image
 */
public class DetailsFragment extends BaseFragment {
	private static final String TAG = DetailsFragment.class.getSimpleName();
	private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
	@InjectView(R.id.photo)
	ImageView ivPhoto;
	@InjectView(R.id.photo_thumbnail)
	ImageView ivPhotoThumbnail;
	@InjectView(R.id.photo_author)
	TextView tvPhotoAuthor;
	@InjectView(R.id.photo_favorites)
	TextView tvPhotoFavorites;
	@InjectView(R.id.info_container)
	View vInfoContainer;
	@InjectView(R.id.photo_favorites_container)
	View vFavoritesContainer;

	private Spring mSpring;
	private View decorView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup the Spring by creating a SpringSystem adding a SimpleListener that renders the
		// animation whenever the spring is updated.

		mSpring = SpringSystem.create().createSpring().setSpringConfig(ORIGAMI_SPRING_CONFIG).addListener(new SimpleSpringListener() {
			@Override
			public void onSpringUpdate(Spring spring) {
				// Just tell the UI to update based on the springs current state.
				render();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		BusHelper.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHelper.unregister(this);
		decorView.setOnSystemUiVisibilityChangeListener(null);
	}

	/**
	 * Fragment Constructor
	 */
	public static DetailsFragment newInstance() {
		return new DetailsFragment();
	}

	/**
	 * Click on photo switch to fullscreen and hide any other ui elements
	 */
	@OnClick(R.id.photo)
	public void onClickPhoto() {
		uiHide();
	}

	/**
	 * When a new Gallery Item is selected, clear previous image views and load the new one
	 *
	 * @param event
	 */
	@Subscribe
	public void onGalleryItemChosen(GalleryItemChosenEvent event) {
		if (event != null && event.getPhoto() != null) {
			// Clear previous photo
			ivPhotoThumbnail.setImageBitmap(null);
			ivPhoto.setImageBitmap(null);
			// Load new photo
			bindImages(event.getPhoto());
			bindTexts(event.getPhoto());
		}
	}

	@Subscribe
	public void onPhotoDetailsAvailableEvent(PhotoDetailsAvailableEvent event) {
		if (event != null && event.getPhoto() != null) {
			bindTexts(event.getPhoto());
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photo_details;
	}

	/**
	 * Layout is initialized empty. Photos are loaded by listening to bus events
	 */
	@Override
	protected void initLayout() {
		// Set listener to react on view visibility changes: restoring UI when coming back from fullscreen
		decorView = getActivity().getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					uiRestore();
				}
			}
		});
	}

	/**
	 * Load photo info from photoModel.
	 * First load thumbnail as background, and then load large photo in foreground
	 *
	 * @param photoModel Object containing photo info
	 */
	private void bindImages(final PhotoModel photoModel) {
		// Start by loading thumbnail photo for background image (this should be instant) Then load current large photo
		PicassoHelper.load(getActivity(), photoModel.getSmallUrl(), ivPhotoThumbnail, new Callback() {
			@Override
			public void onError() {
				PicassoHelper.load(getActivity(), photoModel.getLargeUrl(), ivPhoto);
			}

			@Override
			public void onSuccess() {
				PicassoHelper.load(getActivity(), photoModel.getLargeUrl(), ivPhoto);
			}
		});
	}

	private void bindTexts(PhotoModel photoModel) {
		// Photo Author is always present
		tvPhotoAuthor.setText(photoModel.getAuthorName());

		// Photo Favorites is an extra data, could not be available
		if (photoModel.getFavorites() == null) {
			vFavoritesContainer.setVisibility(View.INVISIBLE);

		} else {
			vFavoritesContainer.setVisibility(View.VISIBLE);
			tvPhotoFavorites.setText(String.valueOf(photoModel.getFavorites()));
		}

		// Also, set photo title on actionbar
		setActionBarTitle(photoModel.getTitle());
	}


	/**
	 * Render spring animations
	 */
	private void render() {
		double value = mSpring.getCurrentValue();
		// Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
		float position = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, vInfoContainer.getHeight());
		vInfoContainer.setTranslationY(position);
	}

	/**
	 * Hide action bar and info container. Dim UI
	 */
	private void uiHide() {
		// Animate UI away
		mSpring.setEndValue(1);
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	/**
	 * Restore UI original visibility
	 */
	private void uiRestore() {
		// Show back action bar
		showActionBar();
		// Animate back UI
		mSpring.setEndValue(0);
		// Clear all flags
		decorView.setSystemUiVisibility(0);
	}
}