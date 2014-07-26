package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.facebook.rebound.*;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BlurTransformation;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Display a single photo in full screen.
 * <p/>
 * Initializes photoview with already cached thumbnail while fetching large image
 */
public class DetailsFragment extends BaseFragment {
	private static final String TAG = DetailsFragment.class.getSimpleName();
	private static final String EXTRA_PHOTO = "extra_photo";
	private PhotoModel photo;
	private boolean isFullscreen;

	@InjectView(R.id.photo)
	ImageView ivPhoto;
	@InjectView(R.id.photo_thumbnail)
	ImageView ivPhotoThumbnail;
	@InjectView(R.id.photo_author)
	TextView tvPhotoAuthor;
	@InjectView(R.id.info_container)
	View vInfoContainer;

	/** Spring animations */
	private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
	private Spring mSpring;
	private View decorView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup the Spring by creating a SpringSystem adding a SimpleListener that renders the
		// animation whenever the spring is updated.
		mSpring = SpringSystem
				.create()
				.createSpring()
				.setSpringConfig(ORIGAMI_SPRING_CONFIG)
				.addListener(new SimpleSpringListener() {
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
		// TODO - remove listener

		//mScaleSpring.removeListener(mSpringListener);
	}

	public static DetailsFragment newInstance(PhotoModel photo) {
		DetailsFragment f = new DetailsFragment();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_PHOTO, photo);
		f.setArguments(args);
		return f;
	}

	public static DetailsFragment newInstance() {
		return new DetailsFragment();
	}

	/**
	 * Click on photo toggle fullscreen visibility
	 */
	@OnClick(R.id.photo)
	public void onClickPhoto() {
		if (mSpring.getEndValue() == 0) {
			mSpring.setEndValue(1);
			hideSystemUI();
		} else {
			mSpring.setEndValue(0);
			showSystemUI();
		}
		/*
		if (isFullscreen) {
			showSystemUI();
		} else {
			hideSystemUI();
		}
		isFullscreen = !isFullscreen;
		*/
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photo_details;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(getContentView(), container, false);
		// Inject views after inflating and just before init layout. This way every child fragment follows correct sequence
		ButterKnife.inject(this, rootView);
		initLayout();
		return rootView;
	}

	/**
	 * Get Photo from arguments and init layout
	 */
	@Override
	protected void initLayout() {
		setupVisibilityChanges();
		if (getArguments() != null) {
			loadPhoto(getArguments().<PhotoModel>getParcelable(EXTRA_PHOTO));
		}
	}

	private void loadPhoto(PhotoModel photoModel) {
		photo = photoModel;

		// Text fields: author and photo title
		tvPhotoAuthor.setText(photo.getAuthorName());

		// Also, set photo title on actionbar
		setActionBarTitle(photo.getTitle());

		// Load cached placeholder image into ivPhotoThumbnail image view. This should be instant from cache
		// After that, always load large photo
		Picasso.with(getActivity()).load(photo.getSmallUrl()).transform(new BlurTransformation(getActivity())).into(ivPhotoThumbnail, new Callback() {
			@Override
			public void onError() {
				loadLargePhoto();
			}

			@Override
			public void onSuccess() {
				loadLargePhoto();
			}
		});
	}

	private void setupVisibilityChanges() {
		decorView = getActivity().getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener
				(new View.OnSystemUiVisibilityChangeListener() {
					@Override
					public void onSystemUiVisibilityChange(int visibility) {
						// Note that system bars will only be "visible" if none of the
						// LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
						if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
							showActionBar();
							mSpring.setEndValue(1);

							// TODO: The system bars are visible. Make any desired
							// adjustments to your UI, such as showing the action bar or
							// other navigational controls.
						} else {
							hideActionBar();
							//mSpring.setEndValue(1);
							// TODO: The system bars are NOT visible. Make any desired
							// adjustments to your UI, such as hiding the action bar or
							// other navigational controls.
						}
					}
				});
	}

	/**
	 * Hide action bar and info container. Dim UI
	 */
	private void hideSystemUI() {
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE);
		//vInfoContainer.setVisibility(View.GONE);
	}

	/**
	 * Load full size image
	 */
	private void loadLargePhoto() {
		// This could be called with some delay, so check if activity still exists.
		if (getActivity() != null) {
			Picasso.with(getActivity()).load(photo.getLargeUrl()).into(ivPhoto, new Callback() {
				@Override
				public void onSuccess() {
					// After large photo is loaded, hide thumbnail imageview
					//ivPhotoThumbnail.setVisibility(View.GONE);
				}

				@Override
				public void onError() {
					Toast.makeText(getActivity(), getString(R.string.service_error), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * Restore UI original visibility
	 */
	private void showSystemUI() {

		// Clear all flags
		decorView.setSystemUiVisibility(0);
		//vInfoContainer.setVisibility(View.VISIBLE);
	}

	private void render() {
		Log.d(TAG, "[DetailsFragment - render] - (line 236): " + "render!");
		double value = mSpring.getCurrentValue();
		// Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
		float barPosition =
				(float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, tvPhotoAuthor.getHeight());
		tvPhotoAuthor.setTranslationY(barPosition);
	}

	@Subscribe
	public void onGalleryItemChosen(GalleryItemChosenEvent event) {
		if (event != null && event.getPhoto() != null) {
			// Clear previous photo
			ivPhotoThumbnail.setImageBitmap(null);
			ivPhoto.setImageBitmap(null);
			// Load new photo
			loadPhoto(event.getPhoto());
		}
	}
}
